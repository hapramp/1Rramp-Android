package com.hapramp.api;

import com.hapramp.utils.RestrictedSocketFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ankit on 5/13/2017.
 */

public class HaprampApiClient {

  private static final String TAG = HaprampApiClient.class.getSimpleName();
  private static Retrofit retrofit = null;
  private static int size1 = 131072;
  private static int size2 = 262144;
  private static int size3 = 524288;

  public static Retrofit getClient(final String token) {

    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient
      .Builder()
      .connectTimeout(5, TimeUnit.MINUTES)
      .readTimeout(5, TimeUnit.MINUTES)
      .socketFactory(new RestrictedSocketFactory(size3))
      .addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
          Request request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Token " + token)
            .build();
          return chain.proceed(request);
        }
      })
      .addInterceptor(logging)
      .build();

    retrofit = new Retrofit.Builder()
      .baseUrl(URLS.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(client)
      .build();
    return retrofit;
  }
}
