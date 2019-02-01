package com.hapramp.api;

import com.hapramp.models.AppServerUserModel;
import com.hapramp.models.CommunitySelectionServerUpdateBody;
import com.hapramp.models.CompetitionCreateResponse;
import com.hapramp.models.CompetitionEntryConfirmationBody;
import com.hapramp.models.CompetitionEntryResponse;
import com.hapramp.models.DeleteCompetitionResponse;
import com.hapramp.models.DraftPostModel;
import com.hapramp.models.DraftUploadResponse;
import com.hapramp.models.FormattedBodyResponse;
import com.hapramp.models.LookupAccount;
import com.hapramp.models.MicroCommunity;
import com.hapramp.models.RebloggedModel;
import com.hapramp.models.VerificationDataBody;
import com.hapramp.models.VerifiedToken;
import com.hapramp.models.WinnersRankBody;
import com.hapramp.models.requests.CompetitionCreateBody;
import com.hapramp.models.response.FileUploadReponse;
import com.hapramp.steem.CommunitySelectionResponse;

import java.util.List;

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
import retrofit2.http.Query;
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

  @GET("users/usernames/{username}")
  Call<AppServerUserModel> fetchUserByUsername(@Path("username") String username);

  @Multipart
  @POST("upload")
  Call<FileUploadReponse> uploadFile(@Part MultipartBody.Part file);

  @POST
  Single<LookupAccount> getUsernames(@Url String url, @Body String body);

  @GET("competitions/{competition_id}/register-permlink/{blog_type}")
  Single<CompetitionCreateResponse> registerCompetitionPermlink(
    @Path("competition_id") String competitionId,
    @Path("blog_type") String blog_type,
    @Query("permlink") String permlink);

  @GET("drafts")
  Call<List<DraftUploadResponse>> getAllDrafts();

  @POST("drafts")
  Call<DraftUploadResponse> postDraft(@Body DraftPostModel draftPostModel);

  @PUT("drafts/{draft_id}")
  Call<DraftUploadResponse> updateDraft(@Path("draft_id") long draftId, @Body DraftPostModel draftPostModel);

  @DELETE("drafts/{draft_id}")
  Call<DraftUploadResponse> deleteDraft(@Path("draft_id") long draftId);

  @POST
  Call<RebloggedModel> getRebloggedByUsers(@Url String url, @Body String body);

  @GET("micro-communities")
  Call<List<MicroCommunity>> fetchMicroCommunity();

  @POST("micro-communities/{micro_community_tag}/join")
  Call<AppServerUserModel> joinCommunity(@Path("micro_community_tag") String mc_id);

  @DELETE("micro-communities/{micro_community_tag}/join")
  Call<AppServerUserModel> leaveCommunity(@Path("micro_community_tag") String mc_id);
}
