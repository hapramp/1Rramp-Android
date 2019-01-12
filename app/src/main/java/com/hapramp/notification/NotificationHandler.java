package com.hapramp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.hapramp.main.HapRampMain;
import com.hapramp.notification.model.BaseNotificationModel;
import com.hapramp.notification.model.ContestCreatedNotificationModel;
import com.hapramp.notification.model.ContestStartedNotificationModel;
import com.hapramp.notification.model.ContestWinnerDeclaredNotificationModel;
import com.hapramp.notification.model.FollowNotificationModel;
import com.hapramp.notification.model.MentionNotificationModel;
import com.hapramp.notification.model.ReblogNotificationModel;
import com.hapramp.notification.model.ReplyNotificationModel;
import com.hapramp.notification.model.TransferNotificationModel;
import com.hapramp.notification.model.VoteNotificationModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ForegroundCheckTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.hapramp.notification.SteemActionsNotificationHandler.CHANNEL_ID;
import static com.hapramp.notification.SteemActionsNotificationHandler.showCommentDirectedNotification;
import static com.hapramp.notification.SteemActionsNotificationHandler.showMentionDirectedNotification;
import static com.hapramp.notification.SteemActionsNotificationHandler.showProfileDirectedNotification;
import static com.hapramp.notification.SteemActionsNotificationHandler.showReblogDirectedNotification;
import static com.hapramp.notification.SteemActionsNotificationHandler.showTransferDirectedNotification;
import static com.hapramp.notification.SteemActionsNotificationHandler.showVoteDirectedNotification;

public class NotificationHandler {
  /**
   * This method parses -> attache notification id -> save to firebase database and notifies the user.
   *
   * @param remoteMessage payload as received from firebase
   */
  public static void handleNotification(RemoteMessage remoteMessage) {
    try {
      BaseNotificationModel baseNotificationModel = NotificationParser.parseNotification(remoteMessage.getData());
      if (baseNotificationModel != null) {
        if (baseNotificationModel.getType() != null) {
          //assign notification id
          baseNotificationModel.setNotificationId(String.valueOf(System.currentTimeMillis()));
          //save some notifications to firebase
          _saveNotification(baseNotificationModel);
          boolean isForeground = new ForegroundCheckTask().execute(HapRampMain.getContext()).get(2, TimeUnit.SECONDS);
          if (!isForeground && HaprampPreferenceManager.getInstance().shouldShowPushNotifications()) {
            switch (baseNotificationModel.getType()) {
              case NotificationKey.NOTIFICATION_TYPE_FOLLOW:

                showProfileDirectedNotification(
                  baseNotificationModel.getNotificationId(),
                  ((FollowNotificationModel) baseNotificationModel).follower);

                break;
              case NotificationKey.NOTIFICATION_TYPE_REBLOG:

                showReblogDirectedNotification(
                  baseNotificationModel.getNotificationId(),
                  ((ReblogNotificationModel) baseNotificationModel).account,
                  ((ReblogNotificationModel) baseNotificationModel).permlink
                );

                break;
              case NotificationKey.NOTIFICATION_TYPE_REPLY:

                showCommentDirectedNotification(
                  baseNotificationModel.getNotificationId(),
                  ((ReplyNotificationModel) baseNotificationModel).author,
                  ((ReplyNotificationModel) baseNotificationModel).parent_permlink,
                  ((ReplyNotificationModel) baseNotificationModel).permlink);

                break;
              case NotificationKey.NOTIFICATION_TYPE_VOTE:

                showVoteDirectedNotification(
                  baseNotificationModel.getNotificationId(),
                  ((VoteNotificationModel) baseNotificationModel).voter,
                  ((VoteNotificationModel) baseNotificationModel).permlink);

                break;
              case NotificationKey.NOTIFICATION_TYPE_TRANSFER:
                showTransferDirectedNotification(
                  baseNotificationModel.getNotificationId(),
                  ((TransferNotificationModel) baseNotificationModel).sender,
                  ((TransferNotificationModel) baseNotificationModel).memo,
                  ((TransferNotificationModel) baseNotificationModel).amount
                );
                break;

              case NotificationKey.NOTIFICATION_TYPE_MENTION:

                showMentionDirectedNotification(
                  baseNotificationModel.getNotificationId(),
                  ((MentionNotificationModel) baseNotificationModel).author,
                  ((MentionNotificationModel) baseNotificationModel).parent_permlink,
                  ((MentionNotificationModel) baseNotificationModel).permlink
                );
                break;

              case NotificationKey.NOTIFICATION_TYPE_CONTEST_CREATED:
                CompetitionNotificationHandler.showContestCreateNotification(
                  (ContestCreatedNotificationModel) baseNotificationModel);
                break;

              case NotificationKey.NOTIFICATION_TYPE_CONTEST_STARTED:
                CompetitionNotificationHandler.showContestStartedNotification((ContestStartedNotificationModel)
                  baseNotificationModel);
                break;

              case NotificationKey.NOTIFICATION_TYPE_CONTEST_WINNERS_ANNOUNCED:
                CompetitionNotificationHandler.showContestWinnerDeclaredNotification((ContestWinnerDeclaredNotificationModel) baseNotificationModel);
                break;
            }
          }
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * saves notifications which are valid/supported.
   *
   * @param baseNotificationModel notification object.
   */
  private static void _saveNotification(BaseNotificationModel baseNotificationModel) {
    if (baseNotificationModel.getType() != null) {
      switch (baseNotificationModel.getType()) {
        case NotificationKey.NOTIFICATION_TYPE_FOLLOW:
        case NotificationKey.NOTIFICATION_TYPE_REBLOG:
        case NotificationKey.NOTIFICATION_TYPE_REPLY:
        case NotificationKey.NOTIFICATION_TYPE_VOTE:
        case NotificationKey.NOTIFICATION_TYPE_TRANSFER:
        case NotificationKey.NOTIFICATION_TYPE_MENTION:
          FirebaseNotificationStore.saveNotification(baseNotificationModel);
        default:
          break;
      }
    }
  }

  public static void createNotificationChannel(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      CharSequence name = "1ramp_notification_channel";
      String description = "user_actions";
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
      channel.setDescription(description);
      // Register the channel with the system; you can't change the importance
      // or other notification behaviors after this
      NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
      notificationManager.createNotificationChannel(channel);
    }
  }
}
