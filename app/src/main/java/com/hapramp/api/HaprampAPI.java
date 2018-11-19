package com.hapramp.api;

import com.hapramp.models.AppServerUserModel;
import com.hapramp.models.CommunitySelectionServerUpdateBody;
import com.hapramp.models.CompetitionCreateResponse;
import com.hapramp.models.CompetitionEntryConfirmationBody;
import com.hapramp.models.CompetitionEntryResponse;
import com.hapramp.models.DeleteCompetitionResponse;
import com.hapramp.models.FormattedBodyResponse;
import com.hapramp.models.LookupAccount;
import com.hapramp.models.VerificationDataBody;
import com.hapramp.models.VerifiedToken;
import com.hapramp.models.WinnersRankBody;
import com.hapramp.models.requests.CompetitionCreateBody;
import com.hapramp.models.response.FileUploadReponse;
import com.hapramp.steem.CommunitySelectionResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

  @POST("competitions/{competition_id}/posts")
  Call<CompetitionEntryResponse> updateServerAboutEntry(@Path("competition_id") String comp_id,
                                                        @Body CompetitionEntryConfirmationBody competitionEntryConfirmationBody);

  @PUT("competitions/{competition_id}/winners")
  Call<CompetitionEntryResponse> updateServerWithRanks(@Path("competition_id") String comp_id, @Body WinnersRankBody winnersRankBody);

  @POST("competitions/{competition_id}/winners/announce")
  Call<CompetitionEntryResponse> announceResults(@Path("competition_id") String comp_id);

  @GET("competitions/{competition_id}/body/winners")
  Call<FormattedBodyResponse> requestWinnersPostBody(@Path("competition_id") String comp_id);

  @GET("competitions/{competition_id}/body/announce")
  Call<FormattedBodyResponse> requestContestPostBody(@Path("competition_id") String comp_id);

  @DELETE("competitions/{competition_id}")
  Call<DeleteCompetitionResponse> deleteCompetition(@Path("competition_id") String comp_id);

  @GET("users/user")
  Call<AppServerUserModel> fetchAppUser();

  @Multipart
  @POST("upload")
  Call<FileUploadReponse> uploadFile(@Part MultipartBody.Part file);

  @POST
  Single<LookupAccount> getUsernames(@Url String url, @Body String body);
}
