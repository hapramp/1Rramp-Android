package com.hapramp.activity;

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

import com.hapramp.FontManager;
import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateArticleActivity extends AppCompatActivity {


    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.nextButton)
    TextView nextButton;
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

    }

}
