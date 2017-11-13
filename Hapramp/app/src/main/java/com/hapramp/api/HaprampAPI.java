package com.hapramp.api;

import com.hapramp.CompetitionsPostReponse;
import com.hapramp.models.LikeBody;
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
import com.hapramp.models.response.OrgsResponse;
import com.hapramp.models.response.PostResponse;
import com.hapramp.models.response.SkillsModel;
import com.hapramp.models.response.SkillsUpdateResponse;
import com.hapramp.models.response.UpdateUserResponse;
import com.hapramp.models.response.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ankit on 5/13/2017.
 */

public interface HaprampAPI {

    @POST("users")
    Call<CreateUserReponse> createUser(@Body CreateUserRequest userRequestModel);

    @GET("organizations")
    Call<List<OrgsResponse>> getOrgs();

    @GET("users/user")
    Call<FetchUserResponse> getUserFromToken();

    @PUT("users/{user_id}")
    Call<UpdateUserResponse> updateOrg(@Path("user_id") String userID, @Body UserUpdateModel user);

    @GET("skills")
    Call<List<SkillsModel>> getSkills();

    @PUT("users/skills")
    Call<SkillsUpdateResponse> setSkills(@Body SkillsUpdateBody skillsUpdateBody);

    @GET("posts")
    Call<List<PostResponse>> getAlltPosts();

    @GET("posts")
    Call<List<PostResponse>> getPostsBySkills(@Query("skills_or") int skills_id);

    @GET("posts")
    Call<List<PostResponse>> getPostsByContest(@Query("contest_id") String contest_id);

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

    @POST("posts/{post_id}/comments")
    Call<CommentCreateResponse> createComment(@Path("post_id") String postId , @Body CommentBody body);

    @GET("posts/{post_id}/comments")
    Call<CommentsResponse> getComments(@Path("post_id") String post_id);

    @GET("posts")
    Call<List<PostResponse>> getPostsBySkillsAndUserId(@Query("skills_or") int skills_id,@Query("user_id") String userId);

    @GET("contests/{contest_id}/posts")
     Call<CompetitionsPostReponse> getCompetitionsPosts(String compId);

}
