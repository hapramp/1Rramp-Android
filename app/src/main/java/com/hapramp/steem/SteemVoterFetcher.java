package com.hapramp.steem;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.WorkerThread;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hapramp.api.URLS;
import com.hapramp.steem.models.Voter;
import com.hapramp.utils.JsonParser;
import com.hapramp.utils.VolleyUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SteemVoterFetcher {
  private final Context context;
  private Handler mHandler;
  private JsonParser jsonParser;
  private Runnable voterFetchFailedCallback = new Runnable() {
    @Override
    public void run() {
      if (steemVoterFetchCallback != null) {
        steemVoterFetchCallback.onVotersFetchError();
      }
    }
  };
  private String currentRequestTag;
  private SteemVotersFetchCallback steemVoterFetchCallback;

  public SteemVoterFetcher(Context context) {
    this.context = context;
    jsonParser = new JsonParser();
    this.mHandler = new Handler();
  }

  @WorkerThread
  public void requestVoters(final String authorOfPost, final String permlink) {
    final String rtag = "voters_" + authorOfPost + permlink;
    currentRequestTag = rtag;
    final String reqBody = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_active_votes\"," +
      " \"params\":[\""+authorOfPost+"\"," +
      " \""+permlink+"\"]," +
      " \"id\":1}";
    StringRequest newBlogRequest = new StringRequest(Request.Method.POST, URLS.STEEMIT_API_URL, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        parseVoters(response, rtag);
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

  private void parseVoters(String response, String rtag) {
    final ArrayList<Voter> comments = jsonParser.parseVoters(response);
    if (isRequestLive(rtag)) {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          if (steemVoterFetchCallback != null) {
            steemVoterFetchCallback.onVotersFetched(comments);
          }
        }
      });
    }
  }

  private void returnErrorCallback() {
    mHandler.post(voterFetchFailedCallback);
  }

  private boolean isRequestLive(String requestTag) {
    return this.currentRequestTag.equals(requestTag);
  }

  public void setSteemVoterFetchCallback(SteemVotersFetchCallback steemVoterFetchCallback) {
    this.steemVoterFetchCallback = steemVoterFetchCallback;
  }

  public interface SteemVotersFetchCallback {
    void onVotersFetched(List<Voter> voters);
    void onVotersFetchError();
  }
}
