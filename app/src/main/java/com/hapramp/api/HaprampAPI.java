package com.hapramp.api;

import com.hapramp.models.Feed;
import com.hapramp.models.UserDataUpdateBody;
import com.hapramp.models.UserResponse;
import com.hapramp.models.requests.FollowRequestBody;
import com.hapramp.models.requests.SteemLoginResponseModel;
import com.hapramp.models.requests.SteemSignupRequestModel;
import com.hapramp.models.requests.UserBioUpdateRequestBody;
import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.models.response.CompetitionsPostReponse;
import com.hapramp.models.requests.LikeBody;
import com.hapramp.models.requests.CommentBody;
import com.hapramp.models.requests.CreateUserRequest;
import com.hapramp.models.requests.PostCreateBody;
import com.hapramp.models.requests.SkillsUpdateBody;
import com.hapramp.models.requests.UserUpdateModel;
import com.hapramp.models.response.CommentCreateResponse;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.models.response.CompetitionResponse;
import com.hapramp.models.response.CreateUserReponse;
import com.hapramp.models.response.FetchUserResponse;
import com.hapramp.models.response.NotificationResponse;
import com.hapramp.models.response.OrgsResponse;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.SkillsUpdateResponse;
import com.hapramp.models.response.SteemLoginRequestModel;
import com.hapramp.models.response.SteemSignUpResponseModel;
import com.hapramp.models.response.UpdateUserResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.models.response.UserStatsModel;
import com.hapramp.steem.PreProcessingModel;
import com.hapramp.steem.ProcessedBodyResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Ankit on 5/13/2017.
 */

public interface HaprampAPI {

    @POST("login")
    Call<SteemLoginResponseModel> login(@Body SteemLoginRequestModel requestModel);

    @POST("signup")
    Call<SteemSignUpResponseModel> signup(@Body SteemSignupRequestModel requestModel);

    @POST("signup/done")
    Call<SteemLoginResponseModel> signupDone(@Body SteemLoginRequestModel requestModel);

    @POST("posts")
    Call<ProcessedBodyResponse> sendForPreProcessing(@Body PreProcessingModel preProcessingModel);

    @POST("posts/_confirm")
    Call<ProcessedBodyResponse> sendConfirmation();






















    @POST("users")
    Call<CreateUserReponse> createUser(@Body CreateUserRequest userRequestModel);

    @GET("organizations")
    Call<List<OrgsResponse>> getOrgs();

    @GET("users/{user_id}")
    Call<UserResponse> fetchUser(@Path("user_id") int user_id);

    @GET("users/user")
    Call<FetchUserResponse> getUserFromToken();

    @PUT("users/{user_id}")
    Call<UpdateUserResponse> updateOrg(@Path("user_id") String userID, @Body UserUpdateModel user);

    @GET("skills")
    Call<List<UserModel.Skills>> getSkills();

    @PUT("users/skills")
    Call<SkillsUpdateResponse> setSkills(@Body SkillsUpdateBody skillsUpdateBody);

    @GET
    Call<PostResponse> getAlltPosts(@Url String url,@Query("order_by") String order_by);

    @GET
    Call<PostResponse> getPostsBySkills(@Url String url, @Query("skills_or") int skills_id);

    @GET
    Call<PostResponse> getPostsByUserId(@Url String url, @Query("user_id") int user_id, @Query("order_by") String order_by);

    @GET
    Call<PostResponse> getPostsByContest(@Url String url, @Query("contest_id") String contest_id);

    @GET("contests")
    Call<List<CompetitionResponse>> getAllCompetitions();

    @GET("contests")
    Call<List<CompetitionResponse>> getCompetitionsBySkills(@Query("skills_or") int skills_id);

    @GET("users/{user_id}/_full")
    Call<UserModel> getFullUserDetails(@Path("user_id") String user_id);

    @POST("posts/{post_id}/votes")
    Call<PostResponse> likePost(@Path("post_id") String post_id,@Body LikeBody body);

    @POST("posts")
    Call<PostResponse> createPost(@Body PostCreateBody body);

    @DELETE("posts/{post_id}")
    Call<PostResponse> deletePost(@Path("post_id") String post_id);

    @POST("posts/{post_id}/comments")
    Call<CommentCreateResponse> createComment(@Path("post_id") String postId , @Body CommentBody body);

    @GET
    Call<CommentsResponse> getComments(@Url String url);

    @GET
    Call<PostResponse> getPostsBySkillsAndUserId(@Url String url, @Query("skills_or") int skills_id,@Query("user_id") int userId,@Query("order_by") String order_by);


    @GET("contests/{contest_id}/posts")
    Call<CompetitionsPostReponse> getCompetitionsPosts(@Path("contest_id") String compId);

    @POST("users/{user_id}/_follow")
    Call<UserResponse> followUser(@Path("user_id") String userId , @Body FollowRequestBody body);

    @PUT("users/{user_id}")
    Call<UserResponse> updateUserDp(@Path("user_id") String userId,@Body UserDataUpdateBody body);

    @PUT("users/{user_id}")
    Call<UserResponse> updateUserBio(@Path("user_id") String userId,@Body UserBioUpdateRequestBody body);

    @POST("posts/{post_id}/votes")
    Call<Feed> votePost(@Path("post_id") String postId , @Body VoteRequestBody body);

    @DELETE("posts/{post_id}/votes")
    Call<Feed> deleteVote(@Path("post_id") int postId);

    @GET("users/{user_id}/stats")
    Call<UserStatsModel> getUserStats(@Path("user_id") String userId);

    @GET("notifications")
    Call<NotificationResponse> getNotifications(@Query("start") int start,@Query("limit") int limit);

    @POST("notifications/{notification_id}/_mark_as_read")
    Call<NotificationResponse> markAsRead(@Path("notification_id") int notification_id);

    @POST("notifications/_mark_all_read")
    Call<NotificationResponse> markAsAllRead();

}
