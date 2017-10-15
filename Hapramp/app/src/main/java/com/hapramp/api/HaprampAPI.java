package com.hapramp.api;

import com.hapramp.models.requests.UpdateUserRequestModel;
import com.hapramp.models.response.CreateUserResponseModel;
import com.hapramp.models.response.FetchUserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Ankit on 5/13/2017.
 */

public interface HaprampAPI {

    @POST("users")
    Call<CreateUserResponseModel> createUser(@Body UpdateUserRequestModel model);

    @GET("users/user")
    Call<FetchUserResponse> getUserFromToken(@Header("Authorization") String token);

}
