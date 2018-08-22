package com.hapramp.api;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.user.User;
import com.hapramp.utils.Constants;
import com.hapramp.utils.JsonParser;
import com.hapramp.utils.VolleyUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RawApiCaller {
  Handler mHandler;
  Context context;
  public static final String HAPRAMP_API_URL = URLS.BASE_URL + "curation/tag/";
  private String STEEMIT_API_URL = "https://api.steemit.com";
  private FeedDataCallback dataCallback;
  private UserMetadataCallback userMetadataCallback;
  private String currentRequestTag = "";

  public RawApiCaller(Context context) {
    this.context = context;
    mHandler = new Handler();
  }

  //for search page [Posts with hapramp tag]
  public void requestLatestPostsByTag(final String tag) {
    final String rtag = "latest_post_by_tag_" + tag;
    this.currentRequestTag = rtag;
    final String reqBody = "{\"jsonrpc\":\"2.0\", \"method\":\"condenser_api.get_discussions_by_created\", \"params\":[{\"tag\":\"" + tag + "\",\"limit\":40}], \"id\":1}";
    StringRequest newBlogRequest = new StringRequest(Request.Method.POST, STEEMIT_API_URL, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        parseLatestPostByTag(response, rtag);
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

  private void parseLatestPostByTag(final String response, final String tag) {
    final JsonParser jsonParser = new JsonParser();
    new Thread() {
      @Override
      public void run() {
        final ArrayList<Feed> feeds = jsonParser.parseFeedStructure3(response);
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            if (isRequestLive(tag))
              if (dataCallback != null) {
                dataCallback.onDataLoaded(feeds, false);
              }
          }
        });
      }
    }.start();
  }

  private void returnErrorCallback() {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        if (dataCallback != null) {
          dataCallback.onDataLoadError();
        }
      }
    });
  }

  private boolean isRequestLive(String requestTag) {
    return this.currentRequestTag.equals(requestTag);
  }

  public void requestUserBlogs(String username) {
    final String rtag = "user_blog_" + username;
    this.currentRequestTag = rtag;
    String url = URLS.BASE_URL + "feeds/blog/" + username;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        parseUserFeed(response, rtag, false);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        returnErrorCallback();
      }
    });
    VolleyUtils.getInstance().addToRequestQueue(stringRequest, rtag, context);
  }

  private void parseUserFeed(final String response, final String currentRequestTag, final boolean appendableData) {
    final JsonParser jsonParser = new JsonParser();
    new Thread() {
      @Override
      public void run() {
        final ArrayList<Feed> feeds = jsonParser.parseFeedStructure2(response);
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            if (isRequestLive(currentRequestTag))
              if (dataCallback != null) {
                dataCallback.onDataLoaded(feeds, appendableData);
              }
          }
        });
      }
    }.start();
  }

  public void requestUserFeed(String username) {
    final String rtag = "user_feed_" + username;
    this.currentRequestTag = rtag;
    String url = URLS.BASE_URL + "feeds/user/" + username + "?limit=" + Constants.FEED_LOADING_LIMIT;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          parseUserFeed(response, rtag, false);
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        returnErrorCallback();
      }
    });
    VolleyUtils.getInstance().addToRequestQueue(stringRequest, "user_feed_steemit", context);
  }

  public void requestMoreUserFeed(String username, final String start_author, final String start_permlink) {
    final String rtag = "more_user_feed_" + username;
    this.currentRequestTag = rtag;
    String url = URLS.BASE_URL + "feeds/user/" + username +
      "?limit=" + Constants.FEED_LOADING_LIMIT +
      "&start_author=" + start_author +
      "&start_permlink=" + start_permlink;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          parseUserFeed(response, rtag, true);
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {
        returnErrorCallback();
      }
    });
    VolleyUtils.getInstance().addToRequestQueue(stringRequest, "more_user_feed_server", context);
  }

  public void requestUserMetadata(Context context, String username) {
    this.currentRequestTag = "user_metadata";
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

  public void requestCuratedFeedsByTag(final String tag) {
    final String rtag = "curate_by_tag_" + tag;
    this.currentRequestTag = rtag;
    String url = HAPRAMP_API_URL + tag;
    StringRequest curatedFeedRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        parseCuratedFeedResponseOnWorkerThread(response, rtag);
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        returnErrorCallback();
      }
    });
    VolleyUtils.getInstance().addToRequestQueue(curatedFeedRequest, currentRequestTag, context);
  }

  public interface UserMetadataCallback {
    void onUserMetadataLoaded(User user);
  }

  private void parseCuratedFeedResponseOnWorkerThread(final String response, final String requestTag) {
    final JsonParser jsonParser = new JsonParser();
    new Thread() {
      @Override
      public void run() {
        final ArrayList<Feed> feeds = jsonParser.parseCuratedFeed(response);
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            if (isRequestLive(requestTag)) {
              if (dataCallback != null) {
                dataCallback.onDataLoaded(feeds, false);
              }
            }
          }
        });
      }
    }.start();
  }

  public interface FeedDataCallback {
    void onDataLoaded(ArrayList<Feed> feeds, boolean appendableData);
    void onDataLoadError();
  }
}
