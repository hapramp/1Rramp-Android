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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 2/21/2018.
 */

public class SteemPostCreator {
  private Handler mHandler;
  private SteemPostCreatorCallback steemPostCreatorCallback;

  public SteemPostCreator() {
    this.mHandler = new Handler();
  }

  @WorkerThread
  public void createPost(final String body, final String title, final List<String> images, final List<String> tags, final String __permlink) {
    if (AccessTokenValidator.isTokenExpired()) {
      if (steemPostCreatorCallback != null) {
        steemPostCreatorCallback.onPostCreationFailedOnSteem("Token Expired! Login Required!");
      }
      return;
    }

    new Thread() {
      @Override
      public void run() {
        final SteemConnect steemConnect = SteemConnectUtils
          .getSteemConnectInstance(HaprampPreferenceManager.getInstance()
            .getSC2AccessToken());
        int percentSteemDollars = HaprampPreferenceManager.getInstance().getPercentSteemDollars();
        String maxAcceptedPayout = HaprampPreferenceManager.getInstance().getMaxAcceptedPayout();
        boolean allowVote = HaprampPreferenceManager.getInstance().getAllowVotes();
        boolean allowCurationRewards = HaprampPreferenceManager.getInstance().getAllowCurationRewards();
        ArrayList<Beneficiary> beneficiaries = new ArrayList<>();
        //add beneficiaries later
        beneficiaries.add(new Beneficiary(LocalConfig.BENEFICIARY_ACCOUNT, LocalConfig.BENEFICIARY_WEIGHT));
        final String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
        String jsonMetadata = new JsonMetadata(tags, images).getJson();
        steemConnect.comment("",
          LocalConfig.PARENT_PERMALINK,
          username,
          __permlink,
          com.hapramp.utils.StringUtils.stringify(title),
          com.hapramp.utils.StringUtils.stringify(body),
          com.hapramp.utils.StringUtils.stringify(jsonMetadata),
          maxAcceptedPayout,
          percentSteemDollars,
          allowVote,
          allowCurationRewards,
          beneficiaries,
          new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              if (steemPostCreatorCallback != null) {
                mHandler.post(new Runnable() {
                  @Override
                  public void run() {
                    steemPostCreatorCallback.onPostCreatedOnSteem();
                    broadcastCommentOptions(username, __permlink, steemConnect);
                  }
                });
              }
            }

            @Override
            public void onError(final SteemConnectException e) {
              if (steemPostCreatorCallback != null) {
                mHandler.post(new Runnable() {
                  @Override
                  public void run() {
                    steemPostCreatorCallback.onPostCreationFailedOnSteem(e.toString());
                  }
                });
              }
            }
          }
        );
      }
    }.start();
  }

  private void broadcastCommentOptions(final String username, final String __permlink, final SteemConnect steemConnect) {
    try {
      new Thread() {
        @Override
        public void run() {

        }
      }.start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setSteemPostCreatorCallback(SteemPostCreatorCallback steemPostCreatorCallback) {
    this.steemPostCreatorCallback = steemPostCreatorCallback;
  }

  public interface SteemPostCreatorCallback {
    void onPostCreatedOnSteem();

    void onPostCreationFailedOnSteem(String msg);
  }

}
