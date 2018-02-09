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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.CommentsAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CommentCreateCallback;
import com.hapramp.interfaces.CommentFetchCallback;
import com.hapramp.models.requests.CommentBody;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ViewItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 2/9/2018.
 */

public class CommentsActivity extends AppCompatActivity implements CommentFetchCallback, CommentCreateCallback {

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

        postId = getIntent().getExtras().getString(Constants.EXTRAA_KEY_POST_ID);
        initialCommentUrl = String.format(getResources().getString(R.string.commentUrl), Integer.valueOf(postId));
        commentsAdapter = new CommentsAdapter(this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.comment_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
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

    }

    private void fetchComments(String url) {

        DataServer.getComments(url, this);

    }


    private void postComment() {

        String cmnt = commentInputBox.getText().toString().trim();
        if (cmnt.length() > 2) {
            showProgress("Posting Your Comment..." + postId);
            DataServer.createComment(postId, new CommentBody(cmnt), this);
        } else {
            Toast.makeText(this, "Comment Too Short!!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCommentFetched(CommentsResponse response) {

        int len = response.results.size();
        if (len == 0) {
            noCommentsCaption.setVisibility(View.VISIBLE);
        } else {

            noCommentsCaption.setVisibility(View.GONE);

        }
        moreCommentsAt = response.next;

        commentsAdapter.addComment(response.results);

        if (moreCommentsAt.length() > 0) {
            fetchComments(moreCommentsAt);
        }

    }

    @Override
    public void onCommentFetchError() {

    }

    @Override
    public void onCommentCreated() {

        hideProgress();
        Toast.makeText(this, "Create Comment", Toast.LENGTH_SHORT).show();
        commentsAdapter.resetList();
        fetchComments(initialCommentUrl);
        // TODO: 2/9/2018 load only 1 comment not entire re-fetch

    }

    @Override
    public void onCommentCreateError() {

        hideProgress();
        finish();
        Toast.makeText(this, "Cannot Create Comment!", Toast.LENGTH_SHORT).show();

    }

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
