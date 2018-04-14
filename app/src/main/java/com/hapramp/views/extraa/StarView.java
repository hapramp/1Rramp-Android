package com.hapramp.views.extraa;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;

/**
 * Created by Ankit on 12/14/2017.
 */

public class StarView extends FrameLayout {

    private static final long REVEAL_DELAY = 500;
    private static final float REVEAL_START_OFFSET = 84;
    private Context mContext;
    private static final long HIDE_RATING_BAR_DELAY = 2000;
    TextView starIndicator;
    TextView starInfo;
    RatingBar ratingBar;
    LinearLayout ratingBarContainer;
    TextView cancelRateBtn;
    TextView ratingError;
    ProgressBar ratingProgress;
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

    /*
    *  Vote percent: will be like:
    *  for 1 star : 20 * 100 * 1
    *  for 2 star : 20 * 100 * 2 and so on..
    *
    *  VoteCount : active votes whose percent > 0
    *  VoteSum : (Sum of all active vote percent)/(20 * 100)
    *  Avg: VoteSum/VoteCount
    *
    *  This view receives vote as percent
    *
    * */

    private void init(Context context) {

        this.mContext = context;
        View v = LayoutInflater.from(mContext).inflate(R.layout.star_view, this);
        starIndicator = v.findViewById(R.id.starIndicator);
        starInfo = v.findViewById(R.id.starInfo);
        ratingBar = v.findViewById(R.id.ratingBar);
        ratingError = v.findViewById(R.id.ratingError);
        ratingProgress = v.findViewById(R.id.ratingProgress);
        ratingBarContainer = v.findViewById(R.id.ratingBarContainer);
        cancelRateBtn = v.findViewById(R.id.cancelBtn);
        ratingError.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
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
                voteState.postPermlink,
                voteState.myVotePercent,
                voteState.totalVotedUsers,
                voteState.totalVotePercentSum);

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
            ratingBar.setRating(getMappedRatingFromPercent(currentState.myVotePercent));
        } else {
            ratingBar.setRating(0);
        }

    }

    private float getMappedRatingFromPercent(float myVotePercent) {
        return myVotePercent / 2000;
    }

    private void showRatingProgress(boolean show) {
        l("Show rating progress " + show);
        if (show) {
            //hide text
            starInfo.setVisibility(GONE);
            //show progress
            ratingProgress.setVisibility(VISIBLE);
        } else {
            //show text
            starInfo.setVisibility(VISIBLE);
            //hide progress
            ratingProgress.setVisibility(GONE);
        }
    }

    private void setStarIndicatorState(boolean isRated) {

        if (isRated) {
            // color it
            starIndicator.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            // set info
            starInfo.setText(getRatingDescriptionWithAverage());

        } else {
            starIndicator.setTextColor(Color.parseColor("#8a000000"));
            //cancel my rate
            starInfo.setText(getRatingDescriptionWithAverage());
        }

    }

    // This method returns string with average and vote count

    private String getRatingDescriptionWithAverage() {

        float voteSum = getVoteSumFromVotePercentSum(currentState.getTotalVotePercentSum());
        int _totalUser = (int) currentState.getTotalVotedUsers();

        return _totalUser > 0 ? String.format(mContext.getResources().getString(R.string.star_info), ((voteSum) / _totalUser), _totalUser) : "0.0 from 0";

    }

    private float getVoteSumFromVotePercentSum(float totalVotePercentSum) {
        return totalVotePercentSum / 2000;
    }

    private float getRating() {

        return ratingBar.getRating();

    }

    public void onStarIndicatorTapped() {


        if (currentState.iHaveVoted) {
            // cancel all my ratings
            cancelMyRating();

            // showRatingBar();
            // showRatingCancelButton();
        } else {
            // rate 5 star
            setNewRating(5);
            // showRatingBar();
            // removeRatingCancelButton();
        }

    }

    public void onStarIndicatorLongPressed() {

        if (currentState.iHaveVoted) {
            showRatingBar();
            showRatingCancelButton();
        } else {
            showRatingBar();
            removeRatingCancelButton();
        }

    }


    private void cancelMyRating() {

        // cancel my existing ratings
        currentState.setiHaveVoted(false);
        currentState.totalVotedUsers -= 1;
        currentState.totalVotePercentSum -= currentState.myVotePercent;
        currentState.myVotePercent = 0;
        // reset rating Bar
        ratingBar.setRating(0);
        // update the UI
        setCurrentState(currentState);
        deleteVoteFromSteem();

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
                    sendVoteToSteem();
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
        currentState.setMyVotePercent(rating);
        currentState.totalVotedUsers += 1;
        currentState.totalVotePercentSum += getVotePercentFromRating(rating);
        setCurrentState(currentState);

    }

    private float getVotePercentFromRating(float rating) {
        return rating * 2000;
    }

    private void l(String s) {
        Log.i("STRV", s);
    }

    private void sendVoteToSteem() {
        onVoteUpdateCallback.onVoted(currentState.postPermlink, (int) currentState.myVotePercent);
    }

    private void deleteVoteFromSteem() {
        onVoteUpdateCallback.onVoteDeleted(currentState.postPermlink);
    }

    public void onVoteLoading() {
        //show progress
        showRatingProgress(true);
    }

    public void onVoteLoadingFailed() {
        //hide progress
        showRatingProgress(false);
        //show error
        ratingError.setVisibility(VISIBLE);

    }

    public void onVoteLoaded() {
        //hide progress
        showRatingProgress(false);
        //hide error
        ratingError.setVisibility(GONE);
    }

    public void voteProcessing() {
        showRatingProgress(true);
    }

    public void castedVoteSuccessfully() {
        // set new state as the legacy state
        this.legacyState = currentState;
        showRatingProgress(false);
    }

    public void deletedVoteSuccessfully() {
        // set new state as the legacy state
        this.legacyState = currentState;
        showRatingProgress(false);
    }

    public void failedToDeleteVoteFromServer() {
        this.currentState = legacyState;
        setCurrentState(currentState);
        showRatingProgress(false);
    }

    public void failedToCastVote() {
        // re-gain the legacy state
        this.currentState = legacyState;
        setCurrentState(currentState);
        showRatingProgress(false);

    }


    public static class Vote {

        boolean iHaveVoted;
        String postPermlink;
        float myVotePercent;
        float totalVotedUsers;
        float totalVotePercentSum;

        public Vote() {
        }

        public Vote(boolean iHaveVoted, String postPermlink, float vote, float totalVotedUsers, float totalVotesSum) {
            this.iHaveVoted = iHaveVoted;
            this.postPermlink = postPermlink;
            this.myVotePercent = vote;
            this.totalVotedUsers = totalVotedUsers;
            this.totalVotePercentSum = totalVotesSum;
        }

        public boolean getiHaveVoted() {
            return iHaveVoted;
        }

        public void setiHaveVoted(boolean iHaveVoted) {
            this.iHaveVoted = iHaveVoted;
        }

        public float getMyVotePercent() {
            return myVotePercent;
        }

        public void setMyVotePercent(float myVotePercent) {
            this.myVotePercent = myVotePercent;
        }

        public float getTotalVotedUsers() {
            return totalVotedUsers;
        }

        public void setTotalVotedUsers(float totalVotedUsers) {
            this.totalVotedUsers = totalVotedUsers;
        }

        public float getTotalVotePercentSum() {
            return totalVotePercentSum;
        }

        public void setTotalVotePercentSum(float totalVotePercentSum) {
            this.totalVotePercentSum = totalVotePercentSum;
        }

        @Override
        public String toString() {
            return "Vote{" +
                    "iHaveVoted=" + iHaveVoted +
                    ", postPermlink=" + postPermlink +
                    ", myVotePercent=" + myVotePercent +
                    ", totalVotedUsers=" + totalVotedUsers +
                    ", totalVotePercentSum=" + totalVotePercentSum +
                    '}';
        }
    }

    public interface onVoteUpdateCallback {

        void onVoted(String permlink, int vote);

        void onVoteDeleted(String permlink);

    }

}
