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
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FileUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageUrlGenerator;
import com.hapramp.utils.SkillsConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

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
    @BindView(R.id.audioIcon)
    TextView audioIcon;
    @BindView(R.id.audioFileName)
    TextView audioFileName;
    @BindView(R.id.skillsTagView)
    TextView skillsTagView;
    @BindView(R.id.addSkillBtn)
    TextView addSkillBtn;
    @BindView(R.id.skills_wrapper)
    RelativeLayout skillsWrapper;
    @BindView(R.id.characterLimit)
    TextView characterLimit;
    private String uploadedMediaUri;
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
    final String[] skills = {"Art", "Dance", "Music", "Literature", "Action", "Photography"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_creation);
        ButterKnife.bind(this);
        init();
        initProgressDialog();
        attachListener();

    }

    @Override
    protected void onPause() {
        super.onPause();
        HaprampPreferenceManager.getInstance().savePostDraft(content.getText().toString());
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadDraft();
    }

    private void loadDraft() {

        //load content
        content.setText(HaprampPreferenceManager.getInstance().getPostDraft());
        //load image
        if(HaprampPreferenceManager.getInstance().getPostMediaDraft().length()>0){
            loadImageIntoView(HaprampPreferenceManager.getInstance().getPostMediaDraft());
        }

    }

    private void clearDraft(){
        // clear post content
        HaprampPreferenceManager.getInstance().savePostDraft("");
        // clear image draft
        HaprampPreferenceManager.getInstance().savePostMediaDraft("");

    }

    private void init() {

        storage = FirebaseStorage.getInstance();
        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        photosBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        audioBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        videoBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        removeImageBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        selectedSkills = new ArrayList<>();

    }

    private void attachListener() {

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                characterLimit.setText(String.format(getResources().getString(R.string.post_limit),s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSkillsDialog();
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
                toast("Coming Soon! We Apreciate Your Patience :)");
                //todo: Implement audio Upload
//                galleryType = "audio/*";
//                openGallery();
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
                toast("Coming Soon!  We Apreciate Your Patience :)");
                //todo :  Implement video feature
//                galleryType = "video/*";
//                openGallery();
            }
        });

        removeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               removeImageFromView();
            }
        });

    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void preparePost() {

        if (!isSkillsSelected()) {
            toast("You Should Select Skills Regarding Your Post");
            return;
        }

        uploadPost();

    }

    private boolean isSkillsSelected() {
        return selectedSkills.size()>0;
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
        return true;
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
        // clear post draft
        clearDraft();
        L.D.m("PostCreate", "Post Created!");
        finish();

    }

    @Override
    public void onPostCreateError() {
        showProgressDialog(false);
        L.D.m("PostCreate", "unable to create post");
    }

    private void uploadMedia(String uri) {

        showMediaProgress(true);
        StorageReference storageRef = storage.getReference();
        // Store image in /<type-folder>/<user-id>/img.png
        StorageReference ref = storageRef.child(rootFolder).child(String.valueOf(HaprampPreferenceManager.getInstance().getUserId()));
        InputStream stream = null;
        L.D.m("PostCreate", "Uploading from..." + uri);
        try {
            stream = new FileInputStream(new File(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showMediaProgress(false);
        }

        UploadTask uploadTask = ref.putStream(stream);
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
                uploadedMediaUri = downloadUrl.toString();
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
            loadImageIntoView(filePath);
            rootFolder = "images";
            askForUpload("Image", filePath);

        } else if (FileUtils.isVideo(mimeType)) {
            // video
            audioIcon.setVisibility(View.GONE);
            audioFileName.setVisibility(View.GONE);
            postMedia.setVisibility(View.VISIBLE);
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

    private void loadImageIntoView(String filePath){

        audioIcon.setVisibility(View.GONE);
        audioFileName.setVisibility(View.GONE);
        postMedia.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(Uri.fromFile(new File(filePath)))
                .into(postMedia);

        //save as draft
        HaprampPreferenceManager.getInstance().savePostMediaDraft(filePath);

    }

    private void removeImageFromView(){

        isMediaSelected = false;
        postMediaContainer.setVisibility(View.GONE);
        // remove from draft
        HaprampPreferenceManager.getInstance().savePostMediaDraft("");

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

        boolean[] checked = getSelectedSkills();

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select Skills");

        builder.setMultiChoiceItems(skills,checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                int index = selectedSkills.indexOf(SkillsConverter.getSkillIdFromName(skills[which]));
                if (index == -1) {
                    // do not exists
                    if (isChecked) {
                        if(selectedSkills.size()>2){
                            toast("Maximum 3 Skills");
                        }else {
                            selectedSkills.add(SkillsConverter.getSkillIdFromName(skills[which]));
                        }
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
                showSelectedSkills();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", null);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    private boolean[] getSelectedSkills() {

        boolean[] selected = new boolean[6];

        for (int i=0;i<selected.length;i++){
            selected[i] = selectedSkills.contains(SkillsConverter.getSkillIdFromName(skills[i]));
            Log.d("POST",SkillsConverter.getSkillIdFromName(skills[i]) +" vs "+ Arrays.toString(selectedSkills.toArray()));
        }
        return selected;

    }

    private void showSelectedSkills(){
        StringBuilder builder = new StringBuilder();

        for (Integer skillId:selectedSkills){
            builder.append(" #").append(SkillsConverter.getSkillTitleFromId(skillId));
        }

        skillsTagView.setText(builder.toString());

    }

}
