package com.hapramp.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.RemoteMessage;
import com.hapramp.R;
import com.hapramp.main.HapRampMain;
import com.hapramp.notification.model.BaseNotificationModel;
import com.hapramp.notification.model.FollowNotificationModel;
import com.hapramp.notification.model.MentionNotificationModel;
import com.hapramp.notification.model.ReblogNotificationModel;
import com.hapramp.notification.model.ReplyNotificationModel;
import com.hapramp.notification.model.TransferNotificationModel;
import com.hapramp.notification.model.VoteNotificationModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.activity.AccountHistoryActivity;
import com.hapramp.ui.activity.DetailedActivity;
import com.hapramp.ui.activity.ProfileActivity;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ForegroundCheckTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.hapramp.ui.activity.AccountHistoryActivity.EXTRA_USERNAME;

public class NotificationHandler {
  public static final String CHANNEL_ID = "com.1ramp.notifications_channel";

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
          baseNotificationModel.setNotificationId(String.valueOf(System.currentTimeMillis()));
          FirebaseNotificationStore.saveNotification(baseNotificationModel);
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
            }
          }
        }
      }
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    catch (ExecutionException e) {
      e.printStackTrace();
    }
    catch (TimeoutException e) {
      e.printStackTrace();
    }
  }

  /**
   * Shows notification for following, on-click: Profile of follower opens up.
   *
   * @param notifId  notification Id of Notification
   * @param follower user who followed me
   */
  private static void showProfileDirectedNotification(String notifId, String follower) {
    Context context = HapRampMain.getContext();
    String title = "Follow";
    String content = follower + " started following you";
    PendingIntent pendingIntent = getProfilePendingIntent(context, notifId, follower);
    addNotificationToTray(context, pendingIntent, title, content);
  }

  /**
   * @param notificationId
   * @param reblogger      user who reblogged your post
   * @param permlink       permlink of your re-blogged post.
   */
  private static void showReblogDirectedNotification(String notificationId, String reblogger, String permlink) {
    Context context = HapRampMain.getContext();
    //you are the author of the post
    String author = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    String title = "Reblog";
    String content = reblogger + " shared your post";
    PendingIntent pendingIntent = getPostNotificationPendingIntent(context, notificationId, author,"", permlink);
    addNotificationToTray(HapRampMain.getContext(), pendingIntent, title, content);
  }

  /**
   * @param notificationId
   * @param commentor      user who created comment
   * @param permlink       permlink of new comment/reply.
   */
  private static void showCommentDirectedNotification(String notificationId, String commentor, String parentPermlink, String permlink) {
    Context context = HapRampMain.getContext();
    //you are the author of the post
    String author = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    String title = "Comment";
    String content = commentor + " commented on your post";
    PendingIntent pendingIntent = getPostNotificationPendingIntent(context, notificationId, author, parentPermlink, permlink);
    addNotificationToTray(HapRampMain.getContext(), pendingIntent, title, content);
  }

  /**
   * @param notificationId
   * @param voter          someone who voted your post
   * @param permlink       permlink of your post
   */
  private static void showVoteDirectedNotification(String notificationId, String voter, String permlink) {
    Context context = HapRampMain.getContext();
    //you are the author of the post
    String author = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    String title = "Vote";
    String content = voter + " voted your post";
    PendingIntent pendingIntent = getPostNotificationPendingIntent(context, notificationId, author, "", permlink);
    addNotificationToTray(HapRampMain.getContext(), pendingIntent, title, content);
  }

  /**
   * @param notificationId
   * @param sender         user who sent the amount.
   * @param memo           message attached with the transfer
   * @param amount         amount being transferred
   */
  private static void showTransferDirectedNotification(String notificationId, String sender, String memo, String amount) {
    Context context = HapRampMain.getContext();
    String title = "Transfer";
    String content = sender + " sent you " + amount + "\n\"" + memo + "\"\n";
    PendingIntent pendingIntent = getTransferPendingIntent(context, notificationId);
    addNotificationToTray(HapRampMain.getContext(), pendingIntent, title, content);
  }

  /**
   * @param notificationId
   * @param mentioner      user who mentioned you in his/her post.
   * @param permlink       permlink of post in which you were mentioned.
   */
  private static void showMentionDirectedNotification(String notificationId, String mentioner, String parentPermlink, String permlink) {
    Context context = HapRampMain.getContext();
    //you are the author of the post
    String author = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    String title = "Mention";
    String content = mentioner + " mentioned you in a post";
    PendingIntent pendingIntent = getPostNotificationPendingIntent(context, notificationId, author, parentPermlink, permlink);
    addNotificationToTray(HapRampMain.getContext(), pendingIntent, title, content);
  }

  private static PendingIntent getProfilePendingIntent(Context context, String notifId, String username) {
    Intent intent = new Intent(context, ProfileActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.EXTRAA_KEY_STEEM_USER_NAME, username);
    bundle.putString(Constants.EXTRAA_KEY_NOTIFICATION_ID, notifId);
    intent.putExtras(bundle);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addNextIntentWithParentStack(intent);
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public static void addNotificationToTray(Context context, PendingIntent pendingIntent, String title, String content) {
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.mipmap.hapramp_logo)
      .setContentTitle(title)
      .setContentText(content)
      .setContentIntent(pendingIntent)
      .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(content))
      .setAutoCancel(true)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    int notificationId = (int) System.currentTimeMillis();
    notificationManager.notify(notificationId, mBuilder.build());
  }

  private static PendingIntent getPostNotificationPendingIntent(Context context,
                                                                String notificationId,
                                                                String author,
                                                                String parentPermlink,
                                                                String permlink) {
    Intent intent = new Intent(context, DetailedActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.EXTRAA_KEY_NOTIFICATION_ID, notificationId);
    bundle.putString(Constants.EXTRAA_KEY_POST_AUTHOR, author);
    bundle.putString(Constants.EXTRAA_KEY_PARENT_PERMLINK, parentPermlink);
    bundle.putString(Constants.EXTRAA_KEY_POST_PERMLINK, permlink);
    intent.putExtras(bundle);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addNextIntentWithParentStack(intent);
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  private static PendingIntent getTransferPendingIntent(Context context, String notificationId) {
    Intent intent = new Intent(context, AccountHistoryActivity.class);
    Bundle bundle = new Bundle();
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    bundle.putString(EXTRA_USERNAME, username);
    bundle.putString(Constants.EXTRAA_KEY_NOTIFICATION_ID, notificationId);
    intent.putExtras(bundle);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addNextIntentWithParentStack(intent);
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
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
