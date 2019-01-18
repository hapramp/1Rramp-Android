package com.hapramp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.analytics.EventReporter;
import com.hapramp.draft.DraftsHelper;
import com.hapramp.draft.ShortPostDraftModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.GoogleImageFilePathReader;
import com.hapramp.utils.HashTagUtils;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.PostHashTagPreprocessor;
import com.hapramp.views.post.PostCreateComponent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatePostActivity extends AppCompatActivity implements SteemPostCreator.SteemPostCreatorCallback, DraftsHelper.DraftsHelperCallback {
  public static final String EXTRA_KEY_DRAFT_ID = "draftId";
  public static final String EXTRA_KEY_DRAFT_JSON = "draftJson";
  private static final int REQUEST_IMAGE_SELECTOR = 101;
  private static final int REQUEST_CAPTURE_IMAGE = 100;
  private static final int REQUEST_WRITE_PERMISSION = 1023;
  private static final int RC_QUOTE = 103;
  private final long NO_DRAFT = -1;
  @BindView(R.id.backBtn)
  ImageView closeBtn;
  @BindView(R.id.postButton)
  TextView postButton;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.post_create_component)
  PostCreateComponent postCreateComponent;
  @BindView(R.id.scroll_view)
  ScrollView scrollView;
  private ProgressDialog progressDialog;
  private List<String> tags;
  private String generated_permalink;
  private SteemPostCreator steemPostCreator;
  private List<String> images;
  private String body;
  private String cameraImageFilePath;
  private long mDraftId = NO_DRAFT;
  private boolean postPublished;
  private DraftsHelper draftsHelper;
  private boolean leftActivityWithPurpose = false;
  private boolean shouldSaveOrUpdateDraft = true;
  private String title;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_post_creation);
    ButterKnife.bind(this);
    init();
    initProgressDialog();
    attachListener();
    EventReporter.addEvent(AnalyticsParams.EVENT_OPENS_POST_CREATE);
  }

  private void init() {
    draftsHelper = new DraftsHelper();
    draftsHelper.setDraftsHelperCallback(this);
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    steemPostCreator = new SteemPostCreator();
    steemPostCreator.setSteemPostCreatorCallback(this);
    Intent intent = getIntent();
    String action = intent.getAction();
    String type = intent.getType();
    if (Intent.ACTION_SEND.equals(action) && type != null) {
      if ("text/plain".equals(type)) {
        handleSendText(intent);
      } else if (type.startsWith("image/")) {
        handleSendImage(intent);
      }
    }

    Intent receiveIntent = getIntent();
    if (receiveIntent != null) {
      mDraftId = receiveIntent.getLongExtra(EXTRA_KEY_DRAFT_ID, NO_DRAFT);
      if (mDraftId != NO_DRAFT) {
        String draftJson = receiveIntent.getStringExtra(EXTRA_KEY_DRAFT_JSON);
        try {
          ShortPostDraftModel draftModel = new Gson().fromJson(draftJson, ShortPostDraftModel.class);
          configureEditor(draftModel);
        }
        catch (Exception e) {
          e.printStackTrace();
          configureEditor(null);
        }
      }
    } else {
      configureEditor(null);
    }
  }

  private void initProgressDialog() {
    progressDialog = new ProgressDialog(this);
    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(false);
    progressDialog.setMessage("Publishing...");
  }

  private void attachListener() {
    closeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showExistAlert();
      }
    });
    postButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        publishPost();
      }
    });
    postCreateComponent.setMediaSelectorListener(new PostCreateComponent.MediaSelectorListener() {
      @Override
      public void onImageInsertOptionSelected() {
        openGallery();
      }

      @Override
      public void onQuoteInsertOptionSelected() {
        checkWritePermissionAndOpenQuoteEditor();
      }

      @Override
      public void onCameraImageSelected() {
        checkCameraPermission();
      }
    });
  }

  private void handleSendText(Intent intent) {
    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
    postCreateComponent.setDefaultText(sharedText);
  }

  private void handleSendImage(Intent intent) {
    try {
      Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
      intent.setData(imageUri);
      handleImageResult(intent);
    }
    catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
    }
  }

  private void configureEditor(ShortPostDraftModel draftModel) {
    if (draftModel != null) {
      loadShortPostDraftIntoEditor(draftModel);
    }
  }

  private void showExistAlert() {
    if (!isValidDraftToSave()) {
      close();
      return;
    }
    //if there is already draft, show a progress with saving...
    if (mDraftId != NO_DRAFT) {
      showPublishingProgressDialog(true, "Saving changes...");
      shouldSaveOrUpdateDraft = false;
      updateDraft();
      return;
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(this)
      .setTitle("Save as draft?")
      .setMessage("You can edit and publish saved drafts later.")
      .setPositiveButton("Save Draft", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          //save or update draft
          shouldSaveOrUpdateDraft = true;
          close();
        }
      })
      .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          // in delete mode
          shouldSaveOrUpdateDraft = false;
          close();
        }
      });
    builder.show();
  }

  private void publishPost() {
    if (!ConnectionUtils.isConnected(this)) {
      showConnectivityError();
      return;
    }
    if (validPost()) {
      showPublishingProgressDialog(true, "Preparing Your Post...");
      preparePost();
      showPublishingProgressDialog(true, "Pre-processing...");
      sendPostToSteemBlockChain();
    }
  }

  private void openGallery() {
    leftActivityWithPurpose = true;
    try {
      if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ||
        ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
      } else {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void checkWritePermissionAndOpenQuoteEditor() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      try {
        if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
          openQuoteEditor();
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      openQuoteEditor();
    }
  }

  private void checkCameraPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      try {
        if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
          ||
          ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAPTURE_IMAGE);
        } else {
          openCameraIntent();
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      openCameraIntent();
    }
  }

  private void handleImageResult(final Intent intent) {
    final Handler handler = new Handler();
    new Thread() {
      @Override
      public void run() {
        final String filePath = GoogleImageFilePathReader.getImageFilePath(CreatePostActivity.this, intent);
        handler.post(new Runnable() {
          @Override
          public void run() {
            if (filePath != null) {
              addImage(filePath);
            } else {
              Toast.makeText(CreatePostActivity.this, "Failed to read shared image", Toast.LENGTH_LONG).show();
            }
          }
        });
      }
    }.start();
  }

  public void loadShortPostDraftIntoEditor(ShortPostDraftModel draft) {
    //set image
    String imageUrl = draft.getPostImageUrl();
    if (imageUrl != null) {
      if (imageUrl.length() > 0) {
        postCreateComponent.setImageDownloadUrl(imageUrl);
      }
    }
    //set title
    postCreateComponent.setTitle(draft.getTitle());
    //set text
    postCreateComponent.setDefaultText(draft.getText());
    //set community
    postCreateComponent.setDefaultCommunitySelection(draft.getCommunities());
  }

  private boolean isValidDraftToSave() {
    return postCreateComponent.getImageList().size() > 0 || postCreateComponent.getContent().length() > 0;
  }

  private void close() {
    closeCreatePostPage();
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
  }

  private void showPublishingProgressDialog(boolean show, String msg) {
    if (progressDialog != null) {
      if (show) {
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
      } else {
        progressDialog.dismiss();
      }
    } else {
      progressDialog.dismiss();
    }
  }

  private void updateDraft() {
    ShortPostDraftModel shortPostDraftModel = new ShortPostDraftModel();
    if (postCreateComponent.getImageList().size() > 0) {
      shortPostDraftModel.setPostImageUrl(postCreateComponent.getImageList().get(0));
    }
    if (postCreateComponent.getBody().length() > 0) {
      shortPostDraftModel.setText(postCreateComponent.getContent());
    }
    shortPostDraftModel.setCommunities(postCreateComponent.getSelectedCommunityTags());
    String draftTitle = postCreateComponent.getTitle();
    shortPostDraftModel.setTitle(draftTitle);
    shortPostDraftModel.setDraftId(mDraftId);
    draftsHelper.updateShortPostDraft(shortPostDraftModel);
  }

  private void showConnectivityError() {
    Snackbar.make(toolbarContainer, "No Internet!", Snackbar.LENGTH_SHORT).show();
  }

  private boolean validPost() {
    if (postCreateComponent.getSelectedCommunityTags().size() > 1) {
      if (postCreateComponent.isMediaSelected()) {
        if (postCreateComponent.isMediaUploaded()) {
          return true;
        } else {
          toast("Please wait until the upload is complete...");
          return false;
        }
      } else {
        if (postCreateComponent.isContentEnough()) {
          return true;
        } else {
          toast("Write something more...");
        }
      }
    } else {
      toast("Select atleast 1 community");
    }
    return false;
  }

  private void preparePost() {
    generated_permalink = PermlinkGenerator.getPermlink();
    title = postCreateComponent.getTitle();
    body = postCreateComponent.getBody();
    body = body + Constants.FOOTER_TEXT;
    tags = postCreateComponent.getSelectedCommunityTags();
    tags.addAll(getHashTagsFromBody(body));
    tags = PostHashTagPreprocessor.processHashtags(tags);
    body = HashTagUtils.cleanHashTagsFromBody(body);
    images = postCreateComponent.getImageList();
  }

  private void sendPostToSteemBlockChain() {
    showPublishingProgressDialog(true, "Publishing...");
    steemPostCreator.createPost(body, title, images, tags, generated_permalink);
  }

  private void openQuoteEditor() {
    Intent intent = new Intent(this, QuoteEditorActivity.class);
    startActivityForResult(intent, RC_QUOTE);
  }

  private void openCameraIntent() {
    leftActivityWithPurpose = true;
    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (pictureIntent.resolveActivity(getPackageManager()) != null) {
      File photoFile = null;
      try {
        photoFile = createImageFile();
      }
      catch (IOException ex) {

      }
      if (photoFile != null) {
        Uri photoURI = FileProvider.getUriForFile(this, "com.hapramp.provider", photoFile);
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
          photoURI);
        startActivityForResult(pictureIntent,
          REQUEST_CAPTURE_IMAGE);
      }
    }
  }

  private void addImage(String filePath) {
    postCreateComponent.setImageResource(filePath);
  }

  private void closeCreatePostPage() {
    finish();
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }

  private ArrayList<String> getHashTagsFromBody(String body) {
    return HashTagUtils.getHashTags(body);
  }

  private File createImageFile() throws IOException {
    String timeStamp =
      new SimpleDateFormat("yyyyMMdd_HHmmss",
        Locale.getDefault()).format(new Date());
    String imageFileName = "IMG_" + timeStamp + "_";
    File storageDir =
      getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
      imageFileName,  /* prefix */
      ".jpg",         /* suffix */
      storageDir      /* directory */
    );
    cameraImageFilePath = image.getAbsolutePath();
    return image;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_SELECTOR &&
      resultCode == Activity.RESULT_OK &&
      data != null && data.getData() != null) {
      leftActivityWithPurpose = false;
      handleImageResult(data);
    }

    if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
      leftActivityWithPurpose = false;
      addImage(cameraImageFilePath);
    }

    if (requestCode == RC_QUOTE && resultCode == RESULT_OK) {
      Bundle bundle = data.getExtras();
      String quoteUrl = bundle.getString(QuoteEditorActivity.QUOTE_IMAGE_URL);
      addImage(quoteUrl, true);
    }
  }

  private void addImage(String filePath, boolean deleteAfterUse) {
    postCreateComponent.setImageResource(filePath, deleteAfterUse);
  }

  @Override
  public void onBackPressed() {
    showExistAlert();
  }

  @Override
  protected void onPause() {
    super.onPause();
    invalidateDraft();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_IMAGE_SELECTOR:
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          openGallery();
        } else {
          //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
          toast("Permission not granted to access images.");
        }
        break;
      case REQUEST_CAPTURE_IMAGE:
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          openCameraIntent();
        } else {
          //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
          toast("Permission not granted to capture images.");
        }
        break;
    }
  }

  private void invalidateDraft() {
    if (mDraftId != NO_DRAFT) {
      if (postPublished) {
        //delete draft
        deleteDraft();
      } else {
        //update draft
        if (!leftActivityWithPurpose) {
          if (shouldSaveOrUpdateDraft) {
            updateDraft();
          }
        }
      }
    } else {
      if (!postPublished && !leftActivityWithPurpose) {
        if (shouldSaveOrUpdateDraft) {
          //save draft
          addNewDraft();
        }
      }
    }
  }

  private void deleteDraft() {
    draftsHelper.deleteDraft(mDraftId);
  }

  private void addNewDraft() {
    if (!isValidDraftToSave()) {
      return;
    }
    ShortPostDraftModel shortPostDraftModel = new ShortPostDraftModel();
    if (postCreateComponent.getImageList().size() > 0) {
      shortPostDraftModel.setPostImageUrl(postCreateComponent.getImageList().get(0));
    }

    if (postCreateComponent.getContent().length() > 0) {
      shortPostDraftModel.setText(postCreateComponent.getContent());
    }
    shortPostDraftModel.setCommunities(postCreateComponent.getSelectedCommunityTags());
    String draftTitle = postCreateComponent.getTitle();
    shortPostDraftModel.setTitle(draftTitle);
    draftsHelper.saveShortPostDraft(shortPostDraftModel);
  }

  @Override
  public void onPostCreatedOnSteem() {
    toast("Published");
    postPublished = true;
    showPublishingProgressDialog(false, "");
    postCreated();
  }

  private void postCreated() {
    showPublishingProgressDialog(false, "");
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_CREATE_POST);
    HaprampPreferenceManager.getInstance().setLastPostCreatedAt(MomentsUtils.getCurrentTime());
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        closeCreatePostPage();
      }
    }, 1000);
  }

  @Override
  public void onPostCreationFailedOnSteem(String msg) {
    toast("Cannot create post.");
    Log.d("PostCreate", msg);
    showPublishingProgressDialog(false, "");
  }

  @Override
  public void onNewDraftSaved(boolean success) {
  }

  @Override
  public void onDraftUpdated(boolean success) {
    showPublishingProgressDialog(false, "");
    close();
  }

  @Override
  public void onDraftDeleted(boolean success) {
  }
}
