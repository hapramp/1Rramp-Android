package com.hapramp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.content)
    EditText content;
    FirebaseStorage storage;
    @BindView(R.id.postMediaUploadProgress)
    ProgressBar postMediaUploadProgress;
    private Uri mediaUri;
    private ProgressDialog progressDialog;

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
                createPost();
            }
        });

        postMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void createPost() {
        showProgressDialog(true);
        String mu = mediaUri!=null?mediaUri.toString():"";
        List<Integer> skills = new ArrayList<>();
        PostCreateBody body = new PostCreateBody(content.getText().toString(),
                mu,
                1, skills, 1);
        DataServer.createPost(body, this);

    }

    private void initProgressDialog(){

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Post Upload");
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Uploading Your Post...");

    }

    private void showProgressDialog(boolean show){
        if(progressDialog!=null){
            if(show){
                progressDialog.show();
            }else{
                progressDialog.hide();
            }
        }
    }

    private void showMediaProgress(boolean show){
        int v = show?View.VISIBLE:View.GONE;
        if(postMediaUploadProgress!=null){
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
        StorageReference mountainsRef = storageRef.child("postMedia").child(System.currentTimeMillis()+"_postMedia.jpg");
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
                Toast.makeText(NewPostCreationActivity.this,"Failed To Upload Media",Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mediaUri = downloadUrl;
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
            if(cursor!=null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                if (columnIndex < 0) {

                    L.D.m("PostCreate", "Photo Url error!");

                } else {

                    String uri = cursor.getString(columnIndex);
                    postMedia.setImageBitmap(BitmapFactory.decodeFile(uri));
                    Toast.makeText(this, "Uploading Image...", Toast.LENGTH_LONG).show();
                    uploadMedia(cursor.getString(columnIndex));

                }
                cursor.close();
            }
        }
    }

}
