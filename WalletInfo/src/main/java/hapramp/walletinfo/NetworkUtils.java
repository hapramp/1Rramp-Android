package hapramp.walletinfo;

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
										con.setConnectTimeout(20000);
										con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
										con.setRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.339");
										if (body.length() > 0) {
												con.setDoOutput(true);
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
