package com.hapramp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.api.HaprampAPI;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.CommunitySelectionServerUpdateBody;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.views.CommunitySelectionView;
import com.hapramp.views.skills.SelectableInterestsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
*  This activity is responsible for community selection by user.
*  Activity is opened when User has not selected this earlier.
*  LoginActivity gets all the relevant about user after successful login.
*  After which decisions are taken.
* */

public class CommunitySelectionActivity extends BaseActivity {

    private List<String> communities;

    @BindView(R.id.action_bar_title) TextView actionBarTitle;
    @BindView(R.id.communitySelectionView) CommunitySelectionView communitySelectionView;
    @BindView(R.id.toolbar_drop_shadow) FrameLayout toolbarDropShadow;
    @BindView(R.id.communityContinueButton) TextView communityContinueButton;

    public static final String TAG = CommunitySelectionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_selection);
        ButterKnife.bind(this);
        init();

    }

    private void init() {

        communities = new ArrayList<>();
        communityContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  updateServer();
            }
        });

        fetchCommunities();

    }

    private void updateServer() {

        CommunitySelectionServerUpdateBody body = new CommunitySelectionServerUpdateBody(communitySelectionView.getSelectionList());

        DataServer.getService().updateCommunitySelections(body).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    onCommunityUpdated();
                }else{
                    onCommunityUpdateFailed(response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                onCommunityUpdateFailed(t.toString());
            }
        });
    }

    private void onCommunityUpdateFailed(String errorMsg) {
        toast("ERROR : "+errorMsg);
    }

    private void onCommunityUpdated() {
        toast("Community Updated!");
    }

    private void fetchCommunities() {

        DataServer.getService().getCommunities()
                .enqueue(new Callback<List<CommunityModel>>() {
                    @Override
                    public void onResponse(Call<List<CommunityModel>> call, Response<List<CommunityModel>> response) {
                        if (response.isSuccessful()) {
                            onCommunitiesFetched(response.body());
                        } else {
                            onCommunitiesFetchFailed("" + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CommunityModel>> call, Throwable t) {
                        onCommunitiesFetchFailed(t.toString());
                    }
                });

    }

    private void onCommunitiesFetchFailed(String msg) {
        toast("ERROR: "+msg);
    }

    private void onCommunitiesFetched(List<CommunityModel> body) {

        communitySelectionView.setCommunityList(body);
        HaprampPreferenceManager.getInstance().saveAllCommunityListAsJson(new Gson().toJson(body));

    }




}
