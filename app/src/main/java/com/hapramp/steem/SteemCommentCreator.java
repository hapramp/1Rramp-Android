package com.hapramp.steem;

import android.os.Handler;
import android.support.annotation.WorkerThread;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.push.Notifyer;
import com.hapramp.steem.models.data.JsonMetadata;
import com.hapramp.steemconnect.SteemConnectUtils;
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
        tag.add("hapramp");
        String jsonMetadata = new JsonMetadata(tag, null).getJson();
        SteemConnect steemConnect = SteemConnectUtils
          .getSteemConnectInstance(HaprampPreferenceManager.getInstance()
            .getSC2AccessToken());
        String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
        String __permlink = PermlinkGenerator.getPermlink();
        steemConnect.comment(commentOnUser,
          parentPermlink,
          username,
          __permlink,
          "",
          com.hapramp.utils.StringUtils.stringify(comment),
          StringUtils.stringify(jsonMetadata),
          new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              Notifyer.notifyComment(parentPermlink);
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

