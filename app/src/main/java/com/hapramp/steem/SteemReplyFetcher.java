package com.hapramp.steem;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.WorkerThread;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hapramp.api.URLS;
import com.hapramp.models.CommentModel;
import com.hapramp.utils.JsonParser;
import com.hapramp.utils.VolleyUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ankit on 4/15/2018.
 */

public class SteemReplyFetcher {
  private final Context context;
  private Handler mHandler;
  private JsonParser jsonParser;
  private SteemReplyFetchCallback steemReplyFetchCallback;
  private Runnable replyFetchFailedCallback = new Runnable() {
    @Override
    public void run() {
      if (steemReplyFetchCallback != null) {
        steemReplyFetchCallback.onReplyFetchError();
      }
    }
  };
  private String currentRequestTag;

  public SteemReplyFetcher(Context context) {
    this.context = context;
    jsonParser = new JsonParser();
    this.mHandler = new Handler();
  }

  @WorkerThread
  public void requestReplyForPost(final String authorOfPost, final String permlink) {
    final String rtag = "reply_" + authorOfPost + permlink;
    currentRequestTag = rtag;
    final String reqBody = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_content_replies\"," +
      " \"params\":[\"" + authorOfPost + "\", \"" + permlink + "\"], \"id\":1}";
    StringRequest newBlogRequest = new StringRequest(Request.Method.POST, URLS.STEEMIT_API_URL, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        parseReplies(response, rtag);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        returnErrorCallback();
      }
    }) {
      @Override
      public byte[] getBody() {
        try {
          return reqBody.getBytes("utf-8");
        }
        catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        return null;
      }
    };
    VolleyUtils.getInstance().addToRequestQueue(newBlogRequest, currentRequestTag, context);
  }

  private void parseReplies(String response, String rtag) {
    final ArrayList<CommentModel> comments = jsonParser.parseComments(response);
    if (isRequestLive(rtag)) {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          if (steemReplyFetchCallback != null) {
            steemReplyFetchCallback.onReplyFetched(comments);
          }
        }
      });
    }
  }

  private void returnErrorCallback() {
    mHandler.post(replyFetchFailedCallback);
  }

  private boolean isRequestLive(String requestTag) {
    return this.currentRequestTag.equals(requestTag);
  }

  public void setSteemReplyFetchCallback(SteemReplyFetchCallback steemReplyFetchCallback) {
    this.steemReplyFetchCallback = steemReplyFetchCallback;
  }

  public interface SteemReplyFetchCallback {
    void onReplyFetched(List<CommentModel> replies);
    void onReplyFetchError();
  }
}
