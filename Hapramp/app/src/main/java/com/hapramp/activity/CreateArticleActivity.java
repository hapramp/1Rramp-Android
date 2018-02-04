package com.hapramp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorContent;
import com.hapramp.R;
import com.hapramp.adapters.FeaturedImageAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.logger.L;
import com.hapramp.models.FeaturedImageSelectionModel;
import com.hapramp.models.PostJobModel;
import com.hapramp.models.requests.PostCreateBody;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FeaturedImageItemDecorator;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.editor.EditorView;
import com.hapramp.views.editor.FeaturedImageView;
import com.hapramp.views.post.PostCategoryView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateArticleActivity extends AppCompatActivity implements EditorView.OnImageUploadListener, PostCreateCallback {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.draftButton)
    TextView draftButton;
    @BindView(R.id.nextButton)
    TextView nextButton;
    @BindView(R.id.meta_toolbar_container)
    RelativeLayout meta_toolbarContainer;
    @BindView(R.id.backBtnFromArticleMeta)
    TextView backBtnFromArticleMeta;
    @BindView(R.id.publishButton)
    TextView publishButton;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.featured_image_caption)
    TextView featuredImageCaption;
    @BindView(R.id.featured_image_selectorRV)
    RecyclerView featuredImageSelectorRV;
    @BindView(R.id.community_caption)
    TextView communityCaption;
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
    Editor editor;
    @BindView(R.id.editorView)
    EditorView editorView;
    private ArrayList<FeaturedImageSelectionModel> insertedImages;
    private ProgressDialog progressDialog;
    private Dialog dialog;

    FeaturedImageAdapter featuredImageAdapter;

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
         restoreDraft();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                editor.insertImage(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // editor.RestoreState();
        }

    }


    private void init() {

        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        backBtnFromArticleMeta.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        editor = editorView.getEditor();
        progressDialog = new ProgressDialog(this);
        editorView.setOnImageUploadListener(this);
        articleCategoryView.setCategoryItems(SkillsUtils.getSkillsSet());
        insertedImages = new ArrayList<>();
        featuredImageAdapter = new FeaturedImageAdapter(this);
        featuredImageSelectorRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredImageSelectorRV.setAdapter(featuredImageAdapter);
        featuredImageSelectorRV.addItemDecoration(new FeaturedImageItemDecorator());

    }

    private void showPublishingProgressDialog(boolean show) {

        if (progressDialog != null) {
            if (show) {
                progressDialog.setMessage("Publishing Your Article");
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
        } else {
            progressDialog.hide();
        }

    }

    private void showConnectivityError(){
        Snackbar.make(toolbarContainer,"No Internet!  Article Saved To Draft ",Snackbar.LENGTH_SHORT).show();
    }

    private void feedFeaturedImageData() {

        featuredImageAdapter.setImageSelectionModels(insertedImages);

    }

    private void attachListeners() {

        // draft button
        draftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDraft();
            }
        });

        //next Button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMetaData(true);
            }
        });

        backBtnFromArticleMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMetaData(false);
            }
        });

        // publish
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConnectionUtils.isConnected(CreateArticleActivity.this)) {
                    publishArticle();
                }else{
                    showConnectivityError();
                    saveDraft();
                }
            }
        });
    }

    private void publishArticle() {
        PostCreateBody postCreateBody = new PostCreateBody(editor.getContentAsHTML(),
                getMediaUri(),
                Constants.CONTENT_TYPE_ARTICLE,
                getSelectedSkills(),
                -1);

        showPublishingProgressDialog(true);
        //gather the data
        DataServer.createPost(postCreateBody, this);


    }

    private List<Integer> getSelectedSkills() {
        return articleCategoryView.getSelectedSkills();
    }

    private String getMediaUri() {
        String mediaUri = featuredImageAdapter.getSelectedFeaturedImageUrl();
        if (mediaUri == null) {
            return "";
        }
        return featuredImageAdapter.getSelectedFeaturedImageUrl();
    }

    private void showMetaData(boolean show) {

        int vis = show ? View.VISIBLE : View.GONE;
        metaView.setVisibility(vis);
//
//        if (show) {
//            feedFeaturedImageData();
//        }

    }

    private void saveDraft() {

        HaprampPreferenceManager
                .getInstance()
                .setArticleAsDraft(editor.getContentAsHTML());

        toast("Article Saved To Draft");

    }

    private void restoreDraft() {
        editor.render(HaprampPreferenceManager.getInstance().getArticleAsDraft());
    }

    private void toast(String s) {

        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onImageUploaded(String remotePath) {
        insertedImages.add(new FeaturedImageSelectionModel(false, remotePath));
        feedFeaturedImageData();
    }

    @Override
    public void onPostCreated(String... jobId) {
        showPublishingProgressDialog(true);
        finish();
    }

    @Override
    public void onPostCreateError(String... jobId) {
        showPublishingProgressDialog(false);
        toast("Error while creating post");
    }
}
