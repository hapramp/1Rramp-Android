package com.hapramp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hapramp.R;
import com.hapramp.controller.PostCreationController;
import com.hapramp.logger.L;
import com.hapramp.models.PostJobModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FileUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.PostCategoryView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewPostCreationActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_SELECTOR = 101;
    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.postButton)
    TextView postButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.postMedia)
    ImageView postMedia;
    @BindView(R.id.audioIcon)
    TextView audioIcon;
    @BindView(R.id.audioFileName)
    TextView audioFileName;
    @BindView(R.id.removeImageBtn)
    TextView removeImageBtn;
    @BindView(R.id.postMediaUploadProgress)
    ProgressBar postMediaUploadProgress;
    @BindView(R.id.postMediaContainer)
    FrameLayout postMediaContainer;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.characterLimit)
    TextView characterLimit;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.category_caption)
    TextView categoryCaption;
    @BindView(R.id.postCategoryView)
    PostCategoryView postCategoryView;
    @BindView(R.id.skills_wrapper)
    RelativeLayout skillsWrapper;
    @BindView(R.id.photosBtn)
    TextView photosBtn;
    @BindView(R.id.audioBtn)
    TextView audioBtn;
    @BindView(R.id.videoBtn)
    TextView videoBtn;
    @BindView(R.id.bottom_options_container)
    RelativeLayout bottomOptionsContainer;

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
    private String mediaUri = "";

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

//        //load content
//        content.setText(HaprampPreferenceManager.getInstance().getPostDraft());
//        //load image
//        if (HaprampPreferenceManager.getInstance().getPostMediaDraft().length() > 0) {
//            loadImageIntoView(HaprampPreferenceManager.getInstance().getPostMediaDraft());
//        }

    }

    private void clearDraft() {
        // clear post content
        HaprampPreferenceManager.getInstance().savePostDraft("");
        // clear image draft
        HaprampPreferenceManager.getInstance().savePostMediaDraft("");

    }

    private void init() {
        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        photosBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        audioBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        videoBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        removeImageBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        selectedSkills = new ArrayList<>();
        postCategoryView.setCategoryItems(SkillsUtils.getSkillsSet());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void attachListener() {

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                characterLimit.setText(String.format(getResources().getString(R.string.post_limit), s.length()));
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

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendJobToController();
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

    private void sendJobToController() {

        if(postCategoryView.getSelectedSkills().size()==0){
            toast("Select Atleast One Category");
            return;
        }

        PostJobModel postJob = new PostJobModel(
                String.valueOf(SystemClock.currentThreadTimeMillis()),
                content.getText().toString(),
                mediaUri,
                Constants.CONTENT_TYPE_POST,
                postCategoryView.getSelectedSkills(),
                1,
                PostJobModel.JOB_PENDING);

        PostCreationController.addJob(postJob);
        finish();

    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private boolean isSkillsSelected() {
        return selectedSkills.size() > 0;
    }

    private void initProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Post Upload");
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading Your Post...");

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
            mediaUri = filePath;
            loadImageIntoView(filePath);
            rootFolder = "images";

        } else if (FileUtils.isVideo(mimeType)) {
            // video
            //todo: populate Media Uri
            audioIcon.setVisibility(View.GONE);
            audioFileName.setVisibility(View.GONE);
            postMedia.setVisibility(View.VISIBLE);
            rootFolder = "videos";

        } else {
            // audio
            postMedia.setVisibility(View.GONE);
            audioIcon.setVisibility(View.VISIBLE);
            audioFileName.setVisibility(View.VISIBLE);
            audioIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
            audioFileName.setText(new File(filePath).getName());
            //todo: populate Media Uri
            rootFolder = "audios";

        }
    }

    private void loadImageIntoView(String filePath) {

        audioIcon.setVisibility(View.GONE);
        audioFileName.setVisibility(View.GONE);
        postMedia.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(Uri.fromFile(new File(filePath)))
                .into(postMedia);

        //save as draft
        HaprampPreferenceManager.getInstance().savePostMediaDraft(filePath);

    }

    private void removeImageFromView() {

        isMediaSelected = false;
        mediaUri = "";
        postMediaContainer.setVisibility(View.GONE);
        // remove from draft
        HaprampPreferenceManager.getInstance().savePostMediaDraft("");

    }

}
