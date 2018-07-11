package com.hapramp.api;

import com.hapramp.datamodels.CommunityModel;
import com.hapramp.datamodels.CommunitySelectionServerUpdateBody;
import com.hapramp.datamodels.VerificationDataBody;
import com.hapramp.datamodels.VerifiedToken;
import com.hapramp.datamodels.VoteModel;
import com.hapramp.datamodels.VoteStatus;
import com.hapramp.datamodels.requests.SteemLoginResponseModel;
import com.hapramp.datamodels.requests.SteemSignupRequestModel;
import com.hapramp.datamodels.requests.VoteRequestBody;
import com.hapramp.datamodels.response.CompetitionsPostReponse;
import com.hapramp.datamodels.requests.LikeBody;
import com.hapramp.datamodels.requests.CommentBody;
import com.hapramp.datamodels.requests.CreateUserRequest;
import com.hapramp.datamodels.requests.PostCreateBody;
import com.hapramp.datamodels.requests.SkillsUpdateBody;
import com.hapramp.datamodels.requests.UserUpdateModel;
import com.hapramp.datamodels.response.CommentCreateResponse;
import com.hapramp.datamodels.response.CommentsResponse;
import com.hapramp.datamodels.response.CompetitionResponse;
import com.hapramp.datamodels.response.ConfirmationResponse;
import com.hapramp.datamodels.response.CreateUserReponse;
import com.hapramp.datamodels.response.FetchUserResponse;
import com.hapramp.datamodels.response.FileUploadReponse;
import com.hapramp.datamodels.response.NotificationResponse;
import com.hapramp.datamodels.response.OrgsResponse;
import com.hapramp.datamodels.response.PostResponse;
import com.hapramp.datamodels.response.SkillsUpdateResponse;
import com.hapramp.datamodels.response.SteemLoginRequestModel;
import com.hapramp.datamodels.response.SteemSignUpResponseModel;
import com.hapramp.datamodels.response.UpdateUserResponse;
import com.hapramp.datamodels.response.UserModel;
import com.hapramp.datamodels.response.UserStatsModel;
import com.hapramp.steem.CommunitySelectionResponse;
import com.hapramp.steem.PostConfirmationModel;
import com.hapramp.steem.PreProcessingModel;
import com.hapramp.steem.ProcessedBodyResponse;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.FeedResponse;
import com.hapramp.steem.models.FeedWrapper;
import com.hapramp.steem.models.user.SteemUser;
import com.hapramp.views.extraa.CategoryTextView;
import com.hapramp.youtube.YoutubeResultModel;

import java.util.List;

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

    @GET("users/usernames/{username}")
    Call<UserModel> fetchUserCommunities(@Path("username") String username);

    @POST("posts")
    Call<ProcessedBodyResponse> sendForPreProcessing(@Body PreProcessingModel preProcessingModel);

    @POST
    Call<String> getTransferHistory(@Url String url, @Body String body);

    @POST("posts/_confirm")
    Call<ConfirmationResponse> sendPostCreationConfirmation(@Body PostConfirmationModel postConfirmationModel);

    @POST("posts/comments/_notify")
    Call<ConfirmationResponse> notifyCommentOnPost(@Query("permlink") String permlink);

    @POST("posts/votes/_notify")
    Call<ConfirmationResponse> notifyVoteOnPost(@Body VoteRequestBody voteRequestBody,@Query("permlink") String permlink);

    @GET("communities")
    Call<List<CommunityModel>> getCommunities();

    @PUT("users/communities")
    Call<CommunitySelectionResponse> updateCommunitySelections(@Body CommunitySelectionServerUpdateBody body);

    @GET("feeds/user/{username}")
    Call<FeedResponse> getUserFeeds(@Path("username") String username, @Query("limit") int limit);

    @GET
    Call<FeedWrapper> getFeedFromSteem(@Url String url);

    @GET("feeds/user/{username}")
    Call<FeedResponse> getUserFeeds(@Path("username") String username, @Query("limit") int limit,@Query("start_author") String lastAuthor,@Query("start_permlink") String lastPermlink);

    @GET("feeds/blog/{username}")
    Call<FeedResponse> getPostsOfUser(@Path("username") String username, @Query("limit") int limit);

    @GET("feeds/blog/{username}")
    Call<FeedResponse> getPostsOfUser(@Path("username") String username, @Query("limit") int limit,@Query("start_author") String lastAuthor,@Query("start_permlink") String lastPermlink);

    @GET("feeds/created/{tag}")
    Call<FeedResponse> getLatestFeed(@Path("tag") String tag, @Query("limit") int limit);

    @GET("feeds/created/{tag}")
    Call<FeedResponse> getLatestFeed(@Path("tag") String tag, @Query("limit") int limit,@Query("start_author") String lastAuthor,@Query("start_permlink") String lastPermlink);

    @GET("feeds/hot/{tag}")
    Call<FeedResponse> getHotFeed(@Path("tag") String tag, @Query("limit") int limit);

    @GET("feeds/hot/{tag}")
    Call<FeedResponse> getHotFeed(@Path("tag") String tag, @Query("limit") int limit,@Query("start_author") String lastAuthor,@Query("start_permlink") String lastPermlink);

    @GET("feeds/trending/{tag}")
    Call<FeedResponse> getTrendingFeed(@Path("tag") String tag, @Query("limit") int limit);

    @GET("feeds/trending/{tag}")
    Call<FeedResponse> getTrendingFeed(@Path("tag") String tag, @Query("limit") int limit,@Query("start_author") String lastAuthor,@Query("start_permlink") String lastPermlink);

    @GET
    Call<SteemUser> getSteemUser(@Url String url);

    @GET("users/usernames/{username}")
    Call<UserModel> getUserFromUsername(@Path("username") String username);

    @GET
    Call<YoutubeResultModel> getYoutubeResults(@Url String url);

    @PUT("users/user")
    Call<DeviceRegistrationReponse> updateUserDeviceId(@Body DeviceId deviceId);

    @Multipart
    @POST("upload")
    Call<FileUploadReponse> uploadFile(@Part MultipartBody.Part file);

    @GET("users/{user_id}/_full")
    Call<UserModel> getFullUserDetails(@Path("user_id") String user_id);

    @GET("notifications")
    Call<NotificationResponse> getNotifications(@Query("start") int start, @Query("limit") int limit);

    @POST("notifications/{notification_id}/_mark_as_read")
    Call<NotificationResponse> markAsRead(@Path("notification_id") int notification_id);

    @POST("notifications/_mark_all_read")
    Call<NotificationResponse> markAsAllRead();

}
