package com.hapramp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hapramp.Constants;
import com.hapramp.FontManager;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.logger.L;
import com.hapramp.models.requests.PostCreateBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewPostCreationActivity extends AppCompatActivity implements PostCreateCallback {

    private static final int REQUEST_IMAGE_SELECTOR = 101;
    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.postButton)
    TextView postButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.postMedia)
    ImageView postMedia;
    @BindView(R.id.content)
    EditText content;
    FirebaseStorage storage;
    @BindView(R.id.postMediaUploadProgress)
    ProgressBar postMediaUploadProgress;
    @BindView(R.id.postMediaContainer)
    FrameLayout postMediaContainer;
    @BindView(R.id.photosBtn)
    TextView photosBtn;
    @BindView(R.id.audioBtn)
    TextView audioBtn;
    @BindView(R.id.videoBtn)
    TextView videoBtn;
    @BindView(R.id.bottom_options_container)
    RelativeLayout bottomOptionsContainer;
    @BindView(R.id.removeImageBtn)
    TextView removeImageBtn;
    @BindView(R.id.articleBtn)
    TextView articleBtn;
    private Uri uploadedMediaUri;
    private String localMediaLocation = "";
    private ProgressDialog progressDialog;

    private final static int POST_TYPE_PHOTO = 12;
    private final static int POST_TYPE_AUDIO = 13;
    private final static int POST_TYPE_VIDEO = 14;
    int mediaType = -1;
    String galleryType = "";
    private String rootFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_creation);
        ButterKnife.bind(this);
        init();
        initProgressDialog();
        attachListener();

    }

    private void init() {

        storage = FirebaseStorage.getInstance();
        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        photosBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        audioBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        videoBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        removeImageBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

    }

    private void attachListener() {

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadPost();
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryType = "audio/*";
                rootFolder = "audio";
                mediaType = POST_TYPE_AUDIO;
                openGallery();
            }
        });

        photosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryType = "image/*";
                rootFolder = "photo";
                mediaType = POST_TYPE_PHOTO;
                openGallery();
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryType = "video/*";
                rootFolder = "video";
                mediaType = POST_TYPE_VIDEO;
                openGallery();
            }
        });

        removeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMediaContainer.setVisibility(View.GONE);
            }
        });

        articleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostCreationActivity.this,CreateArticleActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void showAddHapskills(){

    }

    private void uploadPost() {

        showProgressDialog(true);
        String mu = uploadedMediaUri != null ? uploadedMediaUri.toString() : "";
        List<Integer> skills = new ArrayList<>();
        PostCreateBody body = new PostCreateBody(content.getText().toString(),
                mu, Constants.CONTENT_TYPE_POST, skills, 1);
        DataServer.createPost(body, this);

    }

    private void initProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Post Upload");
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Your Post...");

    }

    private void showProgressDialog(boolean show) {
        if (progressDialog != null) {
            if (show) {
                progressDialog.show();
            } else {
                progressDialog.hide();
            }
        }
    }

    private void showMediaProgress(boolean show) {
        int v = show ? View.VISIBLE : View.GONE;
        if (postMediaUploadProgress != null) {
            postMediaUploadProgress.setVisibility(v);
        }
    }

    @Override
    public void onPostCreated() {
        showProgressDialog(false);
        L.D.m("PostCreate", "Post Created!");
    }

    @Override
    public void onPostCreateError() {
        showProgressDialog(false);
        L.D.m("PostCreate", "unable to create post");
    }

    private void uploadMedia(String uri) {

        showMediaProgress(true);
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child(rootFolder).child(System.currentTimeMillis() + "_" + uri.substring(uri.lastIndexOf('/')));
        InputStream stream = null;
        L.D.m("PostCreate", "Uploading from..." + uri);
        try {
            stream = new FileInputStream(new File(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showMediaProgress(false);
        }

        UploadTask uploadTask = mountainsRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                showMediaProgress(false);
                Toast.makeText(NewPostCreationActivity.this, "Failed To Upload Media", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                uploadedMediaUri = downloadUrl;
                showMediaProgress(false);
                L.D.m("PostCreate", " uploaded to : " + downloadUrl.toString());
            }
        });

    }

    private void openGallery() {

        try {
            if (ActivityCompat.checkSelfPermission(NewPostCreationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NewPostCreationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_SELECTOR);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType(galleryType);
                startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex < 0) {
                    L.D.m("PostCreate", "Photo Url error!");
                } else {

                    uploadMedia(cursor.getString(columnIndex));
                    postMediaContainer.setVisibility(View.VISIBLE);
                    if (mediaType == POST_TYPE_VIDEO) {
                        L.D.m("Gallery", "Video");
                        try {
                            postMedia.setImageBitmap(retriveVideoFrameFromVideo(localMediaLocation));
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }

                    } else if (mediaType == POST_TYPE_PHOTO) {
                        L.D.m("Gallery", "Photo");
                        postMedia.setImageBitmap(BitmapFactory.decodeFile(localMediaLocation));

                    } else {
                        L.D.m("Gallery", "Audio");
                        // thumail of audio
                        //postMedia.setImageBitmap(BitmapFactory.decodeFile(localMediaLocation));
                    }

                }
                cursor.close();
            }
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
