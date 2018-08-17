package com.hapramp.api;

import android.content.Context;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hapramp.utils.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import hapramp.walletinfo.NetworkUtils;

public class FollowingApi {
  private Context context;
  private Handler mHandler;
  private String STEEMIT_API_URL = "https://api.steemit.com";
  private FollowingCallback followingCallback;
  private NetworkUtils networkUtils;
  private String currentRequestTag;

  public FollowingApi(Context context) {
    this.context = context;
    this.mHandler = new Handler();
    networkUtils = new NetworkUtils();
  }

  public void setFollowingCallback(FollowingCallback followingCallback) {
    this.followingCallback = followingCallback;
  }


  public void requestFollowings(final String username, final String startFromUser) {
    final String rtag = "following_" + username + "_" + startFromUser;
    this.currentRequestTag = rtag;
    String startUser = startFromUser == null ? null : "\"" + startFromUser + "\"";
    final String reqBody = "{\"jsonrpc\":\"2.0\"," +
      " \"method\":\"condenser_api.get_following\"," +
      " \"params\":[\"" + username + "\"," + startUser + ",\"blog\",20], \"id\":1}";
    StringRequest newBlogRequest = new StringRequest(Request.Method.POST, STEEMIT_API_URL, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        parseFollowings(response, rtag);
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

  private void parseFollowings(String response, String rtag) {
    ArrayList<String> followings = new ArrayList<>();
    try {
      JSONArray results = new JSONObject(response).getJSONArray("result");
      for (int i = 0; i < results.length(); i++) {
        followings.add(results.getJSONObject(i).getString("following"));
      }
    }
    catch (JSONException e) {

    }
    if (isRequestLive(rtag)) {
      if (followingCallback != null) {
        followingCallback.onFollowings(followings);
      }
    }
  }

  private void returnErrorCallback() {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        if (followingCallback != null) {
          followingCallback.onError();
        }
      }
    });
  }

  private boolean isRequestLive(String requestTag) {
    return this.currentRequestTag.equals(requestTag);
  }


  public interface FollowingCallback {
    void onFollowings(ArrayList<String> followings);

    void onError();
  }

}
