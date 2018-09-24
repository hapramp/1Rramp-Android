package com.hapramp.steemconnect;

import com.hapramp.api.URLS;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectOptions;

import static com.hapramp.api.URLS.STEEMCONNECT_BASE_URL;

public class SteemConnectUtils {
  public static SteemConnect getSteemConnectInstance() {
    SteemConnect.InstanceBuilder instanceBuilder = new SteemConnect.InstanceBuilder();
    instanceBuilder
      .setApp("hapramp.app")
      .setBaseUrl(STEEMCONNECT_BASE_URL)
      .setCallbackUrl(URLS.HAPRAMP_REDIRECT_URL)
      .setScope(new String[]{
        "vote",
        "comment",
        "delete_comment",
        "comment_options",
        "custom_json",
        "claim_reward_balance"});
    return instanceBuilder.build();
  }

  public static SteemConnect getSteemConnectInstance(String accessToken) {
    SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
    steemConnectOptions.setBaseUrl(STEEMCONNECT_BASE_URL);
    steemConnectOptions.setAccessToken(accessToken);
    return new SteemConnect(steemConnectOptions);
  }
}
