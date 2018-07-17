package com.hapramp.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Ankit on 4/18/2018.
 */

public class YoutubeSuggestionsHelper {

  private static Context context;
  private static YoutubeSuggestionsHelper mInstance;
  private SuggestionsCallback suggestionsCallback;

  public YoutubeSuggestionsHelper(Context context) {
    YoutubeSuggestionsHelper.context = context;
  }

  public static YoutubeSuggestionsHelper getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new YoutubeSuggestionsHelper(context);
    }
    return mInstance;
  }

  public void findSuggestion(String query) {

    //cancel previous calls
    VolleyUtils.getInstance().cancelPendingRequests(query);

    if (suggestionsCallback != null) {
      suggestionsCallback.onFetching();
    }

    String url = "http://suggestqueries.google.com/complete/search?q=" + URLEncoder.encode(query) + "&client=firefox&hl=en&ds=yt";

    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        //log("response "+response);
        ArrayList<String> suggestionsList = new ArrayList<>();
        try {
          JSONArray jsonArray = new JSONArray(response);
          //JSONArray suggestions = new JSONArray();
          String suggestionsArr = jsonArray.get(1).toString();
          JSONArray sarr = new JSONArray(suggestionsArr);
          for (int i = 0; i < sarr.length(); i++) {
            suggestionsList.add(sarr.get(i).toString());
          }

          if (suggestionsCallback != null) {
            suggestionsCallback.onSuggestionsFetched(suggestionsList);
          }


        }
        catch (JSONException e) {
          e.printStackTrace();
        }


      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError volleyError) {

      }
    });

    //set tag as term
    VolleyUtils.getInstance().addToRequestQueue(request, query, context);

  }

  public void setSuggestionsCallback(SuggestionsCallback suggestionsCallback) {
    this.suggestionsCallback = suggestionsCallback;
  }

  public interface SuggestionsCallback {
    void onFetching();

    void onSuggestionsFetched(ArrayList<String> suggestions);
  }

}
