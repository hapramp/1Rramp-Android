package com.hapramp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.URLS;
import com.hapramp.datamodels.FeaturedImageSelectionModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.steem.models.data.Content;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FilePathUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.views.editor.LinkInsertDialog;
import com.hapramp.views.hashtag.CustomHashTagInput;
import com.hapramp.views.post.PostCategoryView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xute.markdeditor.EditorControlBar;
import xute.markdeditor.MarkDEditor;
import xute.markdeditor.Styles.TextComponentStyle;

public class CreateArticleActivity extends AppCompatActivity implements SteemPostCreator.SteemPostCreatorCallback, EditorControlBar.EditorControlListener {

  private static final int REQUEST_IMAGE_SELECTOR = 119;
  @BindView(R.id.closeBtn)
  TextView closeBtn;
  @BindView(R.id.previewButton)
  TextView previewButton;
  @BindView(R.id.nextButton)
  TextView nextButton;
  @BindView(R.id.meta_toolbar_container)
  RelativeLayout meta_toolbarContainer;
  @BindView(R.id.backBtnFromArticleMeta)
  TextView backBtnFromArticleMeta;
  @BindView(R.id.publishButton)
  TextView publishButton;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.community_caption)
  TextView communityCaption;
  @BindView(R.id.articleCategoryView)
  PostCategoryView articleCategoryView;
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
  private ArrayList<FeaturedImageSelectionModel> insertedImages;
  private ProgressDialog progressDialog;
  private Dialog dialog;
  private String title;
  private ArrayList<String> tags;
  private Content postStructureModel;
  private String generated_permalink;
  private String permlink_with_username;
  private String body;
  private List<String> images;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_article);
    ButterKnife.bind(this);
    init();
    attachListeners();
    AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_BLOG_CREATION, null);
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_OPENS_ARTICLE_CREATE);
  }

  private void init() {
    closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    backBtnFromArticleMeta.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    progressDialog = new ProgressDialog(this);
    insertedImages = new ArrayList<>();
    articleCategoryView.initCategory();
    editorControlBar.setEditorControlListener(this);
    editorControlBar.setEditor(markDEditor);
    markDEditor.setHeading(TextComponentStyle.H1);
    markDEditor.setServerInfo(URLS.BASE_URL, HaprampPreferenceManager.getInstance().getUserToken());
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

  private void showMetaData(boolean show) {
    int vis = show ? View.VISIBLE : View.GONE;
    metaView.setVisibility(vis);
  }

  private void publishArticle() {
    title = articleTitleEt.getText().toString().trim();
    generated_permalink = PermlinkGenerator.getPermlink(title);
    tags = (ArrayList<String>) articleCategoryView.getSelectedTags();
    includeCustomTags(tags);
    body = markDEditor.getMarkdownContent();
    images = markDEditor.getImageList();
    if (validArticle()) {
      sendPostToSteemBlockChain();
    }
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
    steemPostCreator.createPost(body, title, images, tags, generated_permalink);
  }

  private void showConnectivityError() {
    Snackbar.make(toolbarContainer, "No Internet!  Article Saved To Draft ", Snackbar.LENGTH_SHORT).show();
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

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }

  private void includeCustomTags(ArrayList<String> tags) {
    tags.addAll(tagsInputBox.getHashTags());
  }

  private void close() {
    finish();
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
  }

  private void showPublishingProgressDialog(boolean show, String msg) {
    if (progressDialog != null) {
      if (show) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
      } else {
        progressDialog.hide();
      }
    }
  }

  @Override
  public void onBackPressed() {
    showExistAlert();
  }

  @Override
  public void onPostCreatedOnSteem() {
    toast("Published");
    closeEditor();
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
    toast(msg);
    showPublishingProgressDialog(false, "");
    Log.d(CreateArticleActivity.class.getSimpleName(), msg);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_SELECTOR) {
      if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
        Uri uri = data.getData();
        String filePath = FilePathUtils.getPath(this, uri);
        addImage(filePath);
      }
    }
  }

  public void addImage(String filePath) {
    markDEditor.insertImage(filePath);
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
        &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
      } else {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Intent intent = new Intent();
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
      }
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
}
