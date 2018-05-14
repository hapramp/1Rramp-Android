package com.hapramp.views.post;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datamodels.response.FileUploadReponse;

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

    public PostImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PostImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
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
            }
        });
    }

    public void setImageSource(final Bitmap bitmap, String filePath) {
        if (bitmap == null)
            return;
        invalidateView();
        mainView.setVisibility(VISIBLE);
        image.setImageBitmap(bitmap);
        informationTv.setVisibility(VISIBLE);
        informationTv.setText("Processing...");
        startUploading(filePath);
    }

    private void invalidateView() {
        progressBar.setVisibility(VISIBLE);
        informationTv.setVisibility(GONE);
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

    private void startUploading(String filePath) {

        File file = new File(filePath);
        final RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);
        RetrofitServiceGenerator.getService().uploadFile(body).enqueue(new Callback<FileUploadReponse>() {
            @Override
            public void onResponse(Call<FileUploadReponse> call, Response<FileUploadReponse> response) {
                if (response.isSuccessful()) {
                    downloadUrl = response.body().getDownloadUrl();
                    progressBar.setVisibility(GONE);
                    informationTv.setText("Uploaded");
                    showAndhideActionContainer();
                } else {
                    informationTv.setText("Error");
                    downloadUrl = null;
                    progressBar.setVisibility(GONE);
                }
            }

            @Override
            public void onFailure(Call<FileUploadReponse> call, Throwable t) {
                informationTv.setText("Error");
                progressBar.setVisibility(GONE);
                downloadUrl = null;
            }
        });
    }

    public String getDownloadUrl() {
        return downloadUrl;
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
}
