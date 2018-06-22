package com.hapramp.steemconnect;

import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectOptions;

public class SteemConnectUtils {

		public static SteemConnect getSteemConnectInstance() {
				SteemConnect.InstanceBuilder instanceBuilder = new SteemConnect.InstanceBuilder();
				instanceBuilder
						.setApp("hapramp.app")
						.setCallbackUrl("https://alpha.hapramp.com/_oauth/")
						.setScope(new String[]{"comment", "vote"});
				//todo add more scopes
				return instanceBuilder.build();
		}

		public static SteemConnect getSteemConnectInstance(String accessToken) {
				SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
				steemConnectOptions.setAccessToken(accessToken);
				return new SteemConnect(steemConnectOptions);
		}

}
