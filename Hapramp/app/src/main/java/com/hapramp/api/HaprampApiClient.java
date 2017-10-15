package com.hapramp.api;

import com.hapramp.logger.L;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ankit on 5/13/2017.
 */

public class HaprampApiClient {

    private static Retrofit retrofit = null;
    private static final String TAG = HaprampApiClient.class.getSimpleName();

    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(URLS.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        L.D.m(TAG, "Build Retrofit ");
        return retrofit;
    }
}
