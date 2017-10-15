package com.hapramp.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.hapramp.interfaces.CreateOrUpdateUserCallback;
import com.hapramp.logger.L;
import com.hapramp.models.requests.UpdateUserRequestModel;
import com.hapramp.models.response.CreateUserResponseModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ankit on 10/11/2017.
 */

public class DataServer {
        /*
    * Note: All the helper methods will be static in order to follow singleton pattern
    * */

    private static final String TAG = DataServer.class.getSimpleName();
    private static HaprampAPI haprampAPI = null;

    private static HaprampAPI getService() {
        if(haprampAPI==null) {
            haprampAPI = HaprampApiClient.getClient()
                    .create(HaprampAPI.class);
        }
        return haprampAPI;
    }

    public static void createOrUpdateUser(UpdateUserRequestModel requestModel, final CreateOrUpdateUserCallback callback) {

        L.D.m(TAG, "Requesting Update or Create User");

        getService().createUser(requestModel)
                .enqueue(new Callback<CreateUserResponseModel>() {
                    @Override
                    public void onResponse(Call<CreateUserResponseModel> call, Response<CreateUserResponseModel> response) {

                        if (response.code() == 400 ) {
                            Log.d(TAG, "onResponse - Status : " + response.code());
                            Gson gson = new Gson();
                            TypeAdapter<CreateUserResponseModel> adapter = gson.getAdapter(CreateUserResponseModel.class);
                            try {
                                if (response.errorBody() != null)
                                    Log.d(TAG,"Res-"+adapter.fromJson(response.errorBody().string()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateUserResponseModel> call, Throwable t) {
                        L.D.m(TAG,"Error: "+t.toString());
                        // callback.onFailedToCreateOrUpdateUser();
                    }
                });
    }

}
