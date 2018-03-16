package com.hapramp.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.CommentsAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CommentCreateCallback;
import com.hapramp.interfaces.CommentFetchCallback;
import com.hapramp.models.requests.CommentBody;
import com.hapramp.models.response.CommentCreateResponse;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ViewItemDecoration;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 2/9/2018.
 */

public class CommentsActivity extends AppCompatActivity{

    @BindView(R.id.backBtn)
    TextView backBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.noCommentsCaption)
    TextView noCommentsCaption;
    @BindView(R.id.commentsRecyclerView)
    RecyclerView commentsRecyclerView;
    @BindView(R.id.commentCreaterAvatar)
    ImageView commentCreaterAvatar;
    @BindView(R.id.commentInputBox)
    EditText commentInputBox;
    @BindView(R.id.sendButton)
    TextView sendButton;
    @BindView(R.id.commentInputContainer)
    RelativeLayout commentInputContainer;
    @BindView(R.id.shadow)
    ImageView shadow;
    @BindView(R.id.commentLoadingProgressBar)
    ProgressBar commentLoadingProgressBar;
    private String initialCommentUrl;
    private String postId;
    private CommentsAdapter commentsAdapter;
    private ViewItemDecoration viewItemDecoration;
    private String moreCommentsAt;
    private ProgressDialog progressDialog;
    private Typeface typeface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_screen);
        ButterKnife.bind(this);
        init();
        fetchComments(initialCommentUrl);
        attachListeners();

    }

    private void init() {

        progressDialog = new ProgressDialog(this);
        typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        backBtn.setTypeface(typeface);
        sendButton.setTypeface(typeface);

        //load self image to created
        ImageHandler.loadCircularImage(this,commentCreaterAvatar,HaprampPreferenceManager.getInstance().getUser().getImage_uri());
        postId = getIntent().getExtras().getString(Constants.EXTRAA_KEY_POST_ID);
        initialCommentUrl = String.format(getResources().getString(R.string.commentUrl), Integer.valueOf(postId));
        commentsAdapter = new CommentsAdapter(this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.comment_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        viewItemDecoration.setWantTopOffset(false);
        commentsRecyclerView.addItemDecoration(viewItemDecoration);
        commentsRecyclerView.setAdapter(commentsAdapter);

    }

    private void attachListeners() {

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchComments(String url) {

        showCommentLoadingProgress();

    }

    private void postComment() {

        String cmnt = commentInputBox.getText().toString().trim();
        commentInputBox.setText("");
        if (cmnt.length() > 2) {
            showProgress("Posting Your Comment..." + postId);
           // DataServer.createComment(postId, new CommentBody(cmnt), this);
        } else {
            Toast.makeText(this, "Comment Too Short!!", Toast.LENGTH_LONG).show();
        }

    }

    private void showCommentLoadingProgress(){
        if(commentLoadingProgressBar!=null){
            commentLoadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideCommentLoadingProgress(){
        if(commentLoadingProgressBar!=null){
            commentLoadingProgressBar.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void onCommentFetched(CommentsResponse response) {
//
//        hideCommentLoadingProgress();
//
//        int len = response.results.size();
//        if (len == 0) {
//            noCommentsCaption.setVisibility(View.VISIBLE);
//        } else {
//
//            noCommentsCaption.setVisibility(View.GONE);
//
//        }
//        moreCommentsAt = response.next;
//        Collections.reverse(response.results);
//        commentsAdapter.addComments(response.results);
//
//        if (moreCommentsAt.length() > 0) {
//            fetchComments(moreCommentsAt);
//        }
//
//    }

//    @Override
//    public void onCommentCreated(CommentCreateResponse response) {
//
//        hideProgress();
//        Toast.makeText(this, "Create Comment", Toast.LENGTH_SHORT).show();
//        commentsAdapter.addComment(response);
//        commentsRecyclerView.getLayoutManager().scrollToPosition(0);
//        noCommentsCaption.setVisibility(View.GONE);
//
//    }
//
//    @Override
//    public void onCommentCreateError() {
//
//        hideProgress();
//        finish();
//        Toast.makeText(this, "Cannot Create Comment!", Toast.LENGTH_SHORT).show();
//
//    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    private void hideProgress() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

}
