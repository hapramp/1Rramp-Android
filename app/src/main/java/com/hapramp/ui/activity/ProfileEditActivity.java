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
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.models.response.FileUploadReponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.User;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.GoogleImageFilePathReader;
import com.hapramp.utils.ImageCacheClearUtils;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ImageRotationHandler;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditActivity extends AppCompatActivity implements ImageRotationHandler.ImageRotationOperationListner {
  private static final int USER_PROFILE_IMAGE_REQUEST = 102;
  private static final int USER_COVER_IMAGE_REQUEST = 103;
  @BindView(R.id.backButton)
  ImageView backButton;
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
  private String newProfileImageDownloadUrl = "";
  private String newCoverImageDownloadUrl = "";
  private final int COVER_IMAGE_UID = 1201;
  private final int PROFILE_IMAGE_UID = 1203;
  private ImageRotationHandler imageRotationHandler;
  private Handler mHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_edit);
    ButterKnife.bind(this);
    init();
  }

  private void init() {
    imageRotationHandler = new ImageRotationHandler(this);
    mHandler = new Handler();
    imageRotationHandler.setImageRotationOperationListner(this);
    dpEditButton.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    String userJson = HaprampPreferenceManager.getInstance().getUserProfile(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    User user = new Gson().fromJson(userJson, User.class);
    bindUserData(user);
    attachListeners();
  }

  private void bindUserData(User user) {
    if (user.getFullname() != null) {
      fullname = user.getFullname();
      nameEt.setText(fullname);
    }
    if (user.getAbout() != null) {
      about = user.getAbout();
      aboutMeEt.setText(about);
    }
    if (user.getLocation() != null) {
      location = user.getLocation();
      locationEt.setText(location);
    }
    if (user.getWebsite() != null) {
      website = user.getWebsite();
      websiteEt.setText(website);
    }
    if (user.getProfile_image() != null) {
      userProfileImageDownloadUrl = user.getProfile_image();
      ImageHandler.loadCircularImage(this, profileImageView, userProfileImageDownloadUrl);
    }
    if (user.getCover_image() != null) {
      userCoverImageDownloadUrl = user.getCover_image();
      ImageHandler.load(this, profileCoverImageView, userCoverImageDownloadUrl);
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
        openBrowserForUpdate(url);
      }
    });
  }

  private void openGallery(int requestCode) {
    try {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
      } else {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String collectDataAndBuildUrl() {
    boolean firstPayloadAdded = false;
    String new_fullname = nameEt.getText().toString().trim();
    String new_about = aboutMeEt.getText().toString().trim();
    String new_location = locationEt.getText().toString().trim();
    String new_website = websiteEt.getText().toString().trim();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("https://steemconnect.com/sign/profile-update?");
    //detect name change
    if (!fullname.equals(new_fullname)) {
      stringBuilder
        .append("name=")
        .append(new_fullname);
      firstPayloadAdded = true;
    }
    if (!about.equals(new_about)) {
      if (firstPayloadAdded) {
        stringBuilder.append("&");
      }
      stringBuilder
        .append("about=")
        .append(new_about);
      firstPayloadAdded = true;
    }
    if (!location.equals(new_location)) {
      if (firstPayloadAdded) {
        stringBuilder.append("&");
      }
      stringBuilder
        .append("location=")
        .append(new_location);
      firstPayloadAdded = true;
    }
    if (!website.equals(new_website)) {
      if (firstPayloadAdded) {
        stringBuilder.append("&");
      }
      stringBuilder
        .append("website=")
        .append(new_website);
      firstPayloadAdded = true;
    }
    if (!userProfileImageDownloadUrl.equals(newProfileImageDownloadUrl)) {
      if (firstPayloadAdded) {
        stringBuilder.append("&");
      }
      stringBuilder
        .append("profile_image=")
        .append(newProfileImageDownloadUrl);
      firstPayloadAdded = true;
    }
    if (!userCoverImageDownloadUrl.equals(newCoverImageDownloadUrl)) {
      if (firstPayloadAdded) {
        stringBuilder.append("&");
      }
      stringBuilder
        .append("cover_image=")
        .append(newCoverImageDownloadUrl);
    }
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
        final String imagePath = GoogleImageFilePathReader.getImageFilePath(ProfileEditActivity.this, intent);
        handler.post(new Runnable() {
          @Override
          public void run() {
            ImageHandler.loadCircularImage(ProfileEditActivity.this, profileImageView, imagePath);
            showDpProgress();
          }
        });
        imageRotationHandler.checkOrientationAndFixImage(imagePath, PROFILE_IMAGE_UID);
      }
    }.start();
  }

  private void handleCoverImageUpload(final Intent intent) {
    final Handler handler = new Handler();
    new Thread() {
      @Override
      public void run() {
        final String imagePath = GoogleImageFilePathReader.getImageFilePath(ProfileEditActivity.this, intent);
        handler.post(new Runnable() {
          @Override
          public void run() {
            ImageHandler.loadFilePath(ProfileEditActivity.this, profileCoverImageView, imagePath);
            showCoverImageProgress();
          }
        });
        imageRotationHandler.checkOrientationAndFixImage(imagePath, COVER_IMAGE_UID);
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

  @Override
  public void onImageRotationFixed(final String filePath, final boolean fileShouldBeDeleted, final long uid) {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        startUploading(filePath, fileShouldBeDeleted, uid);
      }
    });
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

  private void startUploading(final String filePath, final boolean fileShouldBeDeleted, final long uid) {
    File file = new File(filePath);
    final RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
    MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);
    RetrofitServiceGenerator.getService().uploadFile(body).enqueue(new Callback<FileUploadReponse>() {
      @Override
      public void onResponse(Call<FileUploadReponse> call, Response<FileUploadReponse> response) {
        if (response.isSuccessful()) {
          if (uid == COVER_IMAGE_UID) {
            newCoverImageDownloadUrl = response.body().getDownloadUrl();
            toast("Cover image uploaded successfully!");
            hideCoverImageProgress();
          } else if (uid == PROFILE_IMAGE_UID) {
            toast("Profile image uploaded successfully!");
            newProfileImageDownloadUrl = response.body().getDownloadUrl();
            hideDpProgress();
          }
        } else {
          if (uid == COVER_IMAGE_UID) {
            newCoverImageDownloadUrl = null;
            toast("Failed to upload cover image!");
            hideCoverImageProgress();
          } else {
            newProfileImageDownloadUrl = null;
            toast("Failed to upload profile image!");
            hideDpProgress();
          }
        }
        if (fileShouldBeDeleted) {
          ImageCacheClearUtils.deleteImage(filePath);
        }
      }

      @Override
      public void onFailure(Call<FileUploadReponse> call, Throwable t) {
        if (uid == COVER_IMAGE_UID) {
          newCoverImageDownloadUrl = null;
          hideCoverImageProgress();
        } else {
          newProfileImageDownloadUrl = null;
          hideDpProgress();
        }
      }
    });
  }

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }
}
