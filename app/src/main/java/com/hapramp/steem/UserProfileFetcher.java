package com.hapramp.steem;

import android.os.Handler;

import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.user.User;
import org.json.JSONException;
import org.json.JSONObject;

import hapramp.walletinfo.NetworkUtils;

public class UserProfileFetcher {
  NetworkUtils networkUtils;
  android.os.Handler mHandler;
  private UserProfileFetchCallback userProfileFetchCallback;
  NetworkUtils.NetworkResponseCallback networkResponseCallback = new NetworkUtils.NetworkResponseCallback() {
    @Override
    public void onResponse(String response) {
      parseUserJsonAndRespond(response);
    }

    @Override
    public void onError(String e) {

    }
  };

  public UserProfileFetcher() {
    networkUtils = new NetworkUtils();
    mHandler = new Handler();
  }

  public void fetchUserProfileFor(final String username) {
    new Thread() {
      @Override
      public void run() {
        String url = "https://steemit.com/@" + username + ".json";
        networkUtils.request(url, "GET", "");
        networkUtils.setNetworkResponseCallback(networkResponseCallback);
      }
    }.start();
  }

  private void parseUserJsonAndRespond(String response) {
    final User user = new User();
    try {
      JSONObject root = new JSONObject(response);
      JSONObject userObj = root.getJSONObject("user");
      user.setUsername(userObj.optString("name"));
      user.setReputation(userObj.optLong("reputation"));
      user.setPostCount(userObj.optInt("post_count", 0));
      HaprampPreferenceManager.getInstance().setLastPostCreatedAt(userObj.getString("last_root_post"));
      //try getting json_metadata
      JSONObject json_metadataObj = getJsonMetadataObject(userObj.get("json_metadata"));
      if (json_metadataObj != null && (json_metadataObj).has("profile")) {
        JSONObject profileObj = (json_metadataObj).getJSONObject("profile");
        user.setAbout(profileObj.optString("about", ""));
        user.setProfile_image(profileObj.optString("profile_image", ""));
        user.setLocation(profileObj.optString("location", ""));
        user.setFullname(profileObj.optString("name", ""));
        user.setCover_image(profileObj.optString("cover_image", ""));
        } else {
        user.setAbout("");
        user.setCover_image("");
        user.setFullname("");
        user.setLocation("");
        user.setProfile_image("");
      }
    }catch (final JSONException e) {
      if (userProfileFetchCallback != null) {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            userProfileFetchCallback.onUserFetchError(e.toString());
          }
        });
      }
      return;
    }
    if (userProfileFetchCallback != null) {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          userProfileFetchCallback.onUserFetched(user);
        }
      });
    }
  }

  private JSONObject getJsonMetadataObject(Object obj) throws JSONException {
    if (obj instanceof String) {
      if (((String) obj).length() == 0) {
        return null;
      } else {
        return new JSONObject((String) obj);
      }
    } else {
      return (JSONObject) obj;
    }
  }

  public void setUserProfileFetchCallback(UserProfileFetchCallback userProfileFetchCallback) {
    this.userProfileFetchCallback = userProfileFetchCallback;
  }

  public interface UserProfileFetchCallback {
    void onUserFetched(User user);

    void onUserFetchError(String e);
  }

}
