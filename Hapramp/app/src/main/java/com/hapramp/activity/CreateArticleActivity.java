package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.api.DataServer;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.models.requests.PostCreateBody;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.R;
import com.hapramp.utils.SkillsConverter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateArticleActivity extends AppCompatActivity implements PostCreateCallback {


    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.nextButton)
    TextView postButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.postMedia)
    ImageView postMedia;
    @BindView(R.id.removeImageBtn)
    TextView removeImageBtn;
    @BindView(R.id.postMediaUploadProgress)
    ProgressBar postMediaUploadProgress;
    @BindView(R.id.postMediaContainer)
    FrameLayout postMediaContainer;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.textSizeBtn)
    TextView textSizeBtn;
    @BindView(R.id.quoteBtn)
    TextView quoteBtn;
    @BindView(R.id.bulletBtn)
    TextView bulletBtn;
    @BindView(R.id.linkBtn)
    TextView linkBtn;
    @BindView(R.id.postViewBtn)
    TextView postViewBtn;
    @BindView(R.id.bottom_options_container)
    RelativeLayout bottomOptionsContainer;
    private Typeface typeface;
    private ArrayList<Integer> selectedSkills;

    private boolean isSkillSelected = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        ButterKnife.bind(this);
        init();
        attachListeners();

    }

    private void init() {

        typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        textSizeBtn.setTypeface(typeface);
        quoteBtn.setTypeface(typeface);
        bulletBtn.setTypeface(typeface);
        linkBtn.setTypeface(typeface);
        closeBtn.setTypeface(typeface);
        initProgressDialog();

    }

    private void attachListeners() {

        postViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateArticleActivity.this,NewPostCreationActivity.class);
                startActivity(intent);
                finish();
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
                prepareArticle();
            }
        });

    }

    private void prepareArticle(){

        if(!validatePostContent())
            return;

        if(!isSkillSelected){
            showAddSkillsDialog();
            return;
        }

        uploadArticle();
    }

    public void uploadArticle(){

        showProgressDialog(true);
        PostCreateBody body = new PostCreateBody(
                content.getText().toString(),
                "",
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


    @Override
    public void onPostCreated() {
        showProgressDialog(false);
        Toast.makeText(this,"Article Created!",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPostCreateError() {
        showProgressDialog(false);
        Toast.makeText(this,"Cannot Create Article!",Toast.LENGTH_SHORT).show();
        finish();
    }

}
