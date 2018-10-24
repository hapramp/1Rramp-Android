package com.hapramp.api;

import com.hapramp.models.CommunitySelectionServerUpdateBody;
import com.hapramp.models.CompetitionCreateResponse;
import com.hapramp.models.VerificationDataBody;
import com.hapramp.models.VerifiedToken;
import com.hapramp.models.requests.CompetitionCreateBody;
import com.hapramp.models.response.FileUploadReponse;
import com.hapramp.steem.CommunitySelectionResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Ankit on 5/13/2017.
 */

public interface HaprampAPI {
  @POST("login")
  Call<VerifiedToken> verifyUser(@Body VerificationDataBody verificationDataBody);

  @PUT("users/communities")
  Call<CommunitySelectionResponse> updateCommunitySelections(@Body CommunitySelectionServerUpdateBody body);

  @POST
  Call<CompetitionCreateResponse> createCompetition(@Url String url, @Body CompetitionCreateBody body);

  @Multipart
  @POST("upload")
  Call<FileUploadReponse> uploadFile(@Part MultipartBody.Part file);
}
