package com.hapramp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.SteemFollowingRequestBody;
import com.hapramp.steem.FollowingsResponse;
import com.hapramp.steem.LocalConfig;
import com.hapramp.steem.SteemHelper;
import com.squareup.okhttp.internal.Util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunitySelectionActivity extends AppCompatActivity {

    List<String> communities;

    public static final String TAG = CommunitySelectionActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.communiti_activity);
        communities = new ArrayList<>();
        fetchFollowings();

    }

    private void fetchFollowings() {

        String username = HaprampPreferenceManager.getInstance().getSteemUsername();
        RequestBody requestBody = SteemFollowingRequestBody.create(SteemHelper.getFollowingsRequestString("the-dragon"));
        RetrofitServiceGenerator.getService()
                .getFollowings(LocalConfig.STEEM_API, requestBody)
                .enqueue(new Callback<FollowingsResponse>() {
                    @Override
                    public void onResponse(Call<FollowingsResponse> call, Response<FollowingsResponse> response) {
                        if (response.isSuccessful()) {
                            filterCommunities(response.body());
                        } else {
                            onFollowingFetchError();
                        }
                    }

                    @Override
                    public void onFailure(Call<FollowingsResponse> call, Throwable t) {

                    }
                });
    }

    private void onFollowingFetchError() {

        Toast.makeText(this, "Something went wrong while fetching followings", Toast.LENGTH_LONG);

    }

    private void filterCommunities(FollowingsResponse body) {

        for (FollowingsResponse.Result r : body.getmResult()) {
            Log.d(TAG,"Checking "+r.getmFollowing());
            if (Communities.doesCommunityExists(r.getmFollowing())) {
                communities.add(r.getmFollowing());
            }
        }
    }

    public abstract class StringRequestBody extends RequestBody {

    }
}
