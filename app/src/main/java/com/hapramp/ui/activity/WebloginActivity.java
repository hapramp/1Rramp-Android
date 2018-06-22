package com.hapramp.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hapramp.R;
import com.hapramp.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebloginActivity extends AppCompatActivity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_weblogin);
				WebView webView = findViewById(R.id.webview);
				String loginUrl = getIntent().getStringExtra(Constants.EXTRA_LOGIN_URL);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setDomStorageEnabled(true);
				webView.setWebViewClient(new WebViewClient() {
						@Override
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
								try {
										Map<String, String> urlMap = splitQuery(new URL(url));
										if (urlMap.containsKey(Constants.EXTRA_ACCESS_TOKEN)) {
												sendBackResult(urlMap.get(Constants.EXTRA_USERNAME), urlMap.get(Constants.EXTRA_ACCESS_TOKEN));
										}
								}
								catch (UnsupportedEncodingException e) {
										e.printStackTrace();
								}
								catch (MalformedURLException e) {
										e.printStackTrace();
								}
								return false;
						}
				});
				webView.loadUrl(loginUrl);
		}

		private void sendBackResult(String username, String token) {
				Intent intent = new Intent();
				intent.putExtra(Constants.EXTRA_USERNAME, username);
				intent.putExtra(Constants.EXTRA_ACCESS_TOKEN, token);
				setResult(RESULT_OK, intent);
				finish();
		}

		public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
				Map<String, String> query_pairs = new LinkedHashMap<String, String>();
				String query = url.getQuery();
				String[] pairs = query.split("&");
				for (String pair : pairs) {
						int idx = pair.indexOf("=");
						query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
				}
				return query_pairs;
		}
}