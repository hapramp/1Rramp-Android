package com.hapramp.views.post;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hapramp.R;
import com.hapramp.datamodels.PostJobModel;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 2/5/2018.
 */

public class PostImageView extends FrameLayout {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.informationTv)
    TextView informationTv;
    @BindView(R.id.removeBtn)
    TextView removeBtn;
    @BindView(R.id.pauseResumeBtn)
    TextView pauseResumeBtn;
    @BindView(R.id.actionContainer)
    RelativeLayout actionContainer;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Context mContext;
    private Handler mHandler;
    private View mainView;
    private Uri downloadUrl;
    private boolean isUploadPaused;
    private UploadTask uploadTask;

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

        this.mContext = context;
        mainView = LayoutInflater.from(context).inflate(R.layout.post_image_view, this);
        ButterKnife.bind(this, mainView);
        mHandler = new Handler();
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
                downloadUrl = null;
            }
        });

        pauseResumeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUploadPaused) {
                    // resume
                    pauseResumeBtn.setText("Pause");
                    uploadTask.resume();
                    isUploadPaused = false;
                } else { // upload is in progress
                    pauseResumeBtn.setText("Resume");
                    uploadTask.pause();
                    isUploadPaused = true;
                }
            }
        });
    }

    public void setImageSource(final Bitmap bitmap) {

        invalidateView();

        mainView.setVisibility(VISIBLE);
        image.setImageBitmap(bitmap);
        // set status
        informationTv.setVisibility(VISIBLE);
        informationTv.setText("Processing...");

        new Thread() {
            @Override
            public void run() {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
                final byte[] byteArray = stream.toByteArray();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // check for visibility
                        if (mainView.getVisibility() == View.VISIBLE) {
                            startUploading(byteArray);
                        }

                    }
                });

            }

        }.start();

    }

    private void invalidateView() {
        //set Progress to 0
        progressBar.setVisibility(VISIBLE);
        progressBar.setProgress(0);
        pauseResumeBtn.setVisibility(GONE);
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

    private void startUploading(byte[] bytes) {

        Log.d("EditorView", "Uploading Media");

        //enable pause listener
        pauseResumeBtn.setVisibility(VISIBLE);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference ref =
                storageRef
                        .child("post_images")
                        .child(PostJobModel.getMediaLocation());

        uploadTask = ref.putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                downloadUrl = taskSnapshot.getDownloadUrl();
                // remove pause button
                pauseResumeBtn.setVisibility(GONE);
                // remove progress bar
                progressBar.setVisibility(GONE);
                informationTv.setText("Uploaded");
                showAndhideActionContainer();

            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                informationTv.setText("Failed !!!");
                //TODO: enable retry button
            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                informationTv.setText("Uploading " + progress + " %");
                progressBar.setProgress(progress);

            }
        });

        uploadTask.addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                informationTv.setText("Paused");
            }
        });
    }

    public String getDownloadUrl() {
        if (downloadUrl != null) {
            return downloadUrl.toString();
        } else {
            return null;
        }

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
