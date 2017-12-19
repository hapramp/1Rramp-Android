package com.hapramp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.adapters.CommentsAdapter;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CommentFetchCallback;
import com.hapramp.interfaces.UserFetchCallback;
import com.hapramp.models.CommentModel;
import com.hapramp.models.UserResponse;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ViewItemDecoration;
import com.hapramp.views.RatingView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedPostActivity extends AppCompatActivity implements CommentFetchCallback, UserFetchCallback {

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
    @BindView(R.id.starIcon)
    TextView startIcon;
    @BindView(R.id.voteCount)
    TextView voteCount;
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
    ImageView writeCommentUserAvatar;
    @BindView(R.id.commentBox)
    TextView commentBox;
    @BindView(R.id.writeCommentComment)
    RelativeLayout writeCommentComment;
    @BindView(R.id.commentsRecyclerView)
    RecyclerView commentsRecyclerView;
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
    private String dpUrl;
    private String totalVotes;
    private List<CommentsResponse.Comments> comments;
    private int ITEM_DECORATION_SPACE = 12;
    private ViewItemDecoration viewItemDecoration;

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
        Drawable drawable = ContextCompat.getDrawable(this,R.drawable.post_item_divider_view);
        viewItemDecoration = new ViewItemDecoration(drawable);
        commentsRecyclerView.addItemDecoration(viewItemDecoration);
        commentsRecyclerView.setAdapter(commentsAdapter);
        ratingView.setIntials(postId, isVoted, mVote);

    }

    private void collectExtras() {

        isVoted = getIntent().getExtras().getBoolean("isVoted");
        mVote = getIntent().getExtras().getInt("vote");
        mContent = getIntent().getExtras().getString("content");
        mMediaUri = getIntent().getExtras().getString("mediaUri");
        mUserName = getIntent().getExtras().getString("username");
        postId = getIntent().getExtras().getString("postId");
        dpUrl = getIntent().getExtras().getString("userDpUrl");
        totalVotes = getIntent().getExtras().getString("totalVotes");
        //TODO: total income

    }


    private void fetchComments() {

        DataServer.getComments(postId, this);
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

        shareWithFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailedPostActivity.this, "Hey Dear! Excited about sharing this :). We are Working for You ", Toast.LENGTH_LONG).show();
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
       // featuredImagePost.setImageURI(mMediaUri);
        ImageHandler.load(this,featuredImagePost,mMediaUri);
//        feedOwnerPic.setImageURI(dpUrl);
        ImageHandler.load(this,feedOwnerPic,dpUrl);
        feedOwnerSubtitle.setText(mUserName);
        voteCount.setText(totalVotes);
        //todo: add the value of post here
        hapcoinsCount.setText("0.0");
//        writeCommentUserAvatar.setImageURI(HaprampPreferenceManager.getInstance().getUser().image_uri);
        ImageHandler.load(this,writeCommentUserAvatar,HaprampPreferenceManager.getInstance().getUser().image_uri);
        ratingView.setIntials(
                String.valueOf(postId),
                isVoted,
                mVote);

    }

    @Override
    public void onCommentFetched(CommentsResponse response) {

        prepareComments(response.comments);
        commentCount.setText(String.valueOf(response.comment_count));

    }

    private void prepareComments(List<CommentsResponse.Comments> comments) {

        // fetch all commetns
        // for each comment - request user and merge the infos
        // at the end set the result to recyclerView
        this.comments = comments;
        for(int i = 0;i<comments.size();i++){
            // request user
            DataServer.requestUser(i,comments.get(i).user_id,this);
        }
    }

    @Override
    public void onCommentFetchError() {

    }

//
//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null)
//            return;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//
//    }

    @Override
    public void onUserFetched(int commentPosition , UserResponse response) {

        // get the comments and response to create new CommentModel and add to recycler view

        commentsAdapter.addComment(new CommentModel(
                String.valueOf(comments.get(commentPosition).id),
                String.valueOf(comments.get(commentPosition).user_id),
                response.image_uri,
                response.full_name,
                comments.get(commentPosition).content,
                comments.get(commentPosition).created_at
        ));

    }

    @Override
    public void onUserFetchError() {
        Toast.makeText(this,"Error While Fetching Comments!",Toast.LENGTH_SHORT).show();
    }
}
