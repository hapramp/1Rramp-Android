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

		public static void votePost(String postId, final VoteRequestBody body, final VotePostCallback callback) { }

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
