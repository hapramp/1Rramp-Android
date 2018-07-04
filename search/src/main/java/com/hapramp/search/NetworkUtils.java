package com.hapramp.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
		public void request(final String url,final String method,final String body) {
				new Thread() {
						@Override
						public void run() {
								URL obj;
								try {
										obj = new URL(url);
										HttpURLConnection con = (HttpURLConnection) obj.openConnection();
										con.setRequestMethod(method);
										con.setDoOutput(true);
										if (body.length() > 0) {
												OutputStream os = con.getOutputStream();
												os.write(body.getBytes("UTF-8"));
										}
										int responseCode = con.getResponseCode();
										if (responseCode == HttpURLConnection.HTTP_OK) {
												BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
												String inputLine;
												StringBuilder response = new StringBuilder();
												while ((inputLine = in.readLine()) != null) {
														response.append(inputLine);
												}
												in.close();
												if (networkResponseCallback != null) {
														networkResponseCallback.onResponse(response.toString());
												}
										} else {
												if (networkResponseCallback != null) {
														networkResponseCallback.onError("Response Code: " + responseCode);
												}
										}
								}
								catch (IOException e) {
										if (networkResponseCallback != null) {
												networkResponseCallback.onError("Newtwork Error: " + e.toString());
										}
										e.printStackTrace();
								}
						}
				}.start();
		}

		public NetworkResponseCallback networkResponseCallback;

		public void setNetworkResponseCallback(NetworkResponseCallback networkResponseCallback) {
				this.networkResponseCallback = networkResponseCallback;
		}

		public interface NetworkResponseCallback {
				void onResponse(String response);

				void onError(String e);
		}
}
