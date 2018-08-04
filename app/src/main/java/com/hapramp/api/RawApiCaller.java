package com.hapramp.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.user.User;
import com.hapramp.utils.JsonParser;
import com.hapramp.utils.VolleyUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RawApiCaller {
  Handler mHandler;
  Context context;
  private FeedDataCallback dataCallback;
  private UserMetadataCallback userMetadataCallback;

  public void requestNewFeeds(Context context, String tag) {
    mHandler = new Handler();
    this.context = context;
    final String reqBody = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_discussions_by_created\", \"params\":[{\"" + tag + "\":\"art\",\"limit\":100}], \"id\":1}";
    putNetworkRequest(reqBody, "new");
  }

  public void requestNewFeeds(Context context) {
    mHandler = new Handler();
    this.context = context;
    final String reqBody = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_discussions_by_created\", \"params\":[{\"tag\":\"art\",\"limit\":100}], \"id\":1}";
    putNetworkRequest(reqBody, "new");
  }

  private void putNetworkRequest(final String reqBody, String tag) {
    String url = "https://api.steemit.com";
    StringRequest newBlogRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        parseFeedResponseOnWorkerThread(response);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {
      @Override
      public byte[] getBody() throws AuthFailureError {
        try {
          return reqBody.getBytes("utf-8");
        }
        catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        return null;
      }
    };

    VolleyUtils.getInstance().addToRequestQueue(newBlogRequest, tag, context);
  }

  private void parseFeedResponseOnWorkerThread(final String response) {
    final JsonParser jsonParser = new JsonParser();
    new Thread() {
      @Override
      public void run() {
        final ArrayList<Feed> feeds = jsonParser.parseFeed(response);
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            if (dataCallback != null) {
              dataCallback.onDataLoaded(feeds);
            }
          }
        });
      }
    }.start();
  }

  public void requestTrendingFeeds(Context context) {
    mHandler = new Handler();
    this.context = context;
    final String reqBody = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_discussions_by_trending\", \"params\":[{\"tag\":\"steem\",\"limit\":100}], \"id\":1}";
    putNetworkRequest(reqBody, "trending");
  }

  public void requestHotFeeds(Context context) {
    mHandler = new Handler();
    this.context = context;
    final String reqBody = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_discussions_by_hot\", \"params\":[{\"tag\":\"steem\",\"limit\":100}], \"id\":1}";
    putNetworkRequest(reqBody, "hot");
  }

  public void requestUserMetadata(Context context, String username) {
    mHandler = new Handler();
    this.context = context;
    String url = String.format("https://steemit.com/@%s.json", username);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String s) {
        parseUserMetaResponseOnWorkerThread(s);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {

      }
    });
    VolleyUtils.getInstance().addToRequestQueue(stringRequest, "user_metadata", context);
  }

  private void parseUserMetaResponseOnWorkerThread(final String userJson) {
    new Thread() {
      @Override
      public void run() {
        final JsonParser jsonParser = new JsonParser();
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            User user = jsonParser.parseUser(userJson);
            if (userMetadataCallback != null) {
              userMetadataCallback.onUserMetadataLoaded(user);
            }
          }
        });
      }
    }.start();
  }

  public void setDataCallback(FeedDataCallback dataCallback) {
    this.dataCallback = dataCallback;
  }

  public void setUserMetadataCallback(UserMetadataCallback userMetadataCallback) {
    this.userMetadataCallback = userMetadataCallback;
  }

  public interface UserMetadataCallback {
    void onUserMetadataLoaded(User user);
  }

  public interface FeedDataCallback {
    void onDataLoaded(ArrayList<Feed> feeds);
  }
}
