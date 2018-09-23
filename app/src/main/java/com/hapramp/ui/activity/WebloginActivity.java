package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hapramp.R;
import com.hapramp.api.URLS;
import com.hapramp.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebloginActivity extends AppCompatActivity {

  private WebView webView;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_weblogin);
    webView = findViewById(R.id.webview);
    String loginUrl = getIntent().getStringExtra(Constants.EXTRA_LOGIN_URL);
    initProgressDialog();
    initWebView(loginUrl);
  }

  private void initProgressDialog() {
    progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
    progressDialog.setIndeterminate(true);
    progressDialog.setMax(100);
    progressDialog.setMessage("Loading...");
    progressDialog.show();
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  private void initWebView(final String loginUrl) {
    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setDomStorageEnabled(true);
    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
          progressDialog.dismiss();
        }
      }
    });
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
          //check for redirect url
          if (url.contains(URLS.HAPRAMP_REDIRECT_URL)) {
            Map<String, String> urlMap = splitQuery(new URL(url));
            if (urlMap.containsKey(Constants.EXTRA_ACCESS_TOKEN)) {
              sendBackResult(urlMap.get(Constants.EXTRA_USERNAME), urlMap.get(Constants.EXTRA_ACCESS_TOKEN));
            } else {
              sendBackError();
            }
          }
        }
        catch (UnsupportedEncodingException e) {
          e.printStackTrace();
          sendBackError();
        }
        catch (MalformedURLException e) {
          e.printStackTrace();
          sendBackError();
        }
        return false;
      }
    });
    webView.loadUrl(loginUrl);
  }

  public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
    if (url != null) {
      String query = url.getQuery();
      if (query != null) {
        String[] pairs = query.split("&");
        for (String pair : pairs) {
          int idx = pair.indexOf("=");
          query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
            URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
      }
    }
    return query_pairs;
  }

  private void sendBackResult(String username, String token) {
    Intent intent = new Intent();
    intent.putExtra(Constants.EXTRA_USERNAME, username);
    intent.putExtra(Constants.EXTRA_ACCESS_TOKEN, token);
    setResult(RESULT_OK, intent);
    finish();
  }

  private void sendBackError() {
    Intent intent = new Intent();
    setResult(RESULT_CANCELED, intent);
    finish();
  }
}
