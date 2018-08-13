package com.hapramp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datamodels.response.FileUploadReponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.user.User;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageFilePathReader;
import com.hapramp.utils.ImageHandler;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity {

  private static final int REQUEST_IMAGE_SELECTOR = 101;
  private static final int USER_PROFILE_IMAGE_REQUEST = 102;
  private static final int USER_COVER_IMAGE_REQUEST = 103;
  @BindView(R.id.backButton)
  TextView backButton;
  @BindView(R.id.saveButton)
  TextView saveButton;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  @BindView(R.id.profile_wall_pic)
  ImageView profileCoverImageView;
  @BindView(R.id.cover_image_editBtn)
  ImageView coverImageEditBtn;
  @BindView(R.id.profile_pic)
  ImageView profileImageView;
  @BindView(R.id.dpEditBtn)
  TextView dpEditButton;
  @BindView(R.id.dpUploadingProgress)
  ProgressBar dpUploadingProgress;
  @BindView(R.id.profile_header_container)
  RelativeLayout profileHeaderContainer;
  @BindView(R.id.nameEt)
  EditText nameEt;
  @BindView(R.id.aboutMeEt)
  EditText aboutMeEt;
  @BindView(R.id.locationEt)
  EditText locationEt;
  @BindView(R.id.websiteEt)
  EditText websiteEt;
  @BindView(R.id.cover_image_upload_progress_bar)
  ProgressBar coverImageUploadProgressBar;
  private String userProfileImageDownloadUrl = "";
  private String userCoverImageDownloadUrl = "";
  private String fullname = "";
  private String about = "";
  private String location = "";
  private String website = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_edit);
    ButterKnife.bind(this);
    init();
  }

  private void init() {
    dpEditButton.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    backButton.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    String userJson = HaprampPreferenceManager.getInstance().getUserProfile(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    User user = new Gson().fromJson(userJson, User.class);
    bindUserData(user);
    attachListeners();
  }

  private void bindUserData(User user) {
    if (user.getFullname() != null) {
      fullname = user.getFullname();
      nameEt.setText(user.getFullname());
    }
    if (user.getAbout() != null) {
      about = user.getAbout();
      aboutMeEt.setText(user.getAbout());
    }
    if (user.getLocation() != null) {
      location = user.getLocation();
      locationEt.setText(user.getLocation());
    }
    if (user.getWebsite() != null) {
      website = user.getWebsite();
      websiteEt.setText(user.getWebsite());
    }
    if (user.getProfile_image() != null) {
      userProfileImageDownloadUrl = user.getProfile_image();
      ImageHandler.loadCircularImage(this, profileImageView, user.getProfile_image());
    }
    if (user.getCover_image() != null) {
      userCoverImageDownloadUrl = user.getCover_image();
      ImageHandler.load(this, profileCoverImageView, user.getCover_image());
    }
  }

  private void attachListeners() {
    backButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    dpEditButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openGallery(USER_PROFILE_IMAGE_REQUEST);
      }
    });

    coverImageEditBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openGallery(USER_COVER_IMAGE_REQUEST);
      }
    });
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String url = collectDataAndBuildUrl();
        Log.d("ProfileEdit", url);
        openBrowserForUpdate(url);
      }
    });
  }

  private void openGallery(int requestCode) {
    try {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
      } else {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String collectDataAndBuildUrl() {
    fullname = nameEt.getText().toString().trim();
    about = aboutMeEt.getText().toString().trim();
    location = locationEt.getText().toString().trim();
    website = websiteEt.getText().toString().trim();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("https://steemconnect.com/sign/profile-update?")
      .append("name=" + fullname)
      .append("&about=" + about)
      .append("&location=" + location)
      .append("&website=" + website)
      .append("&profile_image=" + userProfileImageDownloadUrl)
      .append("&cover_image=" + userCoverImageDownloadUrl);
    return stringBuilder.toString();
  }

  private void openBrowserForUpdate(String url) {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(browserIntent);
  }

  @Override
  public void onActivityResult(final int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
      if (requestCode == USER_PROFILE_IMAGE_REQUEST) {
        handleProfileImageUpload(data);
      } else if (requestCode == USER_COVER_IMAGE_REQUEST) {
        handleCoverImageUpload(data);
      }
    }
  }

  private void handleProfileImageUpload(final Intent intent) {
    final Handler handler = new Handler();
    new Thread() {
      @Override
      public void run() {
        final String imagePath = ImageFilePathReader.getImageFilePath(ProfileEditActivity.this, intent);
        handler.post(new Runnable() {
          @Override
          public void run() {
            ImageHandler.loadCircularImage(ProfileEditActivity.this, profileImageView, imagePath);
            showDpProgress();
            startUploading(imagePath, new Callback<FileUploadReponse>() {
              @Override
              public void onResponse(Call<FileUploadReponse> call, Response<FileUploadReponse> response) {
                if (response.isSuccessful()) {
                  userProfileImageDownloadUrl = response.body().getDownloadUrl();
                } else {
                  userProfileImageDownloadUrl = null;
                }
                hideDpProgress();
              }
              @Override
              public void onFailure(Call<FileUploadReponse> call, Throwable t) {
                userProfileImageDownloadUrl = null;
                hideDpProgress();
              }
            });
          }
        });
      }
    }.start();
  }

  private void handleCoverImageUpload(final Intent intent) {
    final Handler handler = new Handler();
    new Thread() {
      @Override
      public void run() {
        final String imagePath = ImageFilePathReader.getImageFilePath(ProfileEditActivity.this, intent);
        handler.post(new Runnable() {
          @Override
          public void run() {
            ImageHandler.load(ProfileEditActivity.this, profileCoverImageView, imagePath);
            showCoverImageProgress();
            startUploading(imagePath, new Callback<FileUploadReponse>() {
              @Override
              public void onResponse(Call<FileUploadReponse> call, Response<FileUploadReponse> response) {
                if (response.isSuccessful()) {
                  userCoverImageDownloadUrl = response.body().getDownloadUrl();
                } else {
                  userCoverImageDownloadUrl = null;
                }
                hideCoverImageProgress();
              }

              @Override
              public void onFailure(Call<FileUploadReponse> call, Throwable t) {
                userCoverImageDownloadUrl = null;
                hideCoverImageProgress();
              }
            });
          }
        });
      }
    }.start();
  }

  private void showDpProgress() {
    if (dpUploadingProgress != null) {
      dpUploadingProgress.setVisibility(View.VISIBLE);
    }
  }

  private void hideDpProgress() {
    if (dpUploadingProgress != null) {
      dpUploadingProgress.setVisibility(View.GONE);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
    switch (requestCode) {
      case USER_COVER_IMAGE_REQUEST:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          openGallery(requestCode);
        } else {
        }
        break;
      case USER_PROFILE_IMAGE_REQUEST:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          openGallery(requestCode);
        } else {
        }
        break;
    }
  }

  private void startUploading(String filePath, Callback<FileUploadReponse> fileUploadReponseCallback) {
    File file = new File(filePath);
    final RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
    MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);
    RetrofitServiceGenerator.getService().uploadFile(body).enqueue(fileUploadReponseCallback);
  }

  private void showCoverImageProgress() {
    if (coverImageUploadProgressBar != null) {
      coverImageUploadProgressBar.setVisibility(View.VISIBLE);
    }
  }

  private void hideCoverImageProgress() {
    if (coverImageUploadProgressBar != null) {
      coverImageUploadProgressBar.setVisibility(View.GONE);
    }
  }
}
