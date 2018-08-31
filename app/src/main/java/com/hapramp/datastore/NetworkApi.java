package com.hapramp.datastore;

import com.hapramp.preferences.HaprampPreferenceManager;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkApi {
  private static OkHttpClient client;
  private static NetworkApi networkApiInstance;

  public NetworkApi() {
    client = new OkHttpClient();
  }

  public Response fetchUserCommunities(String url) {
    try {
      Request request = new Request.Builder()
        .url(url)
        .build();
      return client.newCall(request).execute();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Request getRequestWithAuthorization() {
    Request request = new Request.Builder()
      .header("Authorization", "Token " + HaprampPreferenceManager.getInstance().getUserToken())
      .build();
    return request;
  }

  public static NetworkApi getNetworkApiInstance() {
    if (networkApiInstance == null) {
      networkApiInstance = new NetworkApi();
    }
    return networkApiInstance;
  }
}
