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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datamodels.response.ConfirmationResponse;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.FeedDataConstants;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.PostConfirmationModel;
import com.hapramp.steem.PreProcessingModel;
import com.hapramp.steem.ProcessedBodyResponse;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.steem.models.data.Content;
import com.hapramp.steem.models.data.FeedDataItemModel;
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

import static com.hapramp.views.editor.YoutubeInsertButtonView.YOUTUBE_RESULT_REQUEST;

public class CreatePostActivity extends AppCompatActivity implements PostCreateCallback, SteemPostCreator.SteemPostCreatorCallback {

  public static final String CLOSE_DISTRACTION_FREE_BUTTON_TEXT = "NORMAL MODE";
  public static final String OPEN_DISTRATION_FREE_BUTTON_TEXT = "DISTRACTION FREE MODE";
  private static final int REQUEST_IMAGE_SELECTOR = 101;
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
  @BindView(R.id.distraction_mode_btn)
  TextView distractionModeBtn;
  @BindView(R.id.bottom_options_container)
  RelativeLayout bottomOptionsContainer;
  @BindView(R.id.bottom_postButton)
  TextView bottomPostButton;
  private ProgressDialog progressDialog;
  private Content postStructureModel;
  private List<String> tags;
  private String title;
  private String generated_permalink;
  private SteemPostCreator steemPostCreator;
  private String permlink_with_username;
  private boolean mDistractionFreeMode = false;

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
    progressDialog.setTitle("Post Upload");
    progressDialog.setIndeterminate(true);
    progressDialog.setCancelable(false);
    progressDialog.setMessage("Uploading Your Post...");
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

    distractionModeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setDistractionFreeMode(!mDistractionFreeMode);
      }
    });
    bottomPostButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        publishPost();
      }
    });
  }

  private void close() {
    finish();
    overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit);
  }

  private void publishPost() {
    // check connection
    if (!ConnectionUtils.isConnected(this)) {
      showConnectivityError();
      return;
    }

    if (validPost()) {
      showPublishingProgressDialog(true, "Preparing Your Post...");
      preparePost();
      showPublishingProgressDialog(true, "Pre-processing...");
      sendPostToServerForProcessing(postStructureModel);
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
/*

		@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
		private void showCommunitySelector() {
				int width = communitySelectorContainer.getWidth();
				int height = communitySelectorContainer.getHeight();
				int startRadius = 0;
				communitySelectorContainer.setVisibility(View.VISIBLE);
				int endRadius = (int) Math.hypot(width, height);
				Animator anim = ViewAnimationUtils.createCircularReveal(communitySelectorContainer, width - 48, height, startRadius, endRadius);
				anim.setDuration(400);
				anim.setInterpolator(new DecelerateInterpolator());
				anim.start();
		}
*/

  private void openVideoSelector() {
    Intent youtubeIntent = new Intent(this, YoutubeVideoSelectorActivity.class);
    startActivityForResult(youtubeIntent, YOUTUBE_RESULT_REQUEST);
  }

  private void setDistractionFreeMode(boolean turnedOn) {
    mDistractionFreeMode = turnedOn;
    toolbarContainer.setVisibility(View.GONE);
    distractionModeBtn.setText(turnedOn ? CLOSE_DISTRACTION_FREE_BUTTON_TEXT : OPEN_DISTRATION_FREE_BUTTON_TEXT);
    toolbarContainer.setVisibility(turnedOn ? View.GONE : View.VISIBLE);
    bottomPostButton.setVisibility(turnedOn ? View.VISIBLE : View.GONE);
  }

  private void showConnectivityError() {
    Snackbar.make(toolbarContainer, "No Internet!", Snackbar.LENGTH_SHORT).show();
  }

  private boolean validPost() {
    //check image/video
    if (postCreateComponent.isMediaSelected()) {
      if (postCreateComponent.isMediaUploaded()) {
        if (postCreateComponent.isContentEnough()) {
          if (postCreateComponent.getSelectedCommunityTags().size() > 1) { //default: hapramp is added at community.
            return true;
          } else {
            toast("Select atleast 1 community!");
          }
        } else {
          toast("Write someting more...");
        }
      } else {
        toast("Media is being uploaded!");
      }
    } else {
      toast("You must select image/video");
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
        progressDialog.hide();
      }
    } else {
      progressDialog.hide();
    }
  }

  private void preparePost() {
    generated_permalink = PermlinkGenerator.getPermlink();
    permlink_with_username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername() + "/" + generated_permalink;
    title = "";
    tags = postCreateComponent.getSelectedCommunityTags();
    List<FeedDataItemModel> datas = postCreateComponent.getDataList();
    postStructureModel = new Content(datas, FeedDataConstants.FEED_TYPE_POST);
  }

  //PUBLISHING SECTION
  private void sendPostToServerForProcessing(Content content) {
    PreProcessingModel preProcessingModel = new PreProcessingModel(permlink_with_username, new Gson().toJson(content));
    RetrofitServiceGenerator.getService().sendForPreProcessing(preProcessingModel)
      .enqueue(new Callback<ProcessedBodyResponse>() {
        @Override
        public void onResponse(Call<ProcessedBodyResponse> call, Response<ProcessedBodyResponse> response) {
          if (response.isSuccessful()) {
            // send to blockchain
            sendPostToSteemBlockChain(response.body().getmBody());
          } else {
            toast("Something went wrong while pre-processing...");
            showPublishingProgressDialog(false, "");
          }
        }

        @Override
        public void onFailure(Call<ProcessedBodyResponse> call, Throwable t) {
          toast("Something went wrong with network(" + t.toString() + ")");
          showPublishingProgressDialog(false, "");
        }
      });
  }

  private void toast(String s) {
    Toast.makeText(this, s, Toast.LENGTH_LONG).show();
  }

  private void sendPostToSteemBlockChain(final String body) {
    showPublishingProgressDialog(true, "Adding to Blockchain... Please wait");
    steemPostCreator.createPost(body, title, tags, postStructureModel, generated_permalink);
  }

  private void shakeCommunityButton() {
    final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        animShake.cancel();
      }
    }, 500);
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
      .setTitle("Close")
      .setMessage("Do you want to Close Post Creation ?")
      .setPositiveButton("Close", new DialogInterface.OnClickListener() {
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
    // send confirmation to server
    confirmServerForPostCreation();
  }

  //==================
  // STEEM POST CREATOR CALLBACK
  //==================

  private void confirmServerForPostCreation() {
    showPublishingProgressDialog(true, "Sending Confirmation to Server...");
    RetrofitServiceGenerator.getService()
      .sendPostCreationConfirmation(new PostConfirmationModel(permlink_with_username))
      .enqueue(new Callback<ConfirmationResponse>() {
        @Override
        public void onResponse(Call<ConfirmationResponse> call, Response<ConfirmationResponse> response) {
          if (response.isSuccessful()) {
            serverConfirmed();
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

  private void serverConfirmed() {
    toast("Your post is live now.");
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
