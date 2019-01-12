package com.hapramp.views;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Voter;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.utils.VoteUtils;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SliderView extends FrameLayout {
  @BindView(R.id.bubbleSeekbar)
  BubbleSeekBar bubbleSeekbar;
  @BindView(R.id.done_btn)
  TextView doneBtn;
  @BindView(R.id.rating_progress)
  ProgressBar ratingProgress;
  @BindView(R.id.done_btn_container)
  RelativeLayout doneBtnContainer;
  @BindView(R.id.rate_slider_container)
  RelativeLayout rateSliderContainer;
  private Context context;
  private boolean amIVoted;
  private long myVote;
  private String permlink;
  private float newRateFromUser = 0;
  private SteemConnect steemConnect;
  private String author;
  private Handler mHandler;
  private Runnable steemCastingVoteExceptionRunnable = new Runnable() {
    @Override
    public void run() {
      //update view
      if (doneBtnContainer != null) {
        doneBtnContainer.setVisibility(GONE);
      }
    }
  };
  private OnVoteChangedFromSlider onVoteChangedFromSlider;

  public SliderView(@NonNull Context context) {
    this(context, null);
  }

  public SliderView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SliderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    View view = LayoutInflater.from(context).inflate(R.layout.rating_slider_view, this);
    ButterKnife.bind(this, view);
    steemConnect = SteemConnectUtils.getSteemConnectInstance(HaprampPreferenceManager
      .getInstance().getSC2AccessToken());
    mHandler = new Handler();
    attachListeners();
  }

  private void attachListeners() {
    bubbleSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
      @Override
      public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
        int[] location = new int[2];
        bubbleSeekBar.getLocationOnScreen(location);
      }

      @Override
      public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

      }

      @Override
      public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
        if (fromUser) {
          newRateFromUser = progressFloat;
        }
      }
    });

    doneBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        updateVoteToBlockchain();
      }
    });
  }

  private void updateVoteToBlockchain() {
    try {
      doneBtn.setVisibility(GONE);
      ratingProgress.setVisibility(VISIBLE);
      performVoteOnSteem(getVoteScaledTo10000(newRateFromUser));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void performVoteOnSteem(final int vote) {
    new Thread() {
      @Override
      public void run() {
        steemConnect.vote(
          HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
          author,
          permlink,
          String.valueOf(vote),
          new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  if (onVoteChangedFromSlider != null) {
                    onVoteChangedFromSlider.onVoteChanged();
                    if (doneBtnContainer != null) {
                      doneBtnContainer.setVisibility(GONE);
                    }
                  }
                }
              });
            }

            @Override
            public void onError(SteemConnectException e) {
              mHandler.post(steemCastingVoteExceptionRunnable);
            }
          });
      }
    }.start();
  }

  private int getVoteScaledTo10000(float sliderValue) {
    return (int) (sliderValue * 100);
  }

  public void correctOffsetWhenContainerOnScrolling() {
    bubbleSeekbar.correctOffsetWhenContainerOnScrolling();
  }

  public void setVoteInfo(ArrayList<Voter> voters, String permlink, String author) {
    this.permlink = permlink;
    this.author = author;
    amIVoted = VoteUtils.checkForMyVote(voters);
    myVote = VoteUtils.getMyVotePercent(voters);
    ratingProgress.setVisibility(GONE);
    doneBtnContainer.setVisibility(GONE);
    if (amIVoted) {
      bubbleSeekbar.setProgress(getVoteScaledTo100(myVote));
    }
  }

  private float getVoteScaledTo100(long vote) {
    float lv = vote;
    return lv / 100;
  }

  public void setOnVoteChangedFromSlider(OnVoteChangedFromSlider onVoteChangedFromSlider) {
    this.onVoteChangedFromSlider = onVoteChangedFromSlider;
  }

  public interface OnVoteChangedFromSlider {
    void onVoteChanged();
  }
}
