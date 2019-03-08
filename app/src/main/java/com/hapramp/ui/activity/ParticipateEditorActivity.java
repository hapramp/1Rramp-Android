package com.hapramp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.api.URLS;
import com.hapramp.models.CompetitionEntryConfirmationBody;
import com.hapramp.models.CompetitionEntryResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.GoogleImageFilePathReader;
import com.hapramp.utils.ImageRotationHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.PostHashTagPreprocessor;
import com.hapramp.views.editor.LinkInsertDialog;
import com.hapramp.views.hashtag.CustomHashTagInput;
import com.hapramp.views.post.PostCommunityView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xute.markdeditor.EditorControlBar;
import xute.markdeditor.MarkDEditor;

import static xute.markdeditor.Styles.TextComponentStyle.NORMAL;

public class ParticipateEditorActivity extends AppCompatActivity implements EditorControlBar.EditorControlListener, SteemPostCreator.SteemPostCreatorCallback, ImageRotationHandler.ImageRotationOperationListner {

  public static final String EXTRA_COMPETITION_ID = "competition_id";
  public static final String EXTRA_COMPETITION_TITLE = "competition_title";
  public static final String EXTRA_COMPETITION_HASHTAG = "competition_hashtag";
  private static final int REQUEST_IMAGE_SELECTOR = 1032;
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.previewButton)
  TextView previewButton;
  @BindView(R.id.nextButton)
  TextView nextButton;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.article_title)
  EditText articleTitle;
  @BindView(R.id.mdEditor)
  MarkDEditor markDEditor;
  @BindView(R.id.controlBar)
  EditorControlBar editorControlBar;
  @BindView(R.id.backBtnFromArticleMeta)
  ImageView backBtnFromArticleMeta;
  @BindView(R.id.publishButton)
  TextView publishButton;
  @BindView(R.id.meta_toolbar_container)
  RelativeLayout metaToolbarContainer;
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
  @BindView(R.id.submission_info)
  TextView submissionInfo;
  @BindView(R.id.auto_hashtags_text)
  TextView autoHashtagsText;
  private ProgressDialog progressDialog;
  private String title;
  private String generated_permalink;
  private ArrayList<String> tags;
  private String body;
  private List<String> imageLinks;
  private String mCompetitionHashtag;
  private String mCompetitionId;
  private String mCompetitionTitle;
  private ImageRotationHandler imageRotationHandler;
  private Handler handler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_participate_in_competition_acitivity);
    ButterKnife.bind(this);
    collectCompetitionExtras();
    init();
    attachListeners();
    EventReporter.addEvent(AnalyticsParams.EVENT_OPENS_ARTICLE_CREATE);
  }

  private void collectCompetitionExtras() {
    Intent receiveIntent = getIntent();
    if (receiveIntent != null) {
      mCompetitionHashtag = receiveIntent.getStringExtra(EXTRA_COMPETITION_HASHTAG);
      mCompetitionId = receiveIntent.getStringExtra(EXTRA_COMPETITION_ID);
      mCompetitionTitle = receiveIntent.getStringExtra(EXTRA_COMPETITION_TITLE);
    }
  }

  private void init() {
    handler = new Handler();
    progressDialog = new ProgressDialog(this);
    imageRotationHandler = new ImageRotationHandler(this);
    imageRotationHandler.setImageRotationOperationListner(this);
    articleCategoryView.initCategory();
    setContestInfo();
    editorControlBar.setEditorControlListener(this);
    editorControlBar.setEditor(markDEditor);
    markDEditor.configureEditor(URLS.BASE_URL,
      HaprampPreferenceManager.getInstance().getUserToken(),
      false,
      "Body here...",
      NORMAL);
  }

  private void attachListeners() {
    submissionInfo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openCompetitionDetails();
      }
    });

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
        if (ConnectionUtils.isConnected(ParticipateEditorActivity.this)) {
          publishArticle();
        } else {
          showConnectivityError();
        }
      }
    });

    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showExistAlert();
      }
    });
  }

  private void setContestInfo() {
    String comp_title_part1 = "Your submission for: ";
    String comp_title_part2 = mCompetitionTitle;
    int spanStart = comp_title_part1.length();
    int spanEnd = comp_title_part2.length() + spanStart;

    StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#3F72AF"));

    Spannable compTitleSpan = new SpannableString(String.format("%s%s", comp_title_part1, comp_title_part2));
    //color span
    compTitleSpan.setSpan(foregroundColorSpan,
      spanStart,
      spanEnd,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    //bold span
    compTitleSpan.setSpan(bss, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    String hashtag_part1 = "#" + mCompetitionHashtag;
    String hashtag_part2 = " is auto tagged into your submission.(No need to write it again)";
    spanStart = 0;
    spanEnd = hashtag_part1.length();
    Spannable compHashtagSpan = new SpannableString(String.format("%s%s", hashtag_part1, hashtag_part2));
    //color span
    compHashtagSpan.setSpan(foregroundColorSpan,
      spanStart,
      spanEnd,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    //bold span
    compHashtagSpan.setSpan(bss, spanStart, spanEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

    submissionInfo.setText(compTitleSpan);
    autoHashtagsText.setText(compHashtagSpan);
  }

  private void openCompetitionDetails() {

  }

  private void showMetaData(boolean show) {
    int vis = show ? View.VISIBLE : View.GONE;
    metaView.setVisibility(vis);
  }

  private void publishArticle() {
    title = articleTitle.getText().toString().trim();
    generated_permalink = PermlinkGenerator.getPermlink(title);
    tags = (ArrayList<String>) articleCategoryView.getSelectedTags();
    includeCopetitionHashTag(tags);
    includeCustomTags(tags);
    tags = PostHashTagPreprocessor.processHashtags(tags);
    body = markDEditor.getMarkdownContent();
    imageLinks = getImageLinks();
    if (validArticle()) {
      sendPostToSteemBlockChain();
    }
  }

  private void showConnectivityError() {
    Snackbar.make(toolbarContainer, "No Internet!", Snackbar.LENGTH_SHORT).show();
  }

  private void showExistAlert() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this)
      .setTitle("Discard ?")
      .setMessage("You cannot recover discarded blogs.")
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          close();
        }
      })
      .setNegativeButton("No", null);
    builder.show();
  }

  private void includeCopetitionHashTag(ArrayList<String> tags) {
    tags.add(1, mCompetitionHashtag);
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
    showPublishingProgressDialog(true, "Publishing...");
    SteemPostCreator steemPostCreator = new SteemPostCreator();
    steemPostCreator.setSteemPostCreatorCallback(this);
    //add competition link
    String url = String.format(getString(R.string.contest_structure), mCompetitionTitle, mCompetitionId);
    body = body + url;
    //add footer
    body = body + Constants.COMPETITION_FOOTER;
    steemPostCreator.createPost(body, title, imageLinks, tags, generated_permalink);
  }

  private void close() {
    finish();
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }

  private void showPublishingProgressDialog(boolean show, String msg) {
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

  @Override
  public void onPostCreatedOnSteem() {
    HaprampPreferenceManager.getInstance().setLastPostCreatedAt(MomentsUtils.getCurrentTime());
    updateServerWithSubmission();
  }

  private void updateServerWithSubmission() {
    showPublishingProgressDialog(true, "Confirming submission...");
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    if (username.length() > 0) {
      String full_permlink = username + "/" + generated_permalink;
      CompetitionEntryConfirmationBody competitionEntryConfirmationBody = new CompetitionEntryConfirmationBody(full_permlink);
      RetrofitServiceGenerator.getService().updateServerAboutEntry(mCompetitionId, competitionEntryConfirmationBody).enqueue(new Callback<CompetitionEntryResponse>() {
        @Override
        public void onResponse(Call<CompetitionEntryResponse> call, Response<CompetitionEntryResponse> response) {
          showPublishingProgressDialog(false, "");
          if (response.isSuccessful()) {
            toast("Confirmed your entry!");
          } else {
            toast("Failed to confirm your entry!");
          }
          closeEditor();
        }

        @Override
        public void onFailure(Call<CompetitionEntryResponse> call, Throwable t) {
          showPublishingProgressDialog(false, "");
          toast("Failed to confirm your entry!");
          closeEditor();
        }
      });
    } else {
      showPublishingProgressDialog(false, "");
      toast("Failed to confirm your entry!");
    }
  }

  private void closeEditor() {
    showPublishingProgressDialog(false, "");
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
    System.out.println(msg);
    showPublishingProgressDialog(false, "");
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
  public void onBackPressed() {
    showExistAlert();
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleImageResult(final Intent intent) {
    new Thread() {
      @Override
      public void run() {
        final String filePath = GoogleImageFilePathReader.getImageFilePath(ParticipateEditorActivity.this, intent);
        imageRotationHandler.checkOrientationAndFixImage(filePath, 0);
      }
    }.start();
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
  public void onPointerCaptureChanged(boolean hasCapture) {

  }

  @Override
  public void onImageRotationFixed(final String filePath, boolean fileShouldBeDeleted, long uid) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        addImage(filePath);
      }
    });
  }

  public void addImage(String filePath) {
    markDEditor.insertImage(filePath);
  }
}
