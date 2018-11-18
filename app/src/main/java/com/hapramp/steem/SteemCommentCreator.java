package com.hapramp.steem;

import android.os.Handler;
import android.support.annotation.WorkerThread;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.JsonMetadata;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.Beneficiary;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.utils.AccessTokenValidator;
import com.hapramp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 4/15/2018.
 */

public class SteemCommentCreator {

  private Handler mHandler;
  private SteemCommentCreateCallback steemCommentCreateCallback;
  private Runnable commentCreatedRunnable = new Runnable() {
    @Override
    public void run() {
      if (steemCommentCreateCallback != null) {
        steemCommentCreateCallback.onCommentCreated();
      }
    }
  };
  private Runnable commentCreateFailedRunnable = new Runnable() {
    @Override
    public void run() {
      if (steemCommentCreateCallback != null) {
        steemCommentCreateCallback.onCommentCreateFailed();
      }
    }
  };

  public SteemCommentCreator() {
    this.mHandler = new Handler();
  }
  @WorkerThread
  public void createComment(final String comment, final String commentOnUser, final String parentPermlink) {
    if (steemCommentCreateCallback != null) {
      steemCommentCreateCallback.onCommentCreateProcessing();
    }

    if (AccessTokenValidator.isTokenExpired()) {
      if (steemCommentCreateCallback != null) {
        steemCommentCreateCallback.onCommentCreateFailed();
      }
      return;
    }

    new Thread() {
      @Override
      public void run() {
        List<String> tag = new ArrayList<>();
        tag.add("1ramp");
        String jsonMetadata = new JsonMetadata(tag, null).getJson();
        SteemConnect steemConnect = SteemConnectUtils
          .getSteemConnectInstance(HaprampPreferenceManager.getInstance()
            .getSC2AccessToken());
        int percentSteemDollars = HaprampPreferenceManager.getInstance().getPercentSteemDollars();
        String maxAcceptedPayout = HaprampPreferenceManager.getInstance().getMaxAcceptedPayout();
        boolean allowVote = HaprampPreferenceManager.getInstance().getAllowVotes();
        boolean allowCurationRewards = HaprampPreferenceManager.getInstance().getAllowCurationRewards();
        ArrayList<Beneficiary> beneficiaries = new ArrayList<>();
        beneficiaries.add(new Beneficiary(LocalConfig.BENEFICIARY_ACCOUNT, LocalConfig.BENEFICIARY_WEIGHT));
        String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
        String __permlink = PermlinkGenerator.getPermlink();
        steemConnect.comment(commentOnUser,
          parentPermlink,
          username,
          __permlink,
          "",
          com.hapramp.utils.StringUtils.stringify(comment),
          StringUtils.stringify(jsonMetadata),
          maxAcceptedPayout,
          percentSteemDollars,
          allowVote,
          allowCurationRewards,
          beneficiaries,
          new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              mHandler.post(commentCreatedRunnable);
            }

            @Override
            public void onError(SteemConnectException e) {
              mHandler.post(commentCreateFailedRunnable);
            }
          });
      }
    }.start();
  }

  public void setSteemCommentCreateCallback(SteemCommentCreateCallback steemCommentCreateCallback) {
    this.steemCommentCreateCallback = steemCommentCreateCallback;
  }

  public interface SteemCommentCreateCallback {
    void onCommentCreateProcessing();

    void onCommentCreated();

    void onCommentCreateFailed();
  }
}

