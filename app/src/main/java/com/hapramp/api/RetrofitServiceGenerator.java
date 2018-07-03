package com.hapramp.api;

import com.hapramp.preferences.HaprampPreferenceManager;

public class RetrofitServiceGenerator {

    public static HaprampAPI getService() {
        String token = HaprampPreferenceManager.getInstance().getUserToken();
        return HaprampApiClient.getClient(token).create(HaprampAPI.class);
    }
}
