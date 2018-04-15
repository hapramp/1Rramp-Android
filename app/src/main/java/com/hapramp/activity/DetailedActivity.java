package com.hapramp.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.SteemCommentModel;
import com.hapramp.steem.PostStructureModel;
import com.hapramp.steem.SteemCommentCreator;
import com.hapramp.steem.SteemHelper;
import com.hapramp.steem.SteemReplyFetcher;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.data.ActiveVote;
import com.hapramp.steem.models.user.Profile;
import com.hapramp.steem.models.user.SteemUser;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.views.comments.CommentView;
import com.hapramp.views.extraa.StarView;
import com.hapramp.views.renderer.RendererView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

import static android.view.View.VISIBLE;

public class DetailedActivity extends AppCompatActivity implements SteemCommentCreator.SteemCommentCreateCallback, SteemReplyFetcher.SteemReplyFetchCallback {


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
    ScrollView scroller;
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
    TextView sendCommentButton;
    @BindView(R.id.commentInputContainer)
    RelativeLayout commentInputContainer;
    private String currentCommentUrl;
    private Handler mHandler;
    private Feed post;
    private boolean commentBarVisible;
    private ProgressDialog progressDialog;
    private SteemCommentCreator steemCommentCreator;
    private SteemReplyFetcher replyFetcher;
    private List<SteemCommentModel> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_post);
        ButterKnife.bind(this);
        collectExtras();
        init();
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

    private void init(){

        mHandler = new Handler();
        steemCommentCreator = new SteemCommentCreator();
        replyFetcher = new SteemReplyFetcher();
        replyFetcher.setSteemReplyFetchCallback(this);
        steemCommentCreator.setSteemCommentCreateCallback(this);

    }

    private void collectExtras() {

        post = getIntent().getExtras().getParcelable(Constants.EXTRAA_KEY_POST_DATA);
        currentCommentUrl = String.format(getResources().getString(R.string.commentUrl), Long.valueOf(post.id));
        progressDialog = new ProgressDialog(this);

    }

    private void fetchComments() {
        replyFetcher.requestReplyForPost(post.author,post.permlink);
    }

    @Override
    public void onReplyFetching() {
        if(commentLoadingProgressBar!=null){
            commentLoadingProgressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onReplyFetched(List<SteemCommentModel> discussions) {

        if(commentLoadingProgressBar!=null){
            commentLoadingProgressBar.setVisibility(View.GONE);
        }

        setCommentCount(discussions.size());
        addCommentsToView(discussions);
        comments = discussions;
        Log.d("DetailedActivity","Replies "+discussions.toString());

    }

    @Override
    public void onReplyFetchError() {
        if(commentLoadingProgressBar!=null){
            commentLoadingProgressBar.setVisibility(View.GONE);
        }
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
                navigateToCommentsPage();
            }
        });

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        commentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCommentsPage();
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCommentsPage();
            }
        });

    }

    private void navigateToCommentsPage() {

        Intent intent = new Intent(DetailedActivity.this, CommentsActivity.class);
        intent.putExtra(Constants.EXTRAA_KEY_POST_AUTHOR,post.author);
        intent.putExtra(Constants.EXTRAA_KEY_POST_PERMLINK,post.permlink);
        intent.putParcelableArrayListExtra(Constants.EXTRAA_KEY_COMMENTS, (ArrayList<SteemCommentModel>) comments);
        startActivity(intent);

    }

    private void postComment() {

        String cmnt = commentInputBox.getText().toString().trim();
        commentInputBox.setText("");
        if (cmnt.length() > 2) {
            steemCommentCreator.createComment(cmnt,post.author,post.permlink);
        } else {
            Toast.makeText(this, "Comment Too Short!!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCommentCreateProcessing() {
        showProgress("Posting Your Comment...");
    }

    @Override
    public void onCommentCreated() {
        hideProgress();
        Toast.makeText(this,"Comment Created",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCommentCreateFailed() {
        hideProgress();
        Toast.makeText(this,"Comment Operation Failed",Toast.LENGTH_LONG).show();
    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
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
        sendCommentButton.setTypeface(t);
        sendButtonMock.setTypeface(t);

    }

    //
    private void bindValues() {

        // set basic meta-info
        Profile _p = new Gson().fromJson(HaprampPreferenceManager.getInstance().getUserProfile(post.author), Profile.class);
        if (_p != null) {
            ImageHandler.loadCircularImage(this, feedOwnerPic, _p.getProfileImage());
        }

        feedOwnerTitle.setText(post.author);
        feedOwnerSubtitle.setText(
                String.format(getResources().getString(R.string.post_subtitle_format),
                        MomentsUtils.getFormattedTime(post.created)));

        setCommunities(post.jsonMetadata.tags);
        PostStructureModel postStructureModel = new PostStructureModel(post.jsonMetadata.content.getData(),post.jsonMetadata.getContent().type);
        renderView.render(postStructureModel);

        SteemUser steemUser = new Gson().fromJson(HaprampPreferenceManager.getInstance().getCurrentUserInfoAsJson(), SteemUser.class);
        String user_profile_url = steemUser.getUser().getJsonMetadata().getProfile().getProfileImage() != null ?
                steemUser.getUser().getJsonMetadata().getProfile().getProfileImage()
                :
                "";

        ImageHandler.loadCircularImage(this, commentCreaterAvatar, user_profile_url);
        ImageHandler.loadCircularImage(this, commentCreaterAvatarMock, user_profile_url);

        setSteemEarnings(post.totalPayoutValue);
        bindVotes(post.activeVotes, post.permlink);
        attachListenersOnStarView();

    }

    //==================================================================================
    // Vote part begins

    private void attachListenersOnStarView() {

        starView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionUtils.isConnected(DetailedActivity.this)) {
                    starView.onStarIndicatorTapped();
                }else{
                    Toast.makeText(DetailedActivity.this,"Check Network Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        starView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (ConnectionUtils.isConnected(DetailedActivity.this)) {
                    starView.onStarIndicatorLongPressed();
                }else{
                    Toast.makeText(DetailedActivity.this,"Check Network Connection",Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }

    private void bindVotes(List<ActiveVote> votes, String permlink) {

        long votePercentSum = getVotePercentSum(votes);
        boolean amIVoted = checkForMyVote(votes);
        long myVotePercent = amIVoted ? getMyVotePercent(votes) : 0;
        long totalVotes = getNonZeroVoters(votes);

        starView.setVoteState(
                new StarView.Vote(
                        amIVoted,
                        permlink,
                        myVotePercent,
                        totalVotes,
                        votePercentSum))
                .setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
                    @Override
                    public void onVoted(String full_permlink, int _vote) {
                        performVoteOnSteem(_vote);
                    }

                    @Override
                    public void onVoteDeleted(String full_permlink) {
                        deleteVoteOnSteem();
                    }
                });


    }

    private long getNonZeroVoters(List<ActiveVote> votes) {
        long sum = 0;
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).percent > 0) {
                sum++;
            }
        }
        return sum;
    }

    private long getVotePercentSum(List<ActiveVote> activeVotes) {
        long sum = 0;
        for (int i = 0; i < activeVotes.size(); i++) {
            sum += activeVotes.get(i).percent;
        }
        return sum;
    }

    private long getMyVotePercent(List<ActiveVote> votes) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).voter.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
                return votes.get(i).percent;
            }
        }
        return 0;
    }

    private boolean checkForMyVote(List<ActiveVote> votes) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).voter.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername()) && votes.get(i).percent > 0) {
                return true;
            }
        }
        return false;
    }

    /*
    *  author of the vote: author of the pose
    *  votePower: 0 for 1-2 ratte
    *  votePower: 100 for 3-5 rate
    * */
    private void performVoteOnSteem(final int vote) {

        starView.voteProcessing();

        final int votePower = vote;
        Log.d("VoteTest", "voting with percent " + votePower);
        new Thread() {

            @Override
            public void run() {
                try {

                    AccountName voteFor = new AccountName(post.author);
                    AccountName voter = new AccountName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
                    SteemJ steemJ = SteemHelper.getSteemInstance();

                    steemJ.vote(voter, voteFor, new Permlink(post.permlink), (short) votePower);
                    Log.d("VoteTest", "voted " + votePower);
                    //callback for success
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            castingVoteSuccess();
                        }
                    });

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "error " + e.toString());
                    mHandler.post(steemCastingVoteExceptionRunnable);
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "error " + e.toString());
                    mHandler.post(steemCastingVoteExceptionRunnable);
                } catch (SteemInvalidTransactionException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "error " + e.toString());
                    mHandler.post(steemCastingVoteExceptionRunnable);
                }

            }
        }.start();

    }

    /*
      *  author of the vote: author of the pose
      *  cancel vote
      * */
    private void deleteVoteOnSteem() {

        Log.d("VoteTest", "Deleting vote");

        starView.voteProcessing();

        new Thread() {

            @Override
            public void run() {
                try {

                    AccountName voteFor = new AccountName(post.author);
                    AccountName voter = new AccountName(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
                    SteemJ steemJ = SteemHelper.getSteemInstance();
                    steemJ.cancelVote(voter, voteFor, new Permlink(post.permlink));
                    Log.d("VoteTest", "Deleted Vote");

                    //callback for success
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            voteDeleteSuccess();
                        }
                    });

                } catch (SteemCommunicationException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "Deleting vote error " + e.toString());

                    mHandler.post(steemCancellingVoteExceptionRunnable);
                } catch (SteemResponseException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "Deleting vote error " + e.toString());
                    mHandler.post(steemCancellingVoteExceptionRunnable);
                } catch (SteemInvalidTransactionException e) {
                    e.printStackTrace();
                    Log.d("VoteTest", "Deleting vote error " + e.toString());
                    mHandler.post(steemCancellingVoteExceptionRunnable);
                }

            }
        }.start();

    }

    private void castingVoteFailed() {
        if (starView != null) {
            starView.failedToCastVote();
        }
        Toast.makeText(this, "FAILED : Vote Casting", Toast.LENGTH_LONG).show();
    }

    private void castingVoteSuccess() {
        if (starView != null) {
            starView.castedVoteSuccessfully();
        }
        Toast.makeText(this, "SUCCESS : Vote Casting", Toast.LENGTH_LONG).show();
    }

    private void voteDeleteFailed() {
        if (starView != null) {
            starView.failedToDeleteVoteFromServer();
        }
        Toast.makeText(this, "FAILED : Vote Delete", Toast.LENGTH_LONG).show();
    }

    private void voteDeleteSuccess() {
        if (starView != null) {
            starView.deletedVoteSuccessfully();
        }
        Toast.makeText(this, "SUCCESS : Vote Deleted", Toast.LENGTH_LONG).show();
    }

    private Runnable steemCastingVoteExceptionRunnable = new Runnable() {
        @Override
        public void run() {
            castingVoteFailed();
        }
    };

    private Runnable steemCancellingVoteExceptionRunnable = new Runnable() {
        @Override
        public void run() {
            voteDeleteFailed();
        }
    };

    // Ends Vote part
    // ================================================================================


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

    private void setSteemEarnings(String payout) {
        hapcoinsCount.setText(String.format(getResources().getString(R.string.hapcoins_format), payout.substring(0, payout.indexOf(' '))));
    }

    private void setCommentCount(int count) {
        commentCount.setText(String.format(getResources().getString(R.string.comment_format), count));
    }

    private void addCommentsToView(List<SteemCommentModel> discussions) {

        int commentCount = discussions.size();
        commentLoadingProgressBar.setVisibility(View.GONE);
        if (commentCount == 0) {
            emptyCommentsCaption.setVisibility(VISIBLE);
        }

        int range = commentCount > 3 ? 3 : discussions.size();

        for (int i = 0; i < range; i++) {

            CommentView view = new CommentView(this);
            view.setComment(discussions.get(i));
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

}
