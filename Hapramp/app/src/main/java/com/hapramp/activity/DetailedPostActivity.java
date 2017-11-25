package com.hapramp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.R;
import com.hapramp.adapters.CommentsAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CommentFetchCallback;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.utils.FontManager;
import com.hapramp.views.RatingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedPostActivity extends AppCompatActivity implements CommentFetchCallback {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.feed_owner_pic)
    SimpleDraweeView feedOwnerPic;
    @BindView(R.id.feed_owner_title)
    TextView feedOwnerTitle;
    @BindView(R.id.feed_owner_subtitle)
    TextView feedOwnerSubtitle;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.featured_image_post)
    SimpleDraweeView featuredImagePost;
    @BindView(R.id.post_snippet)
    TextView postSnippet;
    @BindView(R.id.shareWithFriendBtn)
    TextView shareWithFriendBtn;
    @BindView(R.id.startIcon)
    TextView startIcon;
    @BindView(R.id.likeCount)
    TextView likeCount;
    @BindView(R.id.commentBtn)
    TextView commentBtn;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.hapcoinBtn)
    TextView hapcoinBtn;
    @BindView(R.id.hapcoins_count)
    TextView hapcoinsCount;
    @BindView(R.id.post_meta_container)
    RelativeLayout postMetaContainer;
    @BindView(R.id.commentsCaption)
    TextView commentsCaption;
    @BindView(R.id.writeCommentUserAvatar)
    SimpleDraweeView writeCommentUserAvatar;
    @BindView(R.id.commentBox)
    TextView commentBox;
    @BindView(R.id.writeCommentComment)
    RelativeLayout writeCommentComment;
    @BindView(R.id.commentsListView)
    ListView commentsListView;
    @BindView(R.id.starBtn)
    TextView starBtn;
    @BindView(R.id.ratingView)
    RatingView ratingView;
    private String mContent;
    private String mMediaUri;
    private String mUserName;
    private String postId;
    private CommentsAdapter commentsAdapter;
    private boolean isVoted;
    private int mVote;

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
        commentsListView.setAdapter(commentsAdapter);
        ratingView.setIntials(postId,isVoted,mVote);
    }

    private void collectExtras() {
        isVoted = getIntent().getExtras().getBoolean("isVoted");
        mVote = getIntent().getExtras().getInt("vote");
        mContent = getIntent().getExtras().getString("content");
        mMediaUri = getIntent().getExtras().getString("mediaUri");
        mUserName = getIntent().getExtras().getString("username");
        postId = getIntent().getExtras().getString("postId");
    }

    private void fetchComments() {
        DataServer.getComments(postId, this);
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

        featuredImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingView.addRating();
            }
        });

        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingView.addRating();
            }
        });

    }

    private void setTypefaces() {
        Typeface t = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        closeBtn.setTypeface(t);
        overflowBtn.setTypeface(t);
        startIcon.setTypeface(t);
        starBtn.setTypeface(t);
        commentBtn.setTypeface(t);
        hapcoinBtn.setTypeface(t);
    }

    private void bindValues() {
        postSnippet.setText(mContent);
        feedOwnerTitle.setText(mUserName);
        featuredImagePost.setImageURI(mMediaUri);
    }

    @Override
    public void onCommentFetched(CommentsResponse response) {
        commentsAdapter.setCommentsList(response.comments);
        setListViewHeightBasedOnChildren(commentsListView);
    }

    @Override
    public void onCommentFetchError() {

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
