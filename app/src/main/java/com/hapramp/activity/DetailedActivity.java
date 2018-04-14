package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.models.response.PostResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.user.Profile;
import com.hapramp.steem.models.user.SteemUser;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.comments.CommentView;
import com.hapramp.views.extraa.StarView;
import com.hapramp.views.renderer.RendererView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.VISIBLE;

public class DetailedActivity extends AppCompatActivity {


    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.overflowBtn)
    TextView overflowBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.feed_owner_pic)
    ImageView feedOwnerPic;
    @BindView(R.id.reference_line)
    Space referenceLine;
    @BindView(R.id.feed_owner_title)
    TextView feedOwnerTitle;
    @BindView(R.id.feed_owner_subtitle)
    TextView feedOwnerSubtitle;
    @BindView(R.id.club3)
    TextView club3;
    @BindView(R.id.club2)
    TextView club2;
    @BindView(R.id.club1)
    TextView club1;
    @BindView(R.id.post_header_container)
    RelativeLayout postHeaderContainer;
    @BindView(R.id.renderView)
    RendererView renderView;
    @BindView(R.id.shareBtn)
    TextView shareBtn;
    @BindView(R.id.commentsViewContainer)
    LinearLayout commentsViewContainer;
    @BindView(R.id.commentLoadingProgressBar)
    ProgressBar commentLoadingProgressBar;
    @BindView(R.id.emptyCommentsCaption)
    TextView emptyCommentsCaption;
    @BindView(R.id.moreCommentsCaption)
    TextView moreCommentsCaption;
    @BindView(R.id.commentCreaterAvatarMock)
    ImageView commentCreaterAvatarMock;
    @BindView(R.id.commentInputBoxMock)
    EditText commentInputBoxMock;
    @BindView(R.id.sendButtonMock)
    TextView sendButtonMock;
    @BindView(R.id.mockCommentParentView)
    RelativeLayout mockCommentParentView;
    @BindView(R.id.scroller)
    NestedScrollView scroller;
    @BindView(R.id.shadow)
    ImageView shadow;
    @BindView(R.id.commentBtn)
    TextView commentBtn;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.hapcoinBtn)
    TextView hapcoinBtn;
    @BindView(R.id.hapcoins_count)
    TextView hapcoinsCount;
    @BindView(R.id.starView)
    StarView starView;
    @BindView(R.id.postMetaContainer)
    RelativeLayout postMetaContainer;
    @BindView(R.id.commentCreaterAvatar)
    ImageView commentCreaterAvatar;
    @BindView(R.id.commentInputBox)
    EditText commentInputBox;
    @BindView(R.id.sendButton)
    TextView sendButton;
    @BindView(R.id.commentInputContainer)
    RelativeLayout commentInputContainer;
    private List<CommentsResponse.Results> comments;
    private String currentCommentUrl;

    private Feed post;
    private boolean commentBarVisible;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_post);
        ButterKnife.bind(this);
        collectExtras();
        fetchComments();
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
    public void onBackPressed() {

        if (commentBarVisible) {
            showCommentBar(false);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void collectExtras() {

        post = getIntent().getExtras().getParcelable(Constants.EXTRAA_KEY_POST_DATA);
        currentCommentUrl = String.format(getResources().getString(R.string.commentUrl), Long.valueOf(post.id));
        progressDialog = new ProgressDialog(this);

    }

    private void fetchComments() {

    }

    private void attachListener() {

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        starView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starView.onStarIndicatorTapped();
            }
        });

        commentInputBoxMock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentBar(true);
            }
        });

        moreCommentsCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailedActivity.this, CommentsActivity.class);
                intent.putExtra(Constants.EXTRAA_KEY_POST_ID, String.valueOf(post.id));
                startActivity(intent);

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

    }


    private void postComment() {

        String cmnt = commentInputBox.getText().toString().trim();
        commentInputBox.setText("");
        if (cmnt.length() > 2) {
            showProgress("Posting Your Comment...");
            // DataServer.createComment(String.valueOf(post.id), new CommentBody(cmnt), this);
        } else {
            Toast.makeText(this, "Comment Too Short!!", Toast.LENGTH_LONG).show();
        }

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

    private void setTypefaces() {

        Typeface t = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        closeBtn.setTypeface(t);
        overflowBtn.setTypeface(t);
        commentBtn.setTypeface(t);
        hapcoinBtn.setTypeface(t);
        sendButton.setTypeface(t);
        sendButtonMock.setTypeface(t);

    }

    //
    private void bindValues() {

        // set basic meta-info
        Profile _p = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserProfile(post.getAuthor()), Profile.class);
        if (_p != null) {
            ImageHandler.loadCircularImage(this, feedOwnerPic, _p.getProfileImage());
        }

        feedOwnerTitle.setText(post.author);
        feedOwnerSubtitle.setText(
                String.format(getResources().getString(R.string.post_subtitle_format),
                        MomentsUtils.getFormattedTime(post.created)));

        setCommunities(post.jsonMetadata.tags);
        PostStructureModel postStructureModel = new PostStructureModel(post.getJsonMetadata().content.getData(),post.getJsonMetadata().getContent().type);
        renderView.render(postStructureModel);
//        content.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        content.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + post.jsonMetadata.content., "text/html", "UTF-8", null);
//        // initialize the starview
//        starView.setVoteState(
//                new StarView.Vote(
//                        post.is_voted,
//                        post.id,
//                        post.current_vote,
//                        post.vote_sum,
//                        post.vote_count
//                )).setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
//            @Override
//            public void onVoted(int postId, int vote) {
//                vote(postId, vote);
//            }
//
//            @Override
//            public void onVoteDeleted(int postId) {
//                deleteVote(postId);
//            }
//        });

        //String _comment_info = post.comment_count > 1 ? String.valueOf(post.comment_count).concat(" comments") : String.valueOf(post.comment_count).concat(" comment");
//        setCommentCount(post.comment_count);
//        setHapcoins(post.hapcoins);

        SteemUser steemUser = new Gson().fromJson(HaprampPreferenceManager.getInstance().getCurrentUserInfoAsJson(), SteemUser.class);
        String user_profile_url = steemUser.getUser().getJsonMetadata().getProfile().getProfileImage() != null ?
                steemUser.getUser().getJsonMetadata().getProfile().getProfileImage()
                :
                "";

        ImageHandler.loadCircularImage(this, commentCreaterAvatar, user_profile_url);
        ImageHandler.loadCircularImage(this, commentCreaterAvatarMock, user_profile_url);

    }

    private void setCommunities(List<String> communities) {
        // community name + community color
        List<CommunityModel> cm = new ArrayList<>();
        for (int i = 0; i < communities.size(); i++) {
            if (Communities.doesCommunityExists(communities.get(i))) {
                cm.add(new CommunityModel("", "", communities.get(i),
                        HaprampPreferenceManager.getInstance().getCommunityColorFromTag(communities.get(i)),
                        HaprampPreferenceManager.getInstance().getCommunityNameFromTag(communities.get(i)),
                        0
                ));
            }
        }

        addCommunitiesToLayout(cm);

    }

    private void addCommunitiesToLayout(List<CommunityModel> cms) {

        int size = cms.size();
        if (size > 0) {
            //first skill
            club1.setVisibility(VISIBLE);

            club1.setText(cms.get(0).getmName());
            club1.getBackground().setColorFilter(
                    Color.parseColor(cms.get(0).getmColor()),
                    PorterDuff.Mode.SRC_ATOP);

            if (size > 1) {
                // second skills
                club2.setVisibility(VISIBLE);

                club2.setText(cms.get(1).getmName());
                club2.getBackground().setColorFilter(
                        Color.parseColor(cms.get(1).getmColor()),
                        PorterDuff.Mode.SRC_ATOP);

                if (size > 2) {
                    // third skills
                    club3.setVisibility(VISIBLE);

                    club3.setText(cms.get(2).getmName());
                    club3.getBackground().setColorFilter(
                            Color.parseColor(cms.get(2).getmColor()),
                            PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

    }

    private void setHapcoins(float hapcoins) {
        /// hapcoinsCount.setText(String.format(getResources().getString(R.string.hapcoins_format), hapcoins));
    }

    private void setCommentCount(int count) {
        commentCount.setText(String.format(getResources().getString(R.string.comment_format), count));
    }

    private void setSkills(List<PostResponse.Skills> skills) {

        int size = skills.size();
        if (size > 0) {
            //first skill
            club1.setVisibility(VISIBLE);
            club1.setText(SkillsUtils.getSkillTitleFromId(skills.get(0).id));
            club1.getBackground().setColorFilter(SkillsUtils.getSkillTagColorFromId(skills.get(0).id), PorterDuff.Mode.SRC_ATOP);
            if (size > 1) {
                // second skills
                club2.setVisibility(VISIBLE);
                club2.setText(SkillsUtils.getSkillTitleFromId(skills.get(1).id));
                club2.getBackground().setColorFilter(SkillsUtils.getSkillTagColorFromId(skills.get(1).id), PorterDuff.Mode.SRC_ATOP);
                if (size > 2) {
                    // third skills
                    club3.setVisibility(VISIBLE);
                    club3.setText(SkillsUtils.getSkillTitleFromId(skills.get(2).id));
                    club3.getBackground().setColorFilter(SkillsUtils.getSkillTagColorFromId(skills.get(2).id), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

    }

    private void addComment(List<CommentsResponse.Results> results) {

        int commentCount = results.size();
        commentLoadingProgressBar.setVisibility(View.GONE);
        if (commentCount == 0) {
            emptyCommentsCaption.setVisibility(VISIBLE);
        }

        int range = commentCount > 3 ? 3 : results.size();

        for (int i = 0; i < range; i++) {

            CommentView view = new CommentView(this);
            view.setComment(results.get(i));
            commentsViewContainer.addView(view, i,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

        }

        if (commentCount > 3) {
            moreCommentsCaption.setVisibility(VISIBLE);
        }

    }

    private void showCommentBar(boolean show) {

        if (show) {
            commentBarVisible = true;
            // hide mock bar and show real input box
            scaleAndHideMainView(mockCommentParentView);
//            commentInputContainer.animate()
//                    .translationY(0)
//                    .translationYBy(PixelUtils.dpToPx(64))
//                    .setDuration(1000)
//                    .start();
            commentInputContainer.setVisibility(VISIBLE);

        } else {

            commentBarVisible = false;
            //show mock bar and hide real input box
            mockCommentParentView.setVisibility(VISIBLE);
//            commentInputContainer.animate()
//                    .translationY(PixelUtils.dpToPx(64))
//                    .setDuration(1000)
//                    .start();
            commentInputContainer.setVisibility(View.GONE);

        }

    }

    public void scaleAndHideMainView(final View view) {

        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                1f, 0f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(false); // Needed to keep the result of the animation
        anim.setDuration(200);
        anim.setInterpolator(new DecelerateInterpolator(1f));
        view.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


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
        // DataServer.deletePost(String.valueOf(post_id), pos, this);
    }

//    @Override
//    public void onCommentFetched(CommentsResponse response) {
//
//        addComment(response.results);
//
//    }
//
//
//    @Override
//    public void onCommentFetchError() {
//
//    }
//
//    @Override
//    public void onUserFetched(int commentPosition, UserResponse response) {
//
//    }
//
//    @Override
//    public void onUserFetchError() {
//        Toast.makeText(this, "Error While Fetching Comments!", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPostDeleted(int position) {
//
//    }
//
//    @Override
//    public void onPostDeleteFailed() {
//
//    }
//
//    private void deleteVote(int postId) {
//        DataServer.deleteVote(postId, this);
//    }
//
//    private void vote(int postId, int vote) {
//        DataServer.votePost(String.valueOf(postId), new VoteRequestBody((int) vote), this);
//    }

//    @Override
//    public void onVoteDeleted(Feed updatedPost) {
//        //update mHapcoins
//        setHapcoins(updatedPost.hapcoins);
//    }
//
//    @Override
//    public void onVoteDeleteError() {
//
//    }
//
//    @Override
//    public void onPostVoted(Feed updatedPost) {
//        //update mHapcoins
//        setHapcoins(updatedPost.hapcoins);
//    }
//
//    @Override
//    public void onPostVoteError() {
//
//    }
//
//    @Override
//    public void onCommentCreated(CommentCreateResponse comment) {
//
//        hideProgress();
//
//        CommentView view = new CommentView(this);
//        UserResponse user = HaprampPreferenceManager.getInstance().getUser();
//        view.setComment(new CommentsResponse.Results(
//                comment.id,
//                comment.created_at,
//                comment.content, false, 0
//                , new CommentsResponse.User(user.id, user.username, user.full_name, user.image_uri)));
//
//        commentsViewContainer.addView(view, 0,
//                new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//
//    }
//
//    @Override
//    public void onCommentCreateError() {
//        hideProgress();
//    }
}
