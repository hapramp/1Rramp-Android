package com.hapramp.api;

import android.util.Log;

import com.hapramp.interfaces.CommentCreateCallback;
import com.hapramp.interfaces.CommentFetchCallback;
import com.hapramp.interfaces.CompetitionFetchCallback;
import com.hapramp.interfaces.CompetitionsPostFetchCallback;
import com.hapramp.interfaces.CreateUserCallback;
import com.hapramp.interfaces.FetchUserCallback;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.MarkAsReadNotificationCallback;
import com.hapramp.interfaces.MarkallAsReadNotificationCallback;
import com.hapramp.interfaces.NotificationCallback;
import com.hapramp.interfaces.OnPostDeleteCallback;
import com.hapramp.interfaces.OnSkillsUpdateCallback;
import com.hapramp.interfaces.OrgUpdateCallback;
import com.hapramp.interfaces.OrgsFetchCallback;
import com.hapramp.interfaces.PostCreateCallback;
import com.hapramp.interfaces.PostFetchCallback;
import com.hapramp.interfaces.UserStatsCallback;
import com.hapramp.interfaces.VoteDeleteCallback;
import com.hapramp.interfaces.VotePostCallback;
import com.hapramp.logger.L;
import com.hapramp.datamodels.requests.VoteRequestBody;
import com.hapramp.datamodels.response.CompetitionsPostReponse;
import com.hapramp.datamodels.requests.CommentBody;
import com.hapramp.datamodels.requests.CreateUserRequest;
import com.hapramp.datamodels.error.GeneralErrorModel;
import com.hapramp.datamodels.requests.PostCreateBody;
import com.hapramp.datamodels.requests.SkillsUpdateBody;
import com.hapramp.datamodels.requests.UserUpdateModel;
import com.hapramp.datamodels.response.CommentCreateResponse;
import com.hapramp.datamodels.response.CommentsResponse;
import com.hapramp.datamodels.response.CompetitionResponse;
import com.hapramp.datamodels.response.CreateUserReponse;
import com.hapramp.datamodels.response.FetchUserResponse;
import com.hapramp.datamodels.response.NotificationResponse;
import com.hapramp.datamodels.response.OrgsResponse;
import com.hapramp.datamodels.response.PostResponse;
import com.hapramp.datamodels.response.SkillsUpdateResponse;
import com.hapramp.datamodels.response.UpdateUserResponse;
import com.hapramp.datamodels.response.UserModel;
import com.hapramp.datamodels.response.UserStatsModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.Feed;
import com.hapramp.utils.Constants;

import java.util.List;

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

    public static HaprampAPI getService() {
        String token = HaprampPreferenceManager.getInstance().getUserToken();
        return HaprampApiClient.getClient(token).create(HaprampAPI.class);
    }

    public static void resetAPI() {
        haprampAPI = null;
    }

    public static void fetchUser(final FetchUserCallback callback) {
        L.D.m(TAG, "fetching User...");

        getService().getUserFromToken().enqueue(new Callback<FetchUserResponse>() {
            @Override
            public void onResponse(Call<FetchUserResponse> call, Response<FetchUserResponse> response) {
                if (response.isSuccessful()) {
                    L.D.m(TAG, " Resp:" + response.body().toString());
                    callback.onUserFetched(response.body());

                } else {
                    GeneralErrorModel error = ErrorUtils.parseError(response);
                    L.D.m(TAG, "Fetch Error " + error.toString());
                    callback.onUserNotExists();
                }
            }

            @Override
            public void onFailure(Call<FetchUserResponse> call, Throwable t) {
                L.D.m(TAG, "Error " + t.toString());
                callback.onUserFetchedError();
            }
        });

    }

    public static void createUser(CreateUserRequest createUserRequest, final CreateUserCallback createUserCallback) {

        L.D.m(TAG, "Creating User...");
        getService().createUser(createUserRequest).enqueue(new Callback<CreateUserReponse>() {
            @Override
            public void onResponse(Call<CreateUserReponse> call, Response<CreateUserReponse> response) {
                if (response.isSuccessful()) {
                    L.D.m(TAG, " Resp:" + response.body().toString());
                    createUserCallback.onUserCreated(response.body());
                } else {
                    GeneralErrorModel error = ErrorUtils.parseError(response);
                    createUserCallback.onFailedToCreateUser(error.getMessage());
                    L.D.m(TAG, "Create Error " + error.toString());
                }
            }

            @Override
            public void onFailure(Call<CreateUserReponse> call, Throwable t) {
                L.D.m(TAG, "Failure : " + t.toString());
                createUserCallback.onFailedToCreateUser("Something Went Wrong!!");
            }
        });

    }

    public static void getOrgs(final OrgsFetchCallback callback) {
        L.D.m(TAG, "Getting Org...");
        getService().getOrgs().enqueue(new Callback<List<OrgsResponse>>() {
            @Override
            public void onResponse(Call<List<OrgsResponse>> call, Response<List<OrgsResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onOrgsFetched(response.body());
                } else {
                    callback.onOrgFetchedError();
                }
            }

            @Override
            public void onFailure(Call<List<OrgsResponse>> call, Throwable t) {
                callback.onOrgFetchedError();
            }
        });

    }

    public static void updateOrg(String userId, UserUpdateModel userUpdateModel, final OrgUpdateCallback callback) {
        L.D.m(TAG, "Updating Org...");
        getService()
                .updateOrg(userId, userUpdateModel).enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "Resp " + response.body().toString());
                    callback.onOrgUpdated();
                } else {
                    Log.d(TAG, "Resp Error: " + ErrorUtils.parseError(response).toString());
                    callback.onOrgUpdateFailed();
                }

            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                callback.onOrgUpdateFailed();
            }
        });

    }

    public static void setSkills(SkillsUpdateBody body, final OnSkillsUpdateCallback callback) {

        L.D.m(TAG, "Skills update...");
        getService().setSkills(body)
                .enqueue(new Callback<SkillsUpdateResponse>() {
                    @Override
                    public void onResponse(Call<SkillsUpdateResponse> call, Response<SkillsUpdateResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSkillsUpdated();
                        } else {
                            callback.onSkillsUpdateFailed();
                            L.D.m(TAG, "Error Skills Update " + ErrorUtils.parseError(response).toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<SkillsUpdateResponse> call, Throwable t) {
                        callback.onSkillsUpdateFailed();
                    }
                });
    }

    public static void getPosts(String url, int skills_id, final PostFetchCallback callback) {

        if (skills_id == 0) {
            getPosts(url, callback);
            return;
        }

        L.D.m(TAG, "Fetching posts by skills...");

        getService()
                .getPostsBySkills(url, skills_id)
                .enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            L.D.m(TAG, "Posts: " + response.body().toString());
                            callback.onPostFetched(response.body());
                        } else {
                            callback.onPostFetchError();
                            L.E.m(TAG, "Error: " + ErrorUtils.parseError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        callback.onPostFetchError();
                        L.D.m(TAG, "Post Fetch Error " + t.toString());
                    }
                });
    }

    public static void getPosts(final String url, final PostFetchCallback callback) {

        L.D.m(TAG, "Fetching posts...");

        getService().getAlltPosts(url, Constants.POST_ORDER_DES)
                .enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onPostFetched(response.body());
                        } else {
                            L.E.m(TAG, "Error: " + ErrorUtils.parseError(response).toString());
                            callback.onPostFetchError();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {

                        L.D.m(TAG, "Post Fetch Error " + t.toString());
                        callback.onPostFetchError();
                    }
                });
    }

    public static void getPostsByUserId(final String url, int userId, final PostFetchCallback callback) {

        getService()
                .getPostsByUserId(url, userId, Constants.POST_ORDER_DES)
                .enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            L.D.m(TAG, "Posts: " + response.body().toString());
                            callback.onPostFetched(response.body());
                        } else {
                            callback.onPostFetchError();
                            L.E.m(TAG, "Error: " + ErrorUtils.parseError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        callback.onPostFetchError();
                        L.D.m(TAG, "Post Fetch Error " + t.toString());
                    }
                });
    }

    public static void getPosts(final String url, int skill_id, int user_id, final PostFetchCallback callback) {

        if (skill_id == 0) {
            getPostsByUserId(url, user_id, callback);
            return;
        }

        getService().getPostsBySkillsAndUserId(url, skill_id, user_id, Constants.POST_ORDER_DES)
                .enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onPostFetched(response.body());
                        } else {
                            L.E.m(TAG, "Error: " + ErrorUtils.parseError(response).toString());
                            callback.onPostFetchError();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {

                        L.D.m(TAG, "Post Fetch Error " + t.toString());
                        callback.onPostFetchError();
                    }
                });

    }

    public static void getCompetitions(final CompetitionFetchCallback callback) {

        L.D.m(TAG, "Fetching competitions...");

        getService().getAllCompetitions()
                .enqueue(new Callback<List<CompetitionResponse>>() {
                    @Override
                    public void onResponse(Call<List<CompetitionResponse>> call, Response<List<CompetitionResponse>> response) {

                        if (response.isSuccessful()) {
                            L.D.m(TAG, "Comp " + response.body().toString());
                            callback.onCompetitionsFetched(response.body());
                        } else {
                            L.D.m(TAG, "Error: " + ErrorUtils.parseError(response).toString());
                            callback.onCompetitionsFetchError();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<CompetitionResponse>> call, Throwable t) {
                        L.D.m(TAG, "Error: " + t.toString());
                        callback.onCompetitionsFetchError();
                    }
                });

    }

    public static void getCompetitionsBySkills(int skill_id, final CompetitionFetchCallback callback) {

        L.D.m(TAG, "Fetching competitions...");


        getService()
                .getCompetitionsBySkills(skill_id)
                .enqueue(new Callback<List<CompetitionResponse>>() {
                    @Override
                    public void onResponse(Call<List<CompetitionResponse>> call, Response<List<CompetitionResponse>> response) {

                        if (response.isSuccessful()) {
                            L.D.m(TAG, "Comp " + response.body().toString());
                            callback.onCompetitionsFetched(response.body());
                        } else {
                            callback.onCompetitionsFetchError();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<CompetitionResponse>> call, Throwable t) {
                        callback.onCompetitionsFetchError();
                    }
                });
    }

    public static void getFullUserDetails(String userId, final FullUserDetailsCallback callback) {
        L.D.m(TAG, "Fetching Complete User Info...");

        getService().getFullUserDetails(userId)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            callback.onFullUserDetailsFetched(response.body());
                        } else {
                            callback.onFullUserDetailsFetchError();
                            L.D.m(TAG, "User Details Error : " + ErrorUtils.parseError(response).toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        callback.onFullUserDetailsFetchError();
                        L.D.m(TAG, "Error:" + t.toString());
                    }
                });

    }

    public static void createPost(final String jobId, PostCreateBody postCreateBody, final PostCreateCallback callback) {

        getService()
                .createPost(postCreateBody)
                .enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onPostCreated(jobId);
                        } else {
                            L.D.m(TAG, ErrorUtils.parseError(response).toString());
                            callback.onPostCreateError(jobId);
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        callback.onPostCreateError(jobId);
                    }
                });

    }

    public static void createPost(PostCreateBody postCreateBody, final PostCreateCallback callback) {

        getService()
                .createPost(postCreateBody)
                .enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onPostCreated();
                        } else {
                            L.D.m(TAG, ErrorUtils.parseError(response).toString());
                            callback.onPostCreateError();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        callback.onPostCreateError();
                    }
                });

    }

    public static void createComment(String postId, CommentBody body, final CommentCreateCallback callback) {

        Log.d(TAG, "Creating comment..");
        getService()
                .createComment(postId, body)
                .enqueue(new Callback<CommentCreateResponse>() {
                    @Override
                    public void onResponse(Call<CommentCreateResponse> call, Response<CommentCreateResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onCommentCreated(response.body());
                        } else {
                            Log.d(TAG, ErrorUtils.parseError(response).toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentCreateResponse> call, Throwable t) {
                        Log.d(TAG, "Error :" + t.toString());
                    }
                });

    }

    public static void getComments(String url, final CommentFetchCallback callback) {

        Log.d(TAG, "Fetching comments...");

        getService()
                .getComments(url)
                .enqueue(new Callback<CommentsResponse>() {
                    @Override
                    public void onResponse(Call<CommentsResponse> call, Response<CommentsResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onCommentFetched(response.body());
                        } else {
                            Log.d(TAG, ErrorUtils.parseError(response).toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentsResponse> call, Throwable t) {
                        Log.d(TAG, "Error " + t.toString());
                    }
                });

    }

    public static void getCompetitionsPosts(String compId, final CompetitionsPostFetchCallback callback) {

        getService()
                .getCompetitionsPosts(compId)
                .enqueue(new Callback<CompetitionsPostReponse>() {
                    @Override
                    public void onResponse(Call<CompetitionsPostReponse> call, Response<CompetitionsPostReponse> response) {
                        if (response.isSuccessful()) {
                            callback.onCompetitionsPostsFetched(response.body());
                        } else {
                            Log.d(TAG, "Error : " + ErrorUtils.parseError(response).toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<CompetitionsPostReponse> call, Throwable t) {
                        Log.d(TAG, "Error: " + t.toString());
                    }
                });

    }

//    public static void setFollowUser(String userId, final FollowRequestBody requestBody, final FollowUserCallback callback) {
//
//        getService()
//                .followUser(userId, requestBody)
//                .enqueue(new Callback<UserResponse>() {
//                    @Override
//                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                        if (response.isSuccessful()) {
//                            callback.onUserFollowSet(requestBody.follow);
//                        } else {
//                            callback.onUserFollowSetFailed(!requestBody.follow);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserResponse> call, Throwable t) {
//                        callback.onUserFollowSetFailed(!requestBody.follow);
//                    }
//                });
//
//    }

//    public static void updataUserDpUrl(String userId, final UserDataUpdateBody body, final UserDpUpdateRequestCallback callback) {
//
//        getService()
//                .updateUserDp(userId, body)
//                .enqueue(new Callback<UserResponse>() {
//                    @Override
//                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                        if (response.isSuccessful()) {
//                            callback.onUserDataUpdated();
//                        } else {
//                            callback.onUserDataUpdateError();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserResponse> call, Throwable t) {
//                        callback.onUserDataUpdateError();
//                    }
//                });
//
//    }

//    public static void updateUserBio(String userId, final UserBioUpdateRequestBody body, final UserBioUpdateRequestCallback callback) {
//
//        getService()
//                .updateUserBio(userId, body)
//                .enqueue(new Callback<UserResponse>() {
//                    @Override
//                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                        if (response.isSuccessful()) {
//                            callback.onBioUpdated();
//                        } else {
//                            callback.onBioUpdateError();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserResponse> call, Throwable t) {
//                        callback.onBioUpdateError();
//                    }
//                });
//
//    }

    public static void votePost(String postId, final VoteRequestBody body, final VotePostCallback callback) {

//        getService()
//                .votePost(postId, body)
//                .enqueue(new Callback<Feed>() {
//                    @Override
//                    public void onResponse(Call<Feed> call, Response<Feed> response) {
//                        if (response.isSuccessful()) {
//                            callback.onPostVoted(response.body());
//                        } else {
//                            callback.onPostVoteError();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Feed> call, Throwable t) {
//                        callback.onPostVoteError();
//                    }
//                });

    }

    public static void deletePost(String post_id, final int position, final OnPostDeleteCallback callback) {

        getService()
                .deletePost(post_id)
                .enqueue(new Callback<PostResponse>() {
                    @Override
                    public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onPostDeleted(position);
                        } else {
                            callback.onPostDeleteFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponse> call, Throwable t) {
                        callback.onPostDeleteFailed();
                    }
                });


    }

//    public static void requestUser(final int commentPosition, int user_id, final UserFetchCallback callback) {
//
//        L.D.m(TAG, "Fetching user.." + user_id);
//
//        getService()
//                .fetchUser(user_id)
//                .enqueue(new Callback<UserResponse>() {
//                    @Override
//                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                        if (response.isSuccessful()) {
//                            callback.onUserFetched(commentPosition, response.body());
//                        } else {
//                            callback.onUserFetchError();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserResponse> call, Throwable t) {
//                        callback.onUserFetchError();
//                    }
//                });
//
//    }

    public static void deleteVote(int postId, final VoteDeleteCallback callback) {

        getService().deleteVote(postId)
                .enqueue(new Callback<Feed>() {
                    @Override
                    public void onResponse(Call<Feed> call, Response<Feed> response) {
                        if (response.isSuccessful()) {
                            callback.onVoteDeleted(response.body());
                        } else {
                            Log.d(TAG, ErrorUtils.parseError(response).toString());
                            callback.onVoteDeleteError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Feed> call, Throwable t) {
                        Log.d(TAG, t.toString());
                        callback.onVoteDeleteError();
                    }
                });

    }

    public static void getUserStats(String userId, final UserStatsCallback callback) {

        getService().getUserStats(userId)
                .enqueue(new Callback<UserStatsModel>() {
                    @Override
                    public void onResponse(Call<UserStatsModel> call, Response<UserStatsModel> response) {
                        if (response.isSuccessful()) {
                            callback.onUserStatsFetched(response.body());
                        } else {
                            callback.onUserStatsFetchError();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserStatsModel> call, Throwable t) {
                        callback.onUserStatsFetchError();
                    }
                });

    }

    public static void getNotifications(int start, int limit, final NotificationCallback callback) {

        getService()
                .getNotifications(start, limit)
                .enqueue(new Callback<NotificationResponse>() {
                    @Override
                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onNotificationsFetched(response.body());
                        } else {
                            callback.onNotificationFetchError();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationResponse> call, Throwable t) {
                        callback.onNotificationFetchError();
                    }
                });

    }

    public static void markNotificationAsRead(int notification_id, final int pos, final MarkAsReadNotificationCallback callback) {

        getService()
                .markAsRead(notification_id)
                .enqueue(new Callback<NotificationResponse>() {
                    @Override
                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onNotificationMarkedAsRead(pos);
                        } else {
                            callback.onNotificationMarkAsReadFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationResponse> call, Throwable t) {
                        callback.onNotificationMarkAsReadFailed();
                    }
                });
    }

    public static void markAllNotificationAsRead(final MarkallAsReadNotificationCallback callback) {

        getService()
                .markAsAllRead()
                .enqueue(new Callback<NotificationResponse>() {
                    @Override
                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                        if (response.isSuccessful()) {
                            callback.markedAllRead();
                        } else {
                            callback.markAllReadFailed();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationResponse> call, Throwable t) {
                        callback.markAllReadFailed();
                    }
                });
    }
}
