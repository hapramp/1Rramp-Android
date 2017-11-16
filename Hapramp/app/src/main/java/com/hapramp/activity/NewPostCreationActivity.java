package com.hapramp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.logger.L;
import com.hapramp.models.requests.PostCreateBody;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FileUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.SkillsConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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
    @BindView(R.id.audioIcon)
    TextView audioIcon;
    @BindView(R.id.audioFileName)
    TextView audioFileName;
    private Uri uploadedMediaUri;
    private ProgressDialog progressDialog;
    private boolean isSkillSelected = false;
    private boolean isMediaUploaded = false;
    private final static int POST_TYPE_PHOTO = 12;
    private final static int POST_TYPE_AUDIO = 13;
    private final static int POST_TYPE_VIDEO = 14;
    int mediaType = -1;
    String galleryType = "";
    private String rootFolder;
    private ArrayList<Integer> selectedSkills;
    private boolean isMediaSelected = false;


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
                preparePost();
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryType = "audio/*";
                openGallery();
            }
        });

        photosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryType = "image/*";
                openGallery();
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryType = "video/*";
                openGallery();
            }
        });

        removeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMediaSelected = false;
                postMediaContainer.setVisibility(View.GONE);
            }
        });

        articleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostCreationActivity.this, CreateArticleActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void preparePost() {

        if (!validatePostContent())
            return;

        if (!isSkillSelected) {
            isSkillSelected = true;
            showAddSkillsDialog();
            return;
        }

        uploadPost();

    }

    private void uploadPost() {

        if (isMediaSelected && !isMediaUploaded) {
            Toast.makeText(this, "Uploading Media, Try again Later!", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(true);

        String mu = uploadedMediaUri != null ? uploadedMediaUri.toString() : "";

        PostCreateBody body = new PostCreateBody(
                content.getText().toString(),
                mu,
                Constants.CONTENT_TYPE_POST,
                selectedSkills,
                1);

        DataServer.createPost(body, this);

    }

    private boolean validatePostContent() {

        if (content.getText().toString().length() < 10) {
            Toast.makeText(this, "Your Content is Small", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

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
                isMediaUploaded = true;
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
                    handleMediaSelection(cursor.getString(columnIndex));
                }
                cursor.close();
            }
        }
    }

    private void handleMediaSelection(String filePath) {

        isMediaSelected = true;
        // check type
        String mimeType = FileUtils.getMimeType(filePath);
        postMediaContainer.setVisibility(View.VISIBLE);
        if (FileUtils.isImage(mimeType)) {
            // image
            audioIcon.setVisibility(View.GONE);
            audioFileName.setVisibility(View.GONE);
            postMedia.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(Uri.fromFile(new File(filePath)))
                    .into(postMedia);
            rootFolder = "images";
            askForUpload("Image", filePath);

        } else if (FileUtils.isVideo(mimeType)) {
            // video
            audioIcon.setVisibility(View.GONE);
            audioFileName.setVisibility(View.GONE);
            postMedia.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(Uri.fromFile(new File(filePath)))
                    .into(postMedia);
            rootFolder = "videos";
            askForUpload("Video", filePath);

        } else {
            // audio
            postMedia.setVisibility(View.GONE);
            audioIcon.setVisibility(View.VISIBLE);
            audioFileName.setVisibility(View.VISIBLE);
            audioIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
            audioFileName.setText(new File(filePath).getName());

            rootFolder = "audios";
            askForUpload("Audio", filePath);

        }
    }

    private void askForUpload(String s, final String path) {

        new AlertDialog.Builder(this)
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadMedia(path);
                    }
                })
                .setNegativeButton("No", null)
                .setMessage("Upload Media :" + s)
                .setTitle("Media Upload")
                .show();

    }

    private void showAddSkillsDialog() {

        selectedSkills = new ArrayList<>();

        final String[] skills = {"Art", "Dance", "Music", "Literature", "Drama", "Photography"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select Tags");
        builder.setMultiChoiceItems(skills, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                int index = selectedSkills.indexOf(SkillsConverter.getSkillIdFromName(skills[which]));
                if (index == -1) {
                    // do not exists
                    if (isChecked) {
                        selectedSkills.add(SkillsConverter.getSkillIdFromName(skills[which]));
                    }
                } else {
                    // exists
                    if (!isChecked) {
                        selectedSkills.remove(index);
                    }
                }
            }
        });

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", null);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

}
