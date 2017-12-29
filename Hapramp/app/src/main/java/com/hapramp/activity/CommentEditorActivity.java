package com.hapramp.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.CommentsAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CommentCreateCallback;
import com.hapramp.interfaces.CommentFetchCallback;
import com.hapramp.interfaces.UserFetchCallback;
import com.hapramp.models.CommentModel;
import com.hapramp.models.UserResponse;
import com.hapramp.models.requests.CommentBody;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ViewItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentEditorActivity extends AppCompatActivity implements CommentCreateCallback, CommentFetchCallback, UserFetchCallback {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.inContextOf)
    TextView inContextOf;
    @BindView(R.id.commentEt)
    EditText commentEt;
    @BindView(R.id.commentPublishBtn)
    TextView commentPublishBtn;
    @BindView(R.id.commentForCaption)
    TextView commentForCaption;
    @BindView(R.id.contentAuthor)
    TextView contentAuthor;
    @BindView(R.id.otherCommentCaption)
    TextView otherCommentCaption;
    @BindView(R.id.commentsRecyclerView)
    RecyclerView commentsRecyclerView;
    @BindView(R.id.contextContainer)
    RelativeLayout contextContainer;
    @BindView(R.id.toolbar_drop_shadow)
    FrameLayout toolbarDropShadow;
    @BindView(R.id.noCommentsCaption)
    TextView noCommentsCaption;
    @BindView(R.id.imageMedia)
    ImageView imageMedia;
    private Typeface typeface;
    private String postId = "";
    private ProgressDialog progressDialog;
    private String contextText;
    private String currentCommentUrl;
    private CommentsAdapter commentsAdapter;
    private ViewItemDecoration viewItemDecoration;
    private List<CommentsResponse.Results> comments;
    private String moreCommentsAt;
    private String author;
    private String mediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_editor);
        ButterKnife.bind(this);
        init();
        fetchComments();
    }

    private void init() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        progressDialog = new ProgressDialog(this);
        typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        closeBtn.setTypeface(typeface);
        contextText = getIntent().getExtras().getString("context");
        postId = getIntent().getExtras().getString("postId");
        author = getIntent().getExtras().getString("author");
        mediaUri = getIntent().getExtras().getString("media", "");

        inContextOf.setText(contextText);
        if (mediaUri.length()>0) {
            imageMedia.setVisibility(View.VISIBLE);
            ImageHandler.load(this,imageMedia,mediaUri);
        }
        contentAuthor.setText(String.format(getResources().getString(R.string.comment_author), author));
        commentPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });
        currentCommentUrl = String.format(getResources().getString(R.string.commentUrl), Integer.valueOf(postId));
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commentsAdapter = new CommentsAdapter(this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.comment_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        commentsRecyclerView.addItemDecoration(viewItemDecoration);
        commentsRecyclerView.setAdapter(commentsAdapter);


    }

    private void fetchComments() {

        DataServer.getComments(currentCommentUrl, this);
        // reset comments
        commentsAdapter.resetList();

    }


    private void postComment() {

        String cmnt = commentEt.getText().toString().trim();
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
            otherCommentCaption.setText(String.format(getResources().getString(R.string.no_other_comment_format)));
        } else {

            noCommentsCaption.setVisibility(View.GONE);

            if (len > 1) {
                otherCommentCaption.setText(String.format(getResources().getString(R.string.other_comments_format), len));
            } else {
                otherCommentCaption.setText(String.format(getResources().getString(R.string.other_comment_format), len));
            }

        }
        prepareComments(response.results);
        moreCommentsAt = response.next;

    }

    private void prepareComments(List<CommentsResponse.Results> comments) {

        // fetch all commetns
        // for each comment - request user and merge the infos
        // at the end set the result to recyclerView
        this.comments = comments;
        for (int i = 0; i < comments.size(); i++) {
            // request user
            DataServer.requestUser(i, comments.get(i).user.id, this);
        }
    }

    @Override
    public void onCommentFetchError() {

    }


    @Override
    public void onCommentCreated() {

        hideProgress();
        finish();
        Toast.makeText(this, "Create Comment", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onUserFetched(int commentPosition, UserResponse response) {

        // get the comments and response to create new CommentModel and add to recycler view

        commentsAdapter.addComment(new CommentModel(
                String.valueOf(comments.get(commentPosition).id),
                String.valueOf(comments.get(commentPosition).user.id),
                response.image_uri,
                response.full_name,
                comments.get(commentPosition).content,
                comments.get(commentPosition).created_at
        ));

    }

    @Override
    public void onUserFetchError() {
        Toast.makeText(this, "Error While Fetching Comments!", Toast.LENGTH_SHORT).show();
    }
}
