package com.hapramp.push;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hapramp.api.DeviceId;
import com.hapramp.api.DeviceRegistrationReponse;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datamodels.requests.VoteRequestBody;
import com.hapramp.datamodels.response.ConfirmationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit on 5/12/2018.
 */

public class Notifyer {

  public static void notifyVote(String permlink, int vote) {
    if (vote == 0)
      return;
    vote = vote / 2000;
    RetrofitServiceGenerator.getService().notifyVoteOnPost(new VoteRequestBody(vote), permlink)
      .enqueue(new Callback<ConfirmationResponse>() {
        @Override
        public void onResponse(Call<ConfirmationResponse> call, Response<ConfirmationResponse> response) {
          if (response.isSuccessful()) {
            Log.d("Notifyer:vote", response.body().getStatus());
          } else {
            Log.d("Notifyer:vote", "failed");
          }
        }

        @Override
        public void onFailure(Call<ConfirmationResponse> call, Throwable t) {
          Log.d("Notifyer:vote", "failed");
        }
      });
  }

  public static void notifyComment(String permlink) {
    RetrofitServiceGenerator.getService().notifyCommentOnPost(permlink)
      .enqueue(new Callback<ConfirmationResponse>() {
        @Override
        public void onResponse(Call<ConfirmationResponse> call, Response<ConfirmationResponse> response) {
          if (response.isSuccessful()) {
            Log.d("Notifyer:comment", response.body().getStatus());
          } else {
            Log.d("Notifyer:comment", "failed");
          }
        }

        @Override
        public void onFailure(Call<ConfirmationResponse> call, Throwable t) {
          Log.d("Notifyer:comment", "failed");
        }
      });
  }

  public static void updateDeviceToken() {
    final String token = FirebaseInstanceId.getInstance().getToken();
    RetrofitServiceGenerator.getService().updateUserDeviceId(new DeviceId(token)).enqueue(new Callback<DeviceRegistrationReponse>() {
      @Override
      public void onResponse(Call<DeviceRegistrationReponse> call, Response<DeviceRegistrationReponse> response) {
        if (response.isSuccessful()) {
          Log.d("FirebaseService", "Token Updated!" + token);
        } else {
          Log.d("FirebaseService", "Token Update Failed");
        }
      }

      @Override
      public void onFailure(Call<DeviceRegistrationReponse> call, Throwable t) {
        Log.d("FirebaseService", "Token Update Failed");
      }
    });
  }
}
