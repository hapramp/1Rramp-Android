package com.hapramp.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.hapramp.R;
import com.hapramp.datastore.UrlBuilder;
import com.hapramp.main.HapRampMain;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.activity.AccountHistoryActivity;
import com.hapramp.ui.activity.DetailedActivity;
import com.hapramp.ui.activity.ProfileActivity;
import com.hapramp.utils.Constants;

import static com.hapramp.ui.activity.AccountHistoryActivity.EXTRA_USERNAME;

public class SteemActionsNotificationHandler {
  public static final String CHANNEL_ID = "com.1ramp.notifications_channel";

  /**
   * Shows notification for following, on-click: Profile of follower opens up.
   *
   * @param notifId  notification Id of Notification
   * @param follower user who followed me
   */
  public static void showProfileDirectedNotification(String notifId, String follower) {
    Context context = HapRampMain.getContext();
    String title = follower;
    String content = follower + " started following you";
    String photoUrl = UrlBuilder.getUserProfileUrl(follower);
    PendingIntent pendingIntent = getProfilePendingIntent(context, notifId, follower);
    addNotificationToTray(context, photoUrl, pendingIntent, title, content);
  }

  public static PendingIntent getProfilePendingIntent(Context context, String notifId, String username) {
    Intent intent = new Intent(context, ProfileActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.EXTRAA_KEY_STEEM_USER_NAME, username);
    bundle.putString(Constants.EXTRAA_KEY_NOTIFICATION_ID, notifId);
    intent.putExtras(bundle);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addNextIntentWithParentStack(intent);
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public static void addNotificationToTray(final Context context, final String photoUrl, final PendingIntent pendingIntent, final String title, final String content) {
    final Handler mHandler = new Handler();
    new Thread() {
      @Override
      public void run() {
        try {
          FutureTarget<Bitmap> futureTarget = Glide.with(context)
            .asBitmap()
            .load(photoUrl)
            .submit();
          final Bitmap bitmap = futureTarget.get();
          Glide.with(context).clear(futureTarget);
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              postNotificationWithBitmap(context, bitmap, title, content, pendingIntent);
            }
          });
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  private static void postNotificationWithBitmap(Context context, Bitmap bitmap, String title, String content, PendingIntent pendingIntent) {
    long when = System.currentTimeMillis();
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.mipmap.hapramp_logo)
      .setContentTitle(title)
      .setContentText(content)
      .setContentIntent(pendingIntent)
      .setLargeIcon(bitmap)
      .setAutoCancel(true)
      .setWhen(when)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mBuilder.setSmallIcon(R.drawable.logo_white_72);
      mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
    } else {
      mBuilder.setSmallIcon(R.drawable.logo_white_24);
    }

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    int notificationId = (int) System.currentTimeMillis();
    notificationManager.notify(notificationId, mBuilder.build());
  }

  /**
   * @param notificationId
   * @param reblogger      user who reblogged your post
   * @param permlink       permlink of your re-blogged post.
   */
  public static void showReblogDirectedNotification(String notificationId, String reblogger, String permlink) {
    Context context = HapRampMain.getContext();
    //you are the author of the post
    String author = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    String title = reblogger;
    String photoUrl = UrlBuilder.getUserProfileUrl(reblogger);
    String content = reblogger + " reposted your post";
    PendingIntent pendingIntent = getPostNotificationPendingIntent(context, notificationId, author, "", permlink);
    addNotificationToTray(HapRampMain.getContext(), photoUrl, pendingIntent, title, content);
  }

  public static PendingIntent getPostNotificationPendingIntent(Context context,
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

  /**
   * @param notificationId
   * @param commentor      user who created comment
   * @param permlink       permlink of new comment/reply.
   *                       Clicking on notification, opens the comment/reply created by commentor.
   */
  public static void showCommentDirectedNotification(String notificationId, String commentor, String parentPermlink, String permlink) {
    Context context = HapRampMain.getContext();
    String title = commentor;
    String content = commentor + " commented on your post";
    String photoUrl = UrlBuilder.getUserProfileUrl(commentor);
    PendingIntent pendingIntent = getPostNotificationPendingIntent(context, notificationId, commentor, parentPermlink, permlink);
    addNotificationToTray(HapRampMain.getContext(), photoUrl, pendingIntent, title, content);
  }

  /**
   * @param notificationId
   * @param voter          someone who voted your post
   * @param permlink       permlink of your post
   */
  public static void showVoteDirectedNotification(String notificationId, String voter, String permlink) {
    Context context = HapRampMain.getContext();
    //you are the author of the post
    String author = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    String title = voter;
    String content = voter + " voted your post";
    String photoUrl = UrlBuilder.getUserProfileUrl(voter);
    PendingIntent pendingIntent = getPostNotificationPendingIntent(context, notificationId, author, "", permlink);
    addNotificationToTray(HapRampMain.getContext(), photoUrl, pendingIntent, title, content);
  }

  /**
   * @param notificationId
   * @param sender         user who sent the amount.
   * @param memo           message attached with the transfer
   * @param amount         amount being transferred
   */
  public static void showTransferDirectedNotification(String notificationId, String sender, String memo, String amount) {
    Context context = HapRampMain.getContext();
    String title = sender;
    String photoUrl = UrlBuilder.getUserProfileUrl(sender);
    String content = sender + " sent you " + amount + "\n\"" + memo + "\"\n";
    PendingIntent pendingIntent = getTransferPendingIntent(context, notificationId);
    addNotificationToTray(HapRampMain.getContext(), photoUrl, pendingIntent, title, content);
  }

  public static PendingIntent getTransferPendingIntent(Context context, String notificationId) {
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

  /**
   * @param notificationId
   * @param mentioner      user who mentioned you in his/her post.
   * @param permlink       permlink of post in which you were mentioned.
   *                       Clicking on notification, opens post in which user is mentioned
   */
  public static void showMentionDirectedNotification(String notificationId, String mentioner, String parentPermlink, String permlink) {
    Context context = HapRampMain.getContext();
    String title = mentioner;
    String photoUrl = UrlBuilder.getUserProfileUrl(mentioner);
    String content = mentioner + " mentioned you in a post";
    PendingIntent pendingIntent = getPostNotificationPendingIntent(context, notificationId, mentioner, parentPermlink, permlink);
    addNotificationToTray(HapRampMain.getContext(), photoUrl, pendingIntent, title, content);
  }
}
