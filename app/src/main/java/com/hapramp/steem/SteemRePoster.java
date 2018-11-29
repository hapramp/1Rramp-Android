package com.hapramp.steem;

import android.os.Handler;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;

public class SteemRePoster {
  private Handler mHandler = new Handler();
  private RepostCallback repostCallback;

  public void repost(final String author, final String permlink) {
    new Thread() {
      @Override
      public void run() {
        final SteemConnect steemConnect = SteemConnectUtils
          .getSteemConnectInstance(HaprampPreferenceManager.getInstance()
            .getSC2AccessToken());
        String account = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
        steemConnect.reblog(account, author, permlink, new SteemConnectCallback() {
          @Override
          public void onResponse(String s) {
            if (repostCallback != null) {
              mHandler.post(
                new Runnable() {
                  @Override
                  public void run() {
                    repostCallback.reposted();
                  }
                }
              );
            }
          }

          @Override
          public void onError(SteemConnectException e) {
            if (repostCallback != null) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  repostCallback.failedToRepost();
                }
              });
            }
          }
        });
      }
    }.start();
  }

  public void setRepostCallback(RepostCallback repostCallback) {
    this.repostCallback = repostCallback;
  }

  public interface RepostCallback {
    void reposted();

    void failedToRepost();
  }
}
