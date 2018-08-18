package com.hapramp.views.post;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datamodels.response.FileUploadReponse;
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

/**
 * Created by Ankit on 2/5/2018.
 */

public class PostImageView extends FrameLayout {
  @BindView(R.id.image)
  ImageView image;
  @BindView(R.id.informationTv)
  TextView informationTv;
  @BindView(R.id.actionContainer)
  RelativeLayout actionContainer;
  @BindView(R.id.progressBar)
  ProgressBar progressBar;
  @BindView(R.id.removeBtn)
  TextView removeBtn;
  private View mainView;
  private String downloadUrl;
  private ImageActionListener imageActionListener;
  private Context mContext;
  private final int MAX_RETRY_COUNT = 5;
  private int retryCount = 0;
  private boolean imageRemoved;


  public PostImageView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    mainView = LayoutInflater.from(context).inflate(R.layout.post_image_view, this);
    ButterKnife.bind(this, mainView);
    attachListeners();
    invalidateView();
  }

  private void attachListeners() {
    image.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (downloadUrl != null) {
          showAndhideActionContainer();
        }
      }
    });
    removeBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        scaleAndHideMainView();
        if (imageActionListener != null) {
          imageActionListener.onImageRemoved();
          imageRemoved = true;
        }
      }
    });
  }

  private void invalidateView() {
    progressBar.setVisibility(VISIBLE);
    informationTv.setVisibility(GONE);
  }

  private void showAndhideActionContainer() {
    actionContainer.setVisibility(VISIBLE);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        actionContainer.setVisibility(GONE);
      }
    }, 2000);
  }

  public void scaleAndHideMainView() {
    Animation anim = new ScaleAnimation(
      1f, 1f, // Start and end values for the X axis scaling
      1f, 0f, // Start and end values for the Y axis scaling
      Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
      Animation.RELATIVE_TO_SELF, 0f); // Pivot point of Y scaling
    anim.setFillAfter(false); // Needed to keep the result of the animation
    anim.setDuration(200);
    anim.setInterpolator(new DecelerateInterpolator(1f));
    mainView.startAnimation(anim);
    anim.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        mainView.setVisibility(GONE);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
      }
    });
  }

  public PostImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PostImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setImageSource(String filePath) {
    invalidateView();
    imageRemoved = false;
    mainView.setVisibility(VISIBLE);
    ImageHandler.load(mContext,image,filePath);
    informationTv.setVisibility(VISIBLE);
    informationTv.setText("Processing...");
    startUploading(filePath);
  }

  private void retryUpload(String filePath) {
    if (informationTv != null && !imageRemoved) {
      retryCount++;
      if (retryCount > MAX_RETRY_COUNT)
        return;
      progressBar.setVisibility(VISIBLE);
      informationTv.setText("Retrying image upload...");
      startUploading(filePath);
    }
  }

  private void startUploading(final String filePath) {
    try {
      final File file = new File(filePath);
      final RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
      MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);
      RetrofitServiceGenerator.getService().uploadFile(body).enqueue(new Callback<FileUploadReponse>() {
        @Override
        public void onResponse(Call<FileUploadReponse> call, Response<FileUploadReponse> response) {
          if (response.isSuccessful()) {
            downloadUrl = response.body().getDownloadUrl();
            progressBar.setVisibility(GONE);
            informationTv.setText("Uploaded");
            if (imageActionListener != null) {
              imageActionListener.onImageUploaded(downloadUrl);
            }
            showAndhideActionContainer();
          } else {
            Crashlytics.log(response.errorBody().toString());
            downloadUrl = null;
            progressBar.setVisibility(GONE);
            retryUpload(filePath);
            Log.d("ImageUploadDebug", "unsuccessfullResponse " + response.toString());
          }
        }

        @Override
        public void onFailure(Call<FileUploadReponse> call, Throwable t) {
          Crashlytics.logException(t);
          progressBar.setVisibility(GONE);
          downloadUrl = null;
          retryUpload(filePath);
          Log.d("ImageUploadDebug", "onFailure() " + t.toString());
        }
      });
    }
    catch (Exception e) {
      informationTv.setText("Failed to load image.");
      Crashlytics.logException(e);
      progressBar.setVisibility(GONE);
      downloadUrl = null;
    }
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setImageActionListener(ImageActionListener imageActionListener) {
    this.imageActionListener = imageActionListener;
  }

  public interface ImageActionListener {
    void onImageRemoved();
    void onImageUploaded(String downloadUrl);
  }
}
