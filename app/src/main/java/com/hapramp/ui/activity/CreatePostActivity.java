package com.hapramp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datamodels.response.ConfirmationResponse;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.PostConfirmationModel;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FilePathUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.views.post.PostCreateComponent;
import com.hapramp.youtube.YoutubeVideoSelectorActivity;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity implements PostCreateCallback, SteemPostCreator.SteemPostCreatorCallback {
  private static final int REQUEST_IMAGE_SELECTOR = 101;
  private static final int YOUTUBE_RESULT_REQUEST = 107;
  @BindView(R.id.closeBtn)
  TextView closeBtn;
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
  private String permlink_with_username;
  private boolean mDistractionFreeMode = false;
  private List<String> images;
  private String body;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_post_creation);
    ButterKnife.bind(this);
    init();
    initProgressDialog();
    attachListener();
    AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_POST_CREATION, null);
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_OPENS_POST_CREATE);
  }

  private void init() {
    closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    steemPostCreator = new SteemPostCreator();
    steemPostCreator.setSteemPostCreatorCallback(this);
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
        close();
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
      public void onYoutubeVideoOptionSelected() {
        openVideoSelector();
      }
    });

  }

  private void close() {
    finish();
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
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
    try {
      if (ActivityCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
      } else {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE_SELECTOR);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void openVideoSelector() {
    Intent youtubeIntent = new Intent(this, YoutubeVideoSelectorActivity.class);
    startActivityForResult(youtubeIntent, YOUTUBE_RESULT_REQUEST);
  }

  private void showConnectivityError() {
    Snackbar.make(toolbarContainer, "No Internet!", Snackbar.LENGTH_SHORT).show();
  }

  private boolean validPost() {
    if (postCreateComponent.isMediaSelected()) {
      if (postCreateComponent.isMediaUploaded()) {
        return true;
      } else {
        toast("Please wait while we upload your image.");
        return false;
      }
    } else {
      if (postCreateComponent.isContentEnough()) {
        if (postCreateComponent.getSelectedCommunityTags().size() > 1) { //default: hapramp is added at community.
          return true;
        } else {
          toast("Select atleast 1 community!");
        }
      } else {
        toast("Write someting more...");
      }
    }
    return false;
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

  private void preparePost() {
    permlink_with_username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername() + "/" + generated_permalink;
    generated_permalink = PermlinkGenerator.getPermlink();
    body = postCreateComponent.getBody();
    tags = postCreateComponent.getSelectedCommunityTags();
    images = postCreateComponent.getImageList();
  }

  private void sendPostToSteemBlockChain() {
    showPublishingProgressDialog(true, "Publishing...");
    steemPostCreator.createPost(body, "", images, tags, generated_permalink);
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_SELECTOR && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
      try {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
        String realPath = FilePathUtils.getPath(this, data.getData());
        postCreateComponent.setImageResource(bitmap, realPath);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (requestCode == YOUTUBE_RESULT_REQUEST && resultCode == Activity.RESULT_OK) {
      String videoId = data.getStringExtra(YoutubeVideoSelectorActivity.EXTRA_VIDEO_KEY);
      insertYoutube(videoId);
    }
  }

  @Override
  public void onBackPressed() {
    showExistAlert();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_IMAGE_SELECTOR:
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          openGallery();
        } else {
          //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
          toast("Permission not granted to access images.");
        }
        break;
    }
  }

  private void showExistAlert() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this)
      .setTitle("Discard?")
      .setMessage("You cannot recover discarded posts.")
      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          close();
        }
      })
      .setNegativeButton("No", null);
    builder.show();
  }

  private void insertYoutube(String videoId) {
    postCreateComponent.setYoutubeThumbnail(videoId);
  }

  @Override
  public void onPostCreated(String... jobId) {
    showPublishingProgressDialog(false, "");
    close();
  }

  @Override
  public void onPostCreateError(String... jobId) {
    showPublishingProgressDialog(false, "");
    toast("Failed to create post");
  }

  @Override
  public void onPostCreatedOnSteem() {
    toast("Your post will take few seconds to appear");
    showPublishingProgressDialog(false, "");
    postCreated();
  }

  private void confirmServerForPostCreation() {
    showPublishingProgressDialog(true, "Sending Confirmation to Server...");
    RetrofitServiceGenerator.getService()
      .sendPostCreationConfirmation(new PostConfirmationModel(permlink_with_username))
      .enqueue(new Callback<ConfirmationResponse>() {
        @Override
        public void onResponse(Call<ConfirmationResponse> call, Response<ConfirmationResponse> response) {
          if (response.isSuccessful()) {
            postCreated();
          } else {
            toast("Failed to Confirm Server!");
            showPublishingProgressDialog(false, "");
          }
        }

        @Override
        public void onFailure(Call<ConfirmationResponse> call, Throwable t) {
          toast("Something went wrong (" + t.toString() + ")");
          showPublishingProgressDialog(false, "");
        }
      });
  }

  private void postCreated() {
    showPublishingProgressDialog(false, "");
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_CREATE_POST);
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
    Log.d(CreatePostActivity.class.getSimpleName(), msg);
  }
}
