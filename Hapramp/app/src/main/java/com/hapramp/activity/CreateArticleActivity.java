package com.hapramp.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.controller.PostCreationController;
import com.hapramp.models.PostJobModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.PostCategoryView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateArticleActivity extends AppCompatActivity {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.nextButton)
    TextView nextButton;
    @BindView(R.id.postMedia)
    ImageView postMedia;
    @BindView(R.id.removeImageBtn)
    TextView removeImageBtn;
    @BindView(R.id.postMediaUploadProgress)
    ProgressBar postMediaUploadProgress;
    @BindView(R.id.postMediaContainer)
    FrameLayout postMediaContainer;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.characterLimit)
    TextView characterLimit;
    @BindView(R.id.textSizeBtn)
    TextView textSizeBtn;
    @BindView(R.id.quoteBtn)
    TextView quoteBtn;
    @BindView(R.id.bulletBtn)
    TextView bulletBtn;
    @BindView(R.id.linkBtn)
    TextView linkBtn;
    @BindView(R.id.bottom_options_container)
    RelativeLayout bottomOptionsContainer;
    @BindView(R.id.backBtnFromArticleMeta)
    TextView backBtnFromArticleMeta;
    @BindView(R.id.publishButton)
    TextView publishButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.category_caption)
    TextView categoryCaption;
    @BindView(R.id.articleCategoryView)
    PostCategoryView articleCategoryView;
    @BindView(R.id.tagsCaption)
    TextView tagsCaption;
    @BindView(R.id.tagsInputBox)
    EditText tagsInputBox;
    @BindView(R.id.skills_wrapper)
    RelativeLayout skillsWrapper;
    @BindView(R.id.metaView)
    RelativeLayout metaView;

    private Typeface typeface;
    private ArrayList<Integer> selectedSkills;
    private String mediaUri ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        ButterKnife.bind(this);
        init();
        attachListeners();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDraft();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDraft();
    }

    private void loadDraft() {
        content.setText(HaprampPreferenceManager.getInstance().getArticleDraft());
    }

    private void clearDraft() {
        HaprampPreferenceManager.getInstance().saveArticleDraft("");
    }

    private void saveDraft() {
        HaprampPreferenceManager.getInstance().saveArticleDraft(content.getText().toString());
    }

    private void init() {

        typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        textSizeBtn.setTypeface(typeface);
        quoteBtn.setTypeface(typeface);
        bulletBtn.setTypeface(typeface);
        backBtnFromArticleMeta.setTypeface(typeface);
        linkBtn.setTypeface(typeface);
        closeBtn.setTypeface(typeface);
        selectedSkills = new ArrayList<>();
        articleCategoryView.setCategoryItems(SkillsUtils.getSkillsSet());

    }

    private void attachListeners() {

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                characterLimit.setText(String.valueOf(s.length()));
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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                metaView.setVisibility(View.VISIBLE);
                // avoid touch input pass to underneath views
                metaView.setClickable(true);

            }
        });

        backBtnFromArticleMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide meta view
                metaView.setVisibility(View.GONE);
                metaView.setClickable(false);
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareAndPublishArticle();
            }
        });

    }

    private void prepareAndPublishArticle() {

        if (!validatePostContent())
            return;

        if (!isSkillSelected()) {
            toast("You Should Select Skills Regarding Your Post");
            return;
        }


        PostJobModel postJob = new PostJobModel(
                String.valueOf(SystemClock.currentThreadTimeMillis()),
                content.getText().toString(),
                mediaUri,
                Constants.CONTENT_TYPE_ARTICLE,
                articleCategoryView.getSelectedSkills(),
                1,
                PostJobModel.JOB_PENDING);

        PostCreationController.addJob(postJob);

        finish();

    }

    private boolean isSkillSelected() {
        return articleCategoryView.getSelectedSkills().size() > 0;
    }


    private boolean validatePostContent() {

        if (content.getText().toString().length() < 140) {
            Toast.makeText(this, "Your Content is Small. Minimum Article Size is 140", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


}
