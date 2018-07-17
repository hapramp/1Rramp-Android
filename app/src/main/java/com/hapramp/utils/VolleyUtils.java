package com.hapramp.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Ankit on 4/18/2018.
 */

public class VolleyUtils {

  public static final String TAG = VolleyUtils.class.getSimpleName();
  private static VolleyUtils bInstance;
  Context baseContext;
  private RequestQueue bRequestQueue;

  public static synchronized VolleyUtils getInstance() {

    if (bInstance == null) {

      bInstance = new VolleyUtils();
    }

    return bInstance;
  }

  public <T> void addToRequestQueue(Request<T> req, String tag, Context context) {
    req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    baseContext = context;
    getRequestQueue(context).add(req);

  }

  public RequestQueue getRequestQueue(Context context) {
    if (bRequestQueue == null) {
      bRequestQueue = Volley.newRequestQueue(context);
    }
    return bRequestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req) {
    req.setTag(TAG);
    getRequestQueue(baseContext).add(req);
  }

  public void cancelPendingRequests(Object tag) {
    if (bRequestQueue != null) {
      bRequestQueue.cancelAll(tag);
    }
  }


}
