package com.hapramp.api;

import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.models.response.NotificationResponse;
import com.hapramp.models.response.UserModel;
import com.hapramp.interfaces.FullUserDetailsCallback;
import com.hapramp.interfaces.MarkAsReadNotificationCallback;
import com.hapramp.interfaces.MarkallAsReadNotificationCallback;
import com.hapramp.interfaces.NotificationCallback;
import com.hapramp.interfaces.VotePostCallback;
import com.hapramp.logger.L;
import com.hapramp.preferences.HaprampPreferenceManager;

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

  public static HaprampAPI getService() {
    String token = HaprampPreferenceManager.getInstance().getUserToken();
    return HaprampApiClient.getClient(token).create(HaprampAPI.class);
  }

  public static void votePost(String postId, final VoteRequestBody body, final VotePostCallback callback) {
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
