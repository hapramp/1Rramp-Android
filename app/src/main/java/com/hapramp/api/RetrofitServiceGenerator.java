package com.hapramp.api;

public class RetrofitServiceGenerator {

    private static HaprampAPI haprampAPI = null;

    public static HaprampAPI getService() {

        if (haprampAPI == null) {
            haprampAPI = HaprampApiClient.getClient().create(HaprampAPI.class);
        }
        return haprampAPI;
    }

}
