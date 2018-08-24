package com.hapramp.viewmodel.communityselectionpage;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.datamodels.CommunitySelectionServerUpdateBody;
import com.hapramp.steem.CommunitySelectionResponse;
import com.hapramp.ui.callbacks.communityselection.CommunitySelectionPageCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit on 5/7/2018.
 */

public class CommunitySelectionPageViewModel extends ViewModel {

  MutableLiveData<List<CommunityModel>> communities;
  CommunitySelectionPageCallback communitySelectionPageCallback;

  public MutableLiveData<List<CommunityModel>> getCommunities(CommunitySelectionPageCallback communitySelectionPageCallback) {
    if (communities == null) {
      communities = new MutableLiveData<>();
      fetchCommunities();
    }
    this.communitySelectionPageCallback = communitySelectionPageCallback;
    return communities;
  }

  private void fetchCommunities() {
    RetrofitServiceGenerator.getService().getCommunities()
      .enqueue(new Callback<List<CommunityModel>>() {
        @Override
        public void onResponse(Call<List<CommunityModel>> call, Response<List<CommunityModel>> response) {
          if (response.isSuccessful()) {
            communities.setValue(response.body());
          } else {
            communitySelectionPageCallback.onCommunityFetchFailed();
          }
        }

        @Override
        public void onFailure(Call<List<CommunityModel>> call, Throwable t) {
          communitySelectionPageCallback.onCommunityFetchFailed();
        }
      });
  }

  public void updateServer(List<Integer> selected) {
    CommunitySelectionServerUpdateBody body = new CommunitySelectionServerUpdateBody(selected);
    RetrofitServiceGenerator.getService().updateCommunitySelections(body).enqueue(new Callback<CommunitySelectionResponse>() {
      @Override
      public void onResponse(Call<CommunitySelectionResponse> call, Response<CommunitySelectionResponse> response) {
        if (response.isSuccessful()) {
          communitySelectionPageCallback.onCommunityUpdated(response.body().getCommunities());
        } else {
          communitySelectionPageCallback.onCommunityUpdateFailed();
        }
      }

      @Override
      public void onFailure(Call<CommunitySelectionResponse> call, Throwable t) {
        communitySelectionPageCallback.onCommunityUpdateFailed();
      }
    });
  }
}
