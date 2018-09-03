package com.hapramp.datastore;

import com.hapramp.preferences.HaprampPreferenceManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkApi {
  private static OkHttpClient client;
  private static NetworkApi networkApiInstance;

  public NetworkApi() {
    client = new OkHttpClient();
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

  public Response fetch(String url) {
    Request request = null;
    try {
      request = new Request.Builder()
        .url(url)
        .build();
      return client.newCall(request).execute();
    }
    catch (Exception e) {
      e.printStackTrace();
      Response response = new Response
        .Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_0)
        .message(e.toString())
        .code(502).build();
      return response;
    }
  }

  public Response postAndFetch(String url, String requestBody) {
    Request request = null;
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(JSON, requestBody);
    try {
      request = new Request.Builder()
        .url(url)
        .post(body)
        .build();
      return client.newCall(request).execute();
    }
    catch (Exception e) {
      e.printStackTrace();
      Response response = new Response
        .Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_0)
        .message(e.toString())
        .code(502).build();
      return response;
    }
  }
}
