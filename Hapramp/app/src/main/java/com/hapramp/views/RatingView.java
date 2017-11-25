package com.hapramp.views;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.VotePostCallback;
import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.utils.FontManager;

/**
 * Created by Ankit on 11/17/2017.
 */

public class RatingView extends FrameLayout implements VotePostCallback {

    private static final long HIDE_RATING_BAR_DELAY = 2000;

    TextView oneStar;
    TextView twoStar;
    TextView threeStar;
    TextView fourStar;
    TextView fiveStar;
    TextView oneStarFilled;
    TextView twoStarFilled;
    TextView threeStarFilled;
    TextView fourStarFilled;
    TextView fiveStarFilled;

    int delay = 500;
    float factor = 4f;

    private Context mContext;
    private Typeface typeface;
    private View ratingView;
    private boolean isVoted = false;
    private int mRate = 0;
    private Handler mHandler;
    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            ratingView.setVisibility(GONE);
            //update App server with the vote
            updateAppServer();
        }
    };
    private String mPostId;

    public RatingView(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RatingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RatingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        mHandler = new Handler();

        typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        ratingView = LayoutInflater.from(mContext).inflate(R.layout.rating_view, this);

        Log.d("ViewC", "inflated View");

        oneStar = (TextView) ratingView.findViewById(R.id.oneStar);
        twoStar = (TextView) ratingView.findViewById(R.id.twoStar);
        threeStar = (TextView) ratingView.findViewById(R.id.threeStar);
        fourStar = (TextView) ratingView.findViewById(R.id.fourStar);
        fiveStar = (TextView) ratingView.findViewById(R.id.fiveStar);

        oneStarFilled = (TextView) ratingView.findViewById(R.id.oneStarFilled);
        twoStarFilled = (TextView) ratingView.findViewById(R.id.twoStarFilled);
        threeStarFilled = (TextView) ratingView.findViewById(R.id.threeStarFilled);
        fourStarFilled = (TextView) ratingView.findViewById(R.id.fourStarFilled);
        fiveStarFilled = (TextView) ratingView.findViewById(R.id.fiveStarFilled);

        oneStar.setTypeface(typeface);
        twoStar.setTypeface(typeface);
        threeStar.setTypeface(typeface);
        fourStar.setTypeface(typeface);
        fiveStar.setTypeface(typeface);

        oneStarFilled.setTypeface(typeface);
        twoStarFilled.setTypeface(typeface);
        threeStarFilled.setTypeface(typeface);
        fourStarFilled.setTypeface(typeface);
        fiveStarFilled.setTypeface(typeface);


    }


    public void setIntials(String postId, boolean isVoted , int vote){
        this.mPostId = postId;
        this.isVoted = isVoted;
        this.mRate = vote;
    }

    private void setRate() {

        showRatingBar();
        pushRateStar(mRate);

    }

    public void addRating() {

        if (isVoted) {

            showRatingBar();

            for (int i = 1; i <= mRate; i++) {
                pushRateStar(i);
            }

            Toast.makeText(mContext, "You have Already Voted " + mRate, Toast.LENGTH_SHORT).show();

        } else {
            incrementVote();
            setRate();
        }

    }

    private void incrementVote() {
        mRate += (mRate<5)?1:0;
    }

    private void showRatingBar() {

        ratingView.setVisibility(VISIBLE);
        // move empty view out of screen
        ObjectAnimator toOut = ObjectAnimator.ofFloat(ratingView, "translationY", -40f);
        toOut.setRepeatCount(0);
        toOut.setInterpolator(new DecelerateInterpolator(factor));
        toOut.setDuration(delay);

        // fade in filled view
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(ratingView, "alpha", 1.0f);
        fadeIn.setDuration(delay);
        fadeIn.setInterpolator(new DecelerateInterpolator(factor));
        fadeIn.start();

        AnimatorSet set = new AnimatorSet();
        set.play(toOut);
        set.play(fadeIn);
        set.start();

        mHandler.removeCallbacks(hideRunnable);
        mHandler.postDelayed(hideRunnable, HIDE_RATING_BAR_DELAY);

    }

    private void pushRateStar(int star) {

        switch (star) {
            case 1:
                animateOneStar();
                break;
            case 2:
                animateTwoStar();
                break;
            case 3:
                animateThreeStar();
                break;
            case 4:
                animateFourStar();
                break;
            case 5:
                animateFiveStar();
                break;

        }

    }

    private void animateFiveStar() {
        animateView(fiveStar, fiveStarFilled);
    }

    private void animateFourStar() {
        animateView(fourStar, fourStarFilled);
    }

    private void animateThreeStar() {
        animateView(threeStar, threeStarFilled);
    }

    private void animateTwoStar() {
        animateView(twoStar, twoStarFilled);
    }

    private void animateOneStar() {
        animateView(oneStar, oneStarFilled);
    }

    private void animateView(View outView, View inView) {

        // move empty view out of screen
        ObjectAnimator toOut = ObjectAnimator.ofFloat(outView, "translationY", -40f);
        toOut.setRepeatCount(0);
        toOut.setInterpolator(new DecelerateInterpolator(factor));
        toOut.setDuration(delay);

        // move filled view in to screen
        ObjectAnimator toIn = ObjectAnimator.ofFloat(inView,
                "translationY",
                dpToPx(22) * (-1));
        toIn.setInterpolator(new DecelerateInterpolator(factor));
        toIn.setRepeatCount(0);
        toIn.setDuration(delay);


        // fade out empty view
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(outView, "alpha", 0.0f);
        fadeOut.setDuration(delay);
        fadeOut.setInterpolator(new DecelerateInterpolator(factor));
        fadeOut.start();


        // fade in filled view
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(inView, "alpha", 1.0f);
        fadeIn.setDuration(delay);
        fadeIn.setInterpolator(new DecelerateInterpolator(factor));
        fadeIn.start();

        AnimatorSet set = new AnimatorSet();
        set.play(toOut);
        set.play(toIn);
        set.play(fadeIn);
        set.play(fadeOut);
        set.start();


    }

    private int dpToPx(int dp) {

        int px = Math.round(dp * getPixelScaleFactor(mContext));
        return px;

    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void updateAppServer() {

        if (!isVoted)
            DataServer.votePost(mPostId, new VoteRequestBody(mRate), this);

    }

    @Override
    public void onPostVoted() {
        Toast.makeText(mContext, "You Voted : " + mRate, Toast.LENGTH_LONG).show();
        isVoted = true;
    }

    @Override
    public void onPostVoteError() {
        mRate = 0;
        Toast.makeText(mContext, "Cannot Vote!", Toast.LENGTH_LONG).show();
    }
}
