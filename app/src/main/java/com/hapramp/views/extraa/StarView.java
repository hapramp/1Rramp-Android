package com.hapramp.views.extraa;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.interfaces.VoteDeleteCallback;
import com.hapramp.interfaces.VotePostCallback;
import com.hapramp.models.Feed;
import com.hapramp.models.response.PostResponse;
import com.hapramp.utils.FontManager;

/**
 * Created by Ankit on 12/14/2017.
 */

public class StarView extends FrameLayout implements VotePostCallback, VoteDeleteCallback {

    private static final long REVEAL_DELAY = 500;
    private static final float REVEAL_START_OFFSET = 84;
    private Context mContext;
    private static final long HIDE_RATING_BAR_DELAY = 2000;
    TextView starIndicator;
    TextView starInfo;
    RatingBar ratingBar;
    LinearLayout ratingBarContainer;
    TextView cancelRateBtn;
    private Handler mHandler;
    private Runnable autoHideRatingBarRunnable = new Runnable() {
        @Override
        public void run() {
            hideRatingBar();
        }
    };
    private Vote currentState;
    private Vote legacyState;
    private onVoteUpdateCallback onVoteUpdateCallback;

    public StarView(@NonNull Context context) {

        super(context);
        init(context);

    }

    public StarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {

        this.mContext = context;
        View v = LayoutInflater.from(mContext).inflate(R.layout.star_view, this);
        starIndicator = (TextView) v.findViewById(R.id.starIndicator);
        starInfo = (TextView) v.findViewById(R.id.starInfo);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        ratingBarContainer = (LinearLayout) v.findViewById(R.id.ratingBarContainer);
        cancelRateBtn = (TextView) v.findViewById(R.id.cancelBtn);
        starIndicator.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        cancelRateBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        setRatingBarListener();

        mHandler = new Handler();

        cancelRateBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelMyRating();
                hideRatingBar();

            }
        });

    }

    public StarView setVoteState(Vote voteState) {

        this.legacyState = new Vote(
                voteState.iHaveVoted,
                voteState.postId,
                voteState.myVote,
                voteState.totalVotedUsers,
                voteState.totalVotesSum);

        setCurrentState(voteState);
        return this;
    }

    public void setOnVoteUpdateCallback(StarView.onVoteUpdateCallback onVoteUpdateCallback) {
        this.onVoteUpdateCallback = onVoteUpdateCallback;
    }

    private void setCurrentState(Vote voteState) {

        l("Setting current vote " + voteState.toString());
        this.currentState = voteState;
        setStarIndicatorState(currentState.iHaveVoted);

        if (voteState.iHaveVoted) {
            ratingBar.setRating(currentState.myVote);
        } else {
            ratingBar.setRating(0);
        }

    }

    private void setStarIndicatorState(boolean isRated) {

        if (isRated) {
            // color it
            starIndicator.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            // set info
            starInfo.setText(getCalculatedInfo());

        } else {
            starIndicator.setTextColor(Color.parseColor("#8a000000"));
            //cancel my rate
            starInfo.setText(getCalculatedInfo());
        }

    }

    private String getCalculatedInfo() {

        float _rating = currentState.getTotalVotesSum();
        int _totalUser = (int) currentState.getTotalVotedUsers();

        return _totalUser > 0 ? String.format(mContext.getResources().getString(R.string.star_info), ((_rating) / _totalUser), _totalUser) : "0.0 of 0";

    }

    private float getRating() {

        return ratingBar.getRating();

    }

    public void onStarIndicatorTapped() {

        if (currentState.iHaveVoted) {
            // show the rating bar with cancel btn
            showRatingBar();
            showRatingCancelButton();
        } else {
            // show the rating bar without cancel btn
            showRatingBar();
            removeRatingCancelButton();

        }

    }

    private void cancelMyRating() {

        // cancel my existing ratings
        currentState.setiHaveVoted(false);
        currentState.totalVotedUsers -= 1;
        currentState.totalVotesSum -= currentState.myVote;
        currentState.myVote = 0;
        // reset rating Bar
        ratingBar.setRating(0);
        // update the UI
        setCurrentState(currentState);
        deleteVoteFromAppServer();

    }

    private void showRatingBar() {
        // for now just show the bar
        //ratingBarContainer.setVisibility(VISIBLE);
        revealRatingBar();
        mHandler.postDelayed(autoHideRatingBarRunnable, HIDE_RATING_BAR_DELAY);

    }


    private void hideRatingBar() {
        // for now just hide the visibility of Rating Bar
        //ratingBarContainer.setVisibility(GONE);
        collapseRatingBar();

    }

    private void revealRatingBar() {
//        // View to reveal -> ratingBarContainer
//        // width of view
//        int w = ratingBarContainer.getWidth();
//        // height of view
//        int h = ratingBarContainer.getHeight();
//
//        // radius of reveal
//        int endRadius = (int) Math.hypot(w, h);
//
//        int cx = (int) (ratingBarContainer.getX() + REVEAL_START_OFFSET);
//        int cy = (int) (ratingBarContainer.getY()) + ratingBarContainer.getHeight();
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            ratingBarContainer.setVisibility(View.VISIBLE);
//            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(ratingBarContainer, cx, cy, 0, endRadius);
//            revealAnimator.setInterpolator(new DecelerateInterpolator(2f));
//            revealAnimator.setDuration(REVEAL_DELAY);
//            revealAnimator.start();
//
//        }else{
            ratingBarContainer.setVisibility(View.VISIBLE);
        //}


    }

    private void collapseRatingBar() {

//        // View to collapse -> ratingBarContainer
//        // width of view
//        int w = ratingBarContainer.getWidth();
//        // height of view
//        int h = ratingBarContainer.getHeight();
//
//        // radius of reveal/collapse
//        int endRadius = (int) Math.hypot(w, h);
//
//        int cx = (int) (ratingBarContainer.getX() + REVEAL_START_OFFSET);
//        int cy = (int) (ratingBarContainer.getY()) + ratingBarContainer.getHeight();
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//
//            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(ratingBarContainer, cx, cy, endRadius, 0);
//            revealAnimator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    ratingBarContainer.setVisibility(GONE);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//
//                }
//            });
//            revealAnimator.setInterpolator(new AccelerateInterpolator(2f));
//            revealAnimator.setDuration(REVEAL_DELAY);
//            revealAnimator.start();
//
//        } else {
            ratingBarContainer.setVisibility(View.GONE);
        //}

    }

    private void showRatingCancelButton() {

        cancelRateBtn.setVisibility(VISIBLE);

    }

    private void removeRatingCancelButton() {

        cancelRateBtn.setVisibility(GONE);

    }


    private void setRatingBarListener() {

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                l("New Rating - " + rating);
                if (fromUser) {
                    //ratings has changed
                    setNewRating(rating);
                    sendVoteToAppServer();
                }
            }
        });

    }

    private void setNewRating(float rating) {

        mHandler.removeCallbacks(autoHideRatingBarRunnable);
        mHandler.postDelayed(autoHideRatingBarRunnable, HIDE_RATING_BAR_DELAY / 2);

        // update info
        if (currentState.iHaveVoted) {
            // reset my existing vote
            cancelMyRating();
        }

        currentState.setiHaveVoted(true);
        currentState.setMyVote(rating);
        currentState.totalVotedUsers += 1;
        currentState.totalVotesSum += rating;
        setCurrentState(currentState);

    }

    private void l(String s) {
     //   Log.i("STRV", s);
    }

    private void sendVoteToAppServer() {
        onVoteUpdateCallback.onVoted(currentState.postId,(int) currentState.myVote);
    }

    private void deleteVoteFromAppServer() {
        onVoteUpdateCallback.onVoteDeleted(currentState.postId);
    }

    @Override
    public void onPostVoted(final Feed updatedResult) {
        // set new state as the legacy state
        this.legacyState = currentState;
    }

    @Override
    public void onPostVoteError() {

        Toast.makeText(mContext, "Cannot Vote!", Toast.LENGTH_LONG).show();
        // re-gain the legacy state
        this.currentState = legacyState;
        setCurrentState(currentState);

    }


    @Override
    public void onVoteDeleted(Feed updatedPost) {

    }

    @Override
    public void onVoteDeleteError() {
        // re-gain the legacy state
     //   l("Vote Cannot Delete!");
        this.currentState = legacyState;
        setCurrentState(currentState);
    }

    public static class Vote {

        boolean iHaveVoted;
        int postId;
        float myVote;
        float totalVotedUsers;
        float totalVotesSum;

        public Vote() {
        }

        public Vote(boolean iHaveVoted, int postId, float vote, float totalVotedUsers, float totalVotesSum) {
            this.iHaveVoted = iHaveVoted;
            this.postId = postId;
            this.myVote = vote;
            this.totalVotedUsers = totalVotedUsers;
            this.totalVotesSum = totalVotesSum;
        }

        public boolean getiHaveVoted() {
            return iHaveVoted;
        }

        public void setiHaveVoted(boolean iHaveVoted) {
            this.iHaveVoted = iHaveVoted;
        }

        public float getMyVote() {
            return myVote;
        }

        public void setMyVote(float myVote) {
            this.myVote = myVote;
        }

        public float getTotalVotedUsers() {
            return totalVotedUsers;
        }

        public void setTotalVotedUsers(float totalVotedUsers) {
            this.totalVotedUsers = totalVotedUsers;
        }

        public float getTotalVotesSum() {
            return totalVotesSum;
        }

        public void setTotalVotesSum(float totalVotesSum) {
            this.totalVotesSum = totalVotesSum;
        }

        @Override
        public String toString() {
            return "Vote{" +
                    "iHaveVoted=" + iHaveVoted +
                    ", postId=" + postId +
                    ", myVote=" + myVote +
                    ", totalVotedUsers=" + totalVotedUsers +
                    ", totalVotesSum=" + totalVotesSum +
                    '}';
        }
    }

    public interface onVoteUpdateCallback{

        void onVoted(int postId, int vote);
        void onVoteDeleted(int postId);

    }

}
