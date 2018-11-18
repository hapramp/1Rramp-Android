package com.hapramp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.analytics.EventReporter;
import com.hapramp.api.URLS;
import com.hapramp.draft.DraftListItemModel;
import com.hapramp.draft.DraftsHelper;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.GoogleImageFilePathReader;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.PostHashTagPreprocessor;
import com.hapramp.views.editor.LinkInsertDialog;
import com.hapramp.views.hashtag.CustomHashTagInput;
import com.hapramp.views.post.PostCommunityView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xute.markdeditor.EditorControlBar;
import xute.markdeditor.MarkDEditor;
import xute.markdeditor.datatype.DraftDataItemModel;
import xute.markdeditor.models.DraftModel;

import static xute.markdeditor.Styles.TextComponentStyle.NORMAL;

public class CreateArticleActivity extends AppCompatActivity implements SteemPostCreator.SteemPostCreatorCallback, EditorControlBar.EditorControlListener, DraftsHelper.BlogDraftsDatabaseCallbacks {
  public static final String EXTRA_KEY_DRAFT_ID = "draftId";
  private static final int REQUEST_IMAGE_SELECTOR = 119;
  private final long NO_DRAFT = -1;
  @BindView(R.id.backBtn)
  ImageView closeBtn;
  @BindView(R.id.previewButton)
  TextView previewButton;
  @BindView(R.id.nextButton)
  TextView nextButton;
  @BindView(R.id.meta_toolbar_container)
  RelativeLayout meta_toolbarContainer;
  @BindView(R.id.backBtnFromArticleMeta)
  ImageView backBtnFromArticleMeta;
  @BindView(R.id.publishButton)
  TextView publishButton;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.community_caption)
  TextView communityCaption;
  @BindView(R.id.articleCategoryView)
  PostCommunityView articleCategoryView;
  @BindView(R.id.tagsCaption)
  TextView tagsCaption;
  @BindView(R.id.tagsInputBox)
  CustomHashTagInput tagsInputBox;
  @BindView(R.id.skills_wrapper)
  RelativeLayout skillsWrapper;
  @BindView(R.id.metaView)
  RelativeLayout metaView;
  @BindView(R.id.article_title)
  EditText articleTitleEt;
  @BindView(R.id.mdEditor)
  MarkDEditor markDEditor;
  @BindView(R.id.controlBar)
  EditorControlBar editorControlBar;
  private ProgressDialog progressDialog;
  private String title;
  private ArrayList<String> tags;
  private String generated_permalink;
  private String body;
  private List<String> images;
  private DraftsHelper draftsHelper;
  private long mDraftId;
  private boolean blogPublished;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_article);
    ButterKnife.bind(this);
    init();
    attachListeners();
    EventReporter.addEvent(AnalyticsParams.EVENT_OPENS_ARTICLE_CREATE);
  }

  private void init() {
    draftsHelper = new DraftsHelper(this);
    draftsHelper.setBlogDraftCallbacks(this);
    progressDialog = new ProgressDialog(this);
    articleCategoryView.initCategory();
    editorControlBar.setEditorControlListener(this);
    editorControlBar.setEditor(markDEditor);
    Intent receiveIntent = getIntent();
    if (receiveIntent != null) {
      long draftId = receiveIntent.getLongExtra(EXTRA_KEY_DRAFT_ID, NO_DRAFT);
      configureEditor(draftId);
    } else {
      configureEditor(NO_DRAFT);
    }
  }

  private void attachListeners() {
    nextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showMetaData(true);
      }
    });

    backBtnFromArticleMeta.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showMetaData(false);
      }
    });

    publishButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (ConnectionUtils.isConnected(CreateArticleActivity.this)) {
          publishArticle();
        } else {
          showConnectivityError();
        }
      }
    });

    closeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showExistAlert();
      }
    });

  }

  private void configureEditor(long draftId) {
    this.mDraftId = draftId;
    if (draftId != NO_DRAFT) {
      showProgressDialog(true, "Loading Draft...");
      //load draft
      draftsHelper.fetchBlogDraftById(draftId);
      markDEditor.configureEditor(URLS.BASE_URL,
        HaprampPreferenceManager.getInstance().getUserToken(),
        true,
        "",
        NORMAL);
      //must call loadDraft() when draft loaded
    } else {
      markDEditor.configureEditor(URLS.BASE_URL,
        HaprampPreferenceManager.getInstance().getUserToken(),
        false,
        "Body here...",
        NORMAL);
    }
  }

  private void showMetaData(boolean show) {
    int vis = show ? View.VISIBLE : View.GONE;
    metaView.setVisibility(vis);
  }

  private void publishArticle() {
    title = articleTitleEt.getText().toString().trim();
    generated_permalink = PermlinkGenerator.getPermlink(title);
    tags = (ArrayList<String>) articleCategoryView.getSelectedTags();
    includeCustomTags(tags);
    tags = PostHashTagPreprocessor.processHashtags(tags);
    body = markDEditor.getMarkdownContent();
    images = getImageLinks();
    if (validArticle()) {
      sendPostToSteemBlockChain();
    }
  }

  private void showConnectivityError() {
    Snackbar.make(toolbarContainer, "No Internet!", Snackbar.LENGTH_SHORT).show();
  }

  private void showExistAlert() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this)
      .setTitle("Exit ?")
      .setMessage("Your draft will be saved.")
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          close();
        }
      })
      .setNegativeButton("No", null);
    builder.show();
  }

  private void showProgressDialog(boolean show, String msg) {
    if (progressDialog != null) {
      if (show) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
      } else {
        progressDialog.dismiss();
      }
    }
  }

  private void includeCustomTags(ArrayList<String> tags) {
    tags.addAll(tagsInputBox.getHashTags());
  }

  private List<String> getImageLinks() {
    List<String> images = markDEditor.getImageList();
    for (int i = 0; i < images.size(); i++) {
      if (images.get(i) == null) {
        images.remove(i);
      }
    }
    return images;
  }

  private boolean validArticle() {
    if (title.length() > 0) {
      if (tags.size() > 0) {
        if (body.length() > 0) {
          return true;
        } else {
          toast("Article is too short!");
        }
      } else {
        toast("Select atleast 1 community!");
      }
    } else {
      toast("Article should have some title.");
    }
    return false;
  }

  private void sendPostToSteemBlockChain() {
    showProgressDialog(true, "Publishing...");
    SteemPostCreator steemPostCreator = new SteemPostCreator();
    steemPostCreator.setSteemPostCreatorCallback(this);
    //add footer
    body = body + Constants.FOOTER_TEXT;
    steemPostCreator.createPost(body, title, images, tags, generated_permalink);
  }

  private void close() {
    finish();
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPostCreatedOnSteem() {
    toast("Published");
    HaprampPreferenceManager.getInstance().setLastPostCreatedAt(MomentsUtils.getCurrentTime());
    blogPublished = true;
    closeEditor();
  }

  private void closeEditor() {
    showProgressDialog(false, "");
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_CREATE_ARTICLE);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        finish();
      }
    }, 1000);
  }

  @Override
  public void onPostCreationFailedOnSteem(String msg) {
    toast("Cannot Create Blog");
    showProgressDialog(false, "");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_SELECTOR) {
      if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
        handleImageResult(data);
      }
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    invalidateDraft();
  }

  private void invalidateDraft() {
    if (mDraftId != NO_DRAFT) {
      if (blogPublished) {
        //delete draft
        deleteDraft();
      } else {
        //update draft
        updateDraft();
      }
    } else {
      if (!blogPublished) {
        //save draft
        addNewDraft();
      }
    }
  }

  private void deleteDraft() {
    draftsHelper.deleteBlogDraft(mDraftId);
  }

  private void updateDraft() {
    DraftModel draftModel = markDEditor.getDraft();
    String draftTitle = articleTitleEt.getText().toString().length() > 0 ? articleTitleEt.getText().toString() : "Untitled Draft";
    draftModel.setDraftTitle(draftTitle);
    draftsHelper.updateBlogDraft(draftModel);
  }

  private void addNewDraft() {
    DraftModel draftModel = markDEditor.getDraft();
    if(checkValidSaveOption(draftModel)) {
      showProgressDialog(true, "Saving draft...");
      String draftTitle = articleTitleEt.getText().toString().length() > 0 ? articleTitleEt.getText().toString() : "Untitled Draft";
      draftModel.setDraftTitle(draftTitle);
      mDraftId = draftModel.getDraftId();
      draftsHelper.insertDraft(draftModel);
    }
  }

  private boolean checkValidSaveOption(DraftModel draftModel) {
    if (draftModel.getItems().size() > 1) {
      return true;
    }
    if (draftModel.getItems().size() > 0) {
      DraftDataItemModel draftDataItemModel = draftModel.getItems().get(0);
      if (draftDataItemModel.getDownloadUrl() != null) {
        return true;
      }

      if (draftDataItemModel.getContent() != null) {
        if (draftDataItemModel.getContent().trim().length() > 0) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_IMAGE_SELECTOR:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          openGallery();
        } else {
          Toast.makeText(this, "Permission not granted to access images.", Toast.LENGTH_SHORT).show();
        }
        break;
    }
  }

  private void openGallery() {
    try {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ||
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
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

  private void handleImageResult(final Intent intent) {
    final Handler handler = new Handler();
    new Thread() {
      @Override
      public void run() {
        final String filePath = GoogleImageFilePathReader.getImageFilePath(CreateArticleActivity.this, intent);
        handler.post(new Runnable() {
          @Override
          public void run() {
            addImage(filePath);
          }
        });
      }
    }.start();
  }

  public void addImage(String filePath) {
    try {
      markDEditor.insertImage(filePath);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onInsertImageClicked() {
    openGallery();
  }

  @Override
  public void onInserLinkClicked() {
    showLinkInsertDialog();
  }

  private void showLinkInsertDialog() {
    LinkInsertDialog linkInsertDialog = new LinkInsertDialog(this);
    linkInsertDialog.setOnLinkInsertedListener(new LinkInsertDialog.OnLinkInsertedListener() {
      @Override
      public void onLinkAdded(String text, String link) {
        markDEditor.addLink(text, link);
      }
    });
    linkInsertDialog.show();
  }

  @Override
  public void onDraftInserted(boolean success) {
    showProgressDialog(false, "");
    if (success) {
      Toast.makeText(CreateArticleActivity.this, "Draft saved!", Toast.LENGTH_LONG).show();
    } else {
      Toast.makeText(CreateArticleActivity.this, "Failed to save draft!", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onSingleBlogDraftRead(DraftModel draft) {
    showProgressDialog(false, "");
    markDEditor.loadDraft(draft);
    String title = draft.getDraftTitle() != null ? draft.getDraftTitle() : "";
    articleTitleEt.setText(title);
  }

  @Override
  public void onAllDraftsRead(ArrayList<DraftListItemModel> drafts) {

  }

  @Override
  public void onDraftUpdated(boolean success) {
    Log.d("BlogDraft", "updated " + success);
  }

  @Override
  public void onDraftDeleted(boolean success) {

  }
}
