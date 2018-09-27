package com.hapramp.views.extraa;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.views.CustomRatingBar;

/**
 * Created by Ankit on 12/14/2017.
 */

public class StarView extends FrameLayout {
  private static final long REVEAL_DELAY = 500;
  private static final float REVEAL_START_OFFSET = 84;
  private static final long HIDE_RATING_BAR_DELAY = 4000;
  ImageView starIndicator;
  TextView rateLabel;
  CustomRatingBar ratingBar;
  RelativeLayout ratingBarContainer;
  TextView cancelRateBtn;
  TextView ratingError;
  ProgressBar ratingProgress;
  private Context mContext;
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
    rateLabel = v.findViewById(R.id.rateLabel);
    ratingBar = v.findViewById(R.id.ratingBar);
    ratingError = v.findViewById(R.id.ratingError);
    ratingProgress = v.findViewById(R.id.ratingProgress);
    ratingBarContainer = v.findViewById(R.id.ratingBarContainer);
    cancelRateBtn = v.findViewById(R.id.cancelBtn);
    ratingError.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
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

    starIndicator.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (ConnectionUtils.isConnected(mContext)) {
          onStarIndicatorTapped();
        } else {
          Toast.makeText(mContext, "Check Network Connection", Toast.LENGTH_LONG).show();
        }
      }
    });

    starIndicator.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View view) {
        if (ConnectionUtils.isConnected(mContext)) {
          onStarIndicatorLongPressed();
        } else {
          Toast.makeText(mContext, "Check Network Connection", Toast.LENGTH_LONG).show();
        }
        return true;
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

  public void onStarIndicatorTapped() {
    if (currentState.iHaveVoted) {
      cancelMyRating();
    } else {
      setNewRating(5);
      sendVoteToSteem();
    }
  }

  private void setCurrentState(Vote voteState) {
    this.currentState = voteState;
    setStarIndicatorState(currentState.iHaveVoted);
    if (voteState.iHaveVoted) {
      ratingBar.setRating(getMappedRatingFromPercent(currentState.myVotePercent));
    } else {
      ratingBar.setRating(0);
    }
  }

  private void setStarIndicatorState(boolean isRated) {
    if (isRated) {
      starIndicator.setImageResource(R.drawable.star_filled);
      rateLabel.setText("Rated");
    } else {
      starIndicator.setImageResource(R.drawable.star);
      rateLabel.setText("Rate");
    }
    if (onVoteUpdateCallback != null) {
      onVoteUpdateCallback.onVoteDescription(getRatingDescriptionWithAverage());
    }
  }

  private float getMappedRatingFromPercent(float myVotePercent) {
    return myVotePercent / 2000;
  }

  private String getRatingDescriptionWithAverage() {
    float voteSum = getVoteSumFromVotePercentSum(currentState.getTotalVotePercentSum());
    int _totalUser = (int) currentState.getTotalVotedUsers();
    return _totalUser > 0 ? String.format(mContext.getResources().getString(R.string.star_info),
      ((voteSum) / _totalUser), _totalUser) : "";
  }

  // This method returns string with average and vote count
  private float getVoteSumFromVotePercentSum(float totalVotePercentSum) {
    return totalVotePercentSum / 2000;
  }

  public void setOnVoteUpdateCallback(StarView.onVoteUpdateCallback onVoteUpdateCallback) {
    this.onVoteUpdateCallback = onVoteUpdateCallback;
  }

  private float getRating() {
    return ratingBar.getRating();
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

  private void showRatingBar() {
    revealRatingBar();
    mHandler.postDelayed(autoHideRatingBarRunnable, HIDE_RATING_BAR_DELAY);
  }

  private void showRatingCancelButton() {
    cancelRateBtn.setVisibility(VISIBLE);
  }

  private void removeRatingCancelButton() {
    cancelRateBtn.setVisibility(GONE);
  }

  private void revealRatingBar() {
    ratingBarContainer.setVisibility(View.VISIBLE);
  }

  private void setNewRating(float rating) {
    mHandler.removeCallbacks(autoHideRatingBarRunnable);
    mHandler.postDelayed(autoHideRatingBarRunnable, HIDE_RATING_BAR_DELAY / 2);
    if (currentState.iHaveVoted) {
      cancelMyRating();
    }
    currentState.setiHaveVoted(true);
    currentState.setMyVotePercent(getVotePercentFromRating(rating));
    currentState.totalVotedUsers += 1;
    currentState.totalVotePercentSum += getVotePercentFromRating(rating);
    setCurrentState(currentState);
  }

  private void cancelMyRating() {
    currentState.setiHaveVoted(false);
    currentState.totalVotedUsers -= 1;
    currentState.totalVotePercentSum -= currentState.myVotePercent;
    currentState.myVotePercent = 0;
    ratingBar.setRating(0);
    setCurrentState(currentState);
    deleteVoteFromSteem();
  }

  private void hideRatingBar() {
    collapseRatingBar();
  }

  private void collapseRatingBar() {
    ratingBarContainer.setVisibility(View.GONE);
  }

  private void setRatingBarListener() {

    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
      @Override
      public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (fromUser) {
          setNewRating(rating);
          sendVoteToSteem();
        }
      }
    });

  }

  private float getVotePercentFromRating(float rating) {
    return rating * 2000;
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

  private void showRatingProgress(boolean show) {
    if (show) {
      //hide text
      rateLabel.setVisibility(GONE);
      //show progress
      ratingProgress.setVisibility(VISIBLE);
    } else {
      //show text
      rateLabel.setVisibility(VISIBLE);
      //hide progress
      ratingProgress.setVisibility(GONE);
    }
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

  public void castedVoteTemporarily() {
    showRatingProgress(false);
  }

  public void deletedVoteTemporarily() {
    showRatingProgress(false);
  }

  public void castedVoteSuccessfully() {
    this.legacyState = currentState;
    showRatingProgress(false);
  }

  public void deletedVoteSuccessfully() {
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

  private void toast(String msg) {
    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
  }

  public String getVoteDescription() {
    return getRatingDescriptionWithAverage();
  }

  public interface onVoteUpdateCallback {

    void onVoted(String permlink, int vote);

    void onVoteDeleted(String permlink);

    void onVoteDescription(String msg);
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

}
