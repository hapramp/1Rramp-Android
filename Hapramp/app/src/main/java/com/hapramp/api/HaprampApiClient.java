package com.hapramp.api;

/**
 * Created by Ankit on 5/13/2017.
 */

public class HaprampApiClient {

//    private static Retrofit retrofit = null;
//    private static final String TAG = HaprampApiClient.class.getSimpleName();
//
//    public static Retrofit getClient(final String api_key) {
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request()
//                                .newBuilder()
//                                .addHeader("Authorization", api_key)
//                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
//                .addInterceptor(logging)
//                .build();
//
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(URLS.BASE_URL)
//                .addConverterFactory(StringConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        L.D.m(TAG, "Build Retrofit");
//        L.D.m(TAG, "API KEY " + api_key);
//
//        return retrofit;
//    }
//

}
