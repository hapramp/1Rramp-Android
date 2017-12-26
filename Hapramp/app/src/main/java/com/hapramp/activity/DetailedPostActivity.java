package com.hapramp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.CommentsAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CommentFetchCallback;
import com.hapramp.interfaces.OnPostDeleteCallback;
import com.hapramp.interfaces.UserFetchCallback;
import com.hapramp.interfaces.VoteDeleteCallback;
import com.hapramp.interfaces.VotePostCallback;
import com.hapramp.models.CommentModel;
import com.hapramp.models.UserResponse;
import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.models.response.PostResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ViewItemDecoration;
import com.hapramp.views.StarView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedPostActivity extends AppCompatActivity implements CommentFetchCallback, UserFetchCallback, OnPostDeleteCallback, VoteDeleteCallback, VotePostCallback {


    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.feed_owner_pic)
    ImageView feedOwnerPic;
    @BindView(R.id.feed_owner_title)
    TextView feedOwnerTitle;
    @BindView(R.id.feed_owner_subtitle)
    TextView feedOwnerSubtitle;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.featured_image_post)
    ImageView featuredImagePost;
    @BindView(R.id.post_snippet)
    TextView postSnippet;
    @BindView(R.id.shareWithFriendBtn)
    TextView shareWithFriendBtn;
    @BindView(R.id.commentBtn)
    TextView commentBtn;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.hapcoinBtn)
    TextView hapcoinBtn;
    @BindView(R.id.hapcoins_count)
    TextView hapcoinsCount;
    @BindView(R.id.post_overflow_icon)
    TextView postOverflowIcon;
    @BindView(R.id.starView)
    StarView starView;
    @BindView(R.id.post_meta_container)
    RelativeLayout postMetaContainer;
    @BindView(R.id.commentsCaption)
    TextView commentsCaption;
    @BindView(R.id.writeCommentUserAvatar)
    ImageView writeCommentUserAvatar;
    @BindView(R.id.commentBox)
    TextView commentBox;
    @BindView(R.id.writeCommentComment)
    RelativeLayout writeCommentComment;
    @BindView(R.id.commentsRecyclerView)
    RecyclerView commentsRecyclerView;
    private String mContent;
    private String mMediaUri;
    private String mUserName;
    private String postId;
    private CommentsAdapter commentsAdapter;
    private boolean isVoted;
    private int mVote;
    private String dpUrl;
    private String totalVoteSum;
    private List<CommentsResponse.Results> comments;
    private int ITEM_DECORATION_SPACE = 12;
    private ViewItemDecoration viewItemDecoration;
    private String totalUserVoted;
    private String currentCommentUrl;
    private String moreCommentsAt;
    private String mHapcoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_post);
        ButterKnife.bind(this);
        collectExtras();
        init();
        setTypefaces();
        bindValues();
        attachListener();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("TAG", "onNew Intent...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchComments();
    }

    private void init() {

        commentsAdapter = new CommentsAdapter(this);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.comment_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        commentsRecyclerView.addItemDecoration(viewItemDecoration);
        commentsRecyclerView.setAdapter(commentsAdapter);


    }

    private void collectExtras() {

        isVoted = getIntent().getExtras().getBoolean("isVoted");
        mVote = getIntent().getExtras().getInt("vote");
        mContent = getIntent().getExtras().getString("content");
        mMediaUri = getIntent().getExtras().getString("mediaUri");
        mUserName = getIntent().getExtras().getString("username");
        postId = getIntent().getExtras().getString("postId");
        dpUrl = getIntent().getExtras().getString("userDpUrl");
        totalVoteSum = getIntent().getExtras().getString("totalVoteSum");
        totalUserVoted = getIntent().getExtras().getString("totalUserVoted");
        currentCommentUrl = String.format(getResources().getString(R.string.commentUrl), Integer.valueOf(postId));
        mHapcoins = getIntent().getExtras().getString("hapcoins");

    }

    private void fetchComments() {

        DataServer.getComments(currentCommentUrl, this);
        // reset comments
        commentsAdapter.resetList();

    }

    private void attachListener() {

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commentBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailedPostActivity.this, CommentEditorActivity.class);
                i.putExtra("context", mContent);
                i.putExtra("postId", postId);
                startActivity(i);
            }
        });

        shareWithFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailedPostActivity.this, "Hey Dear! Excited about sharing this :). We are Working for You ", Toast.LENGTH_LONG).show();
            }
        });

        starView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starView.onStarIndicatorTapped();
            }
        });

    }

    private void setTypefaces() {
        Typeface t = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        closeBtn.setTypeface(t);
        overflowBtn.setTypeface(t);
        commentBtn.setTypeface(t);
        hapcoinBtn.setTypeface(t);
    }

    private void bindValues() {

        postSnippet.setText(mContent);
        feedOwnerTitle.setText(mUserName);
        if (mMediaUri.length() > 0) {
            ImageHandler.load(this, featuredImagePost, mMediaUri);
        } else {
            featuredImagePost.setVisibility(View.GONE);
        }

        ImageHandler.loadCircularImage(this, feedOwnerPic, dpUrl);
        feedOwnerSubtitle.setText(mUserName);
        hapcoinsCount.setText(mHapcoins);
        ImageHandler.load(this, writeCommentUserAvatar, HaprampPreferenceManager.getInstance().getUser().image_uri);

        // initialize the starview
        starView.setVoteState(
                new StarView.Vote(
                        isVoted,
                        Integer.valueOf(postId),
                        mVote,
                        Float.valueOf(totalVoteSum),
                        Float.valueOf(totalUserVoted)
                )).setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
            @Override
            public void onVoted(int postId, int vote) {
                vote(postId, vote);
            }

            @Override
            public void onVoteDeleted(int postId) {
                deleteVote(postId);
            }
        });


    }

    private void loadMoreComments() {

        if (moreCommentsAt.length() > 0)
            DataServer.getComments(moreCommentsAt, this);

    }


    private void showPopUp(View v, final int post_id, final int position) {

        final PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.post_item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showAlertDialogForDelete(post_id, position);
                return true;
            }
        });

        Log.d("POP", "show PopUp");

        popupMenu.show();

    }

    private void showAlertDialogForDelete(final int post_id, final int position) {

        new AlertDialog.Builder(this)
                .setTitle("Post Delete")
                .setMessage("Delete This Post")
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPostDelete(post_id, position);
                            }
                        })
                .setNegativeButton("Cancel",
                        null)
                .show();


    }

    private void requestPostDelete(int post_id, int pos) {
        DataServer.deletePost(String.valueOf(post_id), pos, this);
    }

    @Override
    public void onCommentFetched(CommentsResponse response) {

        prepareComments(response.results);
        commentCount.setText(String.valueOf(response.results.size()));
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

    @Override
    public void onPostDeleted(int position) {

    }

    @Override
    public void onPostDeleteFailed() {

    }

    private void deleteVote(int postId) {
        DataServer.deleteVote(postId, this);
    }

    private void vote(int postId, int vote) {
        DataServer.votePost(String.valueOf(postId), new VoteRequestBody((int) vote), this);
    }

    @Override
    public void onVoteDeleted(PostResponse.Results updatedPost) {
        //update mHapcoins
        hapcoinsCount.setText(String.valueOf(updatedPost.hapcoins));
    }

    @Override
    public void onVoteDeleteError() {

    }

    @Override
    public void onPostVoted(PostResponse.Results updatedPost) {
        //update mHapcoins
        hapcoinsCount.setText(String.valueOf(updatedPost.hapcoins));
    }

    @Override
    public void onPostVoteError() {

    }
}
