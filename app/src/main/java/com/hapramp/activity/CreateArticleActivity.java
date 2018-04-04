package com.hapramp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.hapramp.R;
import com.hapramp.adapters.FeaturedImageAdapter;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.models.FeaturedImageSelectionModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.ContentTypes;
import com.hapramp.steem.PermlinkGenerator;
import com.hapramp.steem.PostConfirmationModel;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.steem.PreProcessingModel;
import com.hapramp.steem.ProcessedBodyResponse;
import com.hapramp.steem.SteemPostCreator;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FeaturedImageItemDecorator;
import com.hapramp.utils.FontManager;
import com.hapramp.views.editor.EditorView;
import com.hapramp.views.post.PostCategoryView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateArticleActivity extends AppCompatActivity implements EditorView.OnImageUploadListener, PostCreateCallback, SteemPostCreator.SteemPostCreatorCallback {

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
    private String title;
    private ArrayList<String> tags;
    private PostStructureModel postStructureModel;
    private String generated_permalink;

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
        //     saveDraft();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // restoreDraft();
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
        insertedImages = new ArrayList<>();
        featuredImageAdapter = new FeaturedImageAdapter(this);
        featuredImageSelectorRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredImageSelectorRV.setAdapter(featuredImageAdapter);
        featuredImageSelectorRV.addItemDecoration(new FeaturedImageItemDecorator());

    }

    private void showPublishingProgressDialog(boolean show,String msg) {

        if (progressDialog != null) {
            if (show) {
                progressDialog.setTitle("Article Upload");
                progressDialog.setMessage(msg);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
        } else {
            progressDialog.hide();
        }

    }

    private void showConnectivityError() {
        Snackbar.make(toolbarContainer, "No Internet!  Article Saved To Draft ", Snackbar.LENGTH_SHORT).show();
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
                if (ConnectionUtils.isConnected(CreateArticleActivity.this)) {
                    publishArticle();
                } else {
                    showConnectivityError();
                    saveDraft();
                }
            }
        });
    }

    private void publishArticle() {

        //prepare title
        title = "";
        //prepare tags
        tags = new ArrayList<>();
        //prepare post structure
        List<PostStructureModel.Data> datas = new ArrayList<>();
        datas.add(new PostStructureModel.Data(featuredImageAdapter.getSelectedFeaturedImageUrl(), ContentTypes.DataType.IMAGE));
        datas.add(new PostStructureModel.Data(editor.getContentAsHTML(), ContentTypes.DataType.TEXT));
        postStructureModel = new PostStructureModel(datas, ContentTypes.CONTENT_TYPE_POST);

    }

    //PUBLISHING SECTION
    private void sendPostToServerForProcessing(PostStructureModel content) {

        generated_permalink = PermlinkGenerator.getPermlink();
        PreProcessingModel preProcessingModel = new PreProcessingModel(generated_permalink, content);

        RetrofitServiceGenerator.getService().sendForPreProcessing(preProcessingModel).enqueue(new Callback<ProcessedBodyResponse>() {
            @Override
            public void onResponse(Call<ProcessedBodyResponse> call, Response<ProcessedBodyResponse> response) {
                if (response.isSuccessful()) {
                    // send to blockchain
                    sendPostToSteemBlockChain(response.body().getmBody());
                } else {
                    toast("Something went wrong while pre-processing...");
                    showPublishingProgressDialog(false, "");
                }
            }

            @Override
            public void onFailure(Call<ProcessedBodyResponse> call, Throwable t) {
                toast("Something went wrong with network(" + t.toString() + ")");
                showPublishingProgressDialog(false, "");
            }
        });
    }

    private void sendPostToSteemBlockChain(final String body) {

        showPublishingProgressDialog(true, "Adding to Blockchain...");
        SteemPostCreator steemPostCreator = new SteemPostCreator();
        steemPostCreator.setSteemPostCreatorCallback(this);
        steemPostCreator.createPost(body, title, tags, postStructureModel,generated_permalink);

    }

    private void confirmServerForPostCreation() {

        showPublishingProgressDialog(true, "Sending Confirmation to Server...");

        String full_permlink = HaprampPreferenceManager.getInstance().getCurrentSteemUsername() + "/" + generated_permalink;

        RetrofitServiceGenerator.getService()
                .sendPostCreationConfirmation(new PostConfirmationModel(full_permlink))
                .enqueue(new Callback<ProcessedBodyResponse>() {
                    @Override
                    public void onResponse(Call<ProcessedBodyResponse> call, Response<ProcessedBodyResponse> response) {
                        if (response.isSuccessful()) {
                            toast("Server Confirmed!");
                            showPublishingProgressDialog(false, "");
                        } else {
                            toast("Failed to Confirm Server!");
                            showPublishingProgressDialog(false, "");
                        }
                    }

                    @Override
                    public void onFailure(Call<ProcessedBodyResponse> call, Throwable t) {
                        toast("Something went wrong (" + t.toString() + ")");
                        showPublishingProgressDialog(false, "");
                    }
                });

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
        showPublishingProgressDialog(true,"");
        finish();
    }

    @Override
    public void onPostCreateError(String... jobId) {
        showPublishingProgressDialog(false,"");
        toast("Error while creating post");
    }

    @Override
    public void onPostCreatedOnSteem() {
        toast("Post Created on Blockchain");
        showPublishingProgressDialog(false,"");
        // send confirmation to server
        confirmServerForPostCreation();
    }

    @Override
    public void onPostCreationFailedOnSteem(String msg) {
        toast(msg);
        Log.d(CreateArticleActivity.class.getSimpleName(),msg);
    }

}
