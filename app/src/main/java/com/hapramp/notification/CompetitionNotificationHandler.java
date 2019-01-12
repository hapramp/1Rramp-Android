package com.hapramp.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.hapramp.R;
import com.hapramp.main.HapRampMain;
import com.hapramp.notification.model.ContestCreatedNotificationModel;
import com.hapramp.notification.model.ContestStartedNotificationModel;
import com.hapramp.notification.model.ContestWinnerDeclaredNotificationModel;
import com.hapramp.ui.activity.HomeActivity;
import com.hapramp.ui.activity.ParticipateEditorActivity;
import com.hapramp.ui.activity.WinnersFeedListActivity;

import static com.hapramp.notification.SteemActionsNotificationHandler.CHANNEL_ID;
import static com.hapramp.ui.activity.HomeActivity.EXTRA_TAB_INDEX;
import static com.hapramp.ui.activity.ParticipateEditorActivity.EXTRA_COMPETITION_HASHTAG;
import static com.hapramp.ui.activity.WinnersFeedListActivity.EXTRA_COMPETITION_ID;
import static com.hapramp.ui.activity.WinnersFeedListActivity.EXTRA_COMPETITION_TITLE;

public class CompetitionNotificationHandler {


  public static void showContestStartedNotification(ContestStartedNotificationModel contestStartedNotificationModel) {
    Context context = HapRampMain.getContext();
    PendingIntent pendingIntent = getCompetitionParticipatePendingIntent(context,
      contestStartedNotificationModel.getTag(),
      contestStartedNotificationModel.getId(),
      contestStartedNotificationModel.getTitle());

    addNotificationToTray(context,
      contestStartedNotificationModel.getImage(),
      pendingIntent,
      "Contest is live!! Participate now",
      contestStartedNotificationModel.getTitle());
  }

  public static void addNotificationToTray(final Context context, final String photoUrl, final PendingIntent pendingIntent, final String title, final String content) {
    try {
      FutureTarget<Bitmap> futureTarget = Glide.with(context)
        .asBitmap()
        .load(photoUrl)
        .submit();
      final Bitmap bitmap = futureTarget.get();
      Glide.with(context).clear(futureTarget);
      postNotificationWithBitmap(context,
        bitmap,
        title,
        content,
        pendingIntent);
    }
    catch (Exception e) {
      e.printStackTrace();
      postNotificationWithBitmap(context,
        null,
        title,
        content,
        pendingIntent);
    }
  }

  private static void postNotificationWithBitmap(Context context, Bitmap bitmap, String title, String content, PendingIntent pendingIntent) {
    long when = System.currentTimeMillis();
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.mipmap.hapramp_logo)
      .setContentTitle(title)
      .setContentText(content)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)
      .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setBigContentTitle(title))
      .setWhen(when)
      .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      mBuilder.setSmallIcon(R.drawable.logo_white_72);
      mBuilder.setCategory(Notification.CATEGORY_SOCIAL);
      mBuilder.setColor(context.getResources().getColor(R.color.colorPrimary));
    } else {
      mBuilder.setSmallIcon(R.drawable.logo_white_24);
    }

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    int notificationId = (int) System.currentTimeMillis();
    notificationManager.notify(notificationId, mBuilder.build());
  }

  public static void showContestWinnerDeclaredNotification(ContestWinnerDeclaredNotificationModel contestWinnerDeclaredNotificationModel) {
    Context context = HapRampMain.getContext();
    PendingIntent pendingIntent = getCompetitionWinnersListPendingIntent(context,
      contestWinnerDeclaredNotificationModel.getId(),
      contestWinnerDeclaredNotificationModel.getTitle());
    addNotificationToTray(context,
      contestWinnerDeclaredNotificationModel.getImage(),
      pendingIntent,
      "Winners declared",
      contestWinnerDeclaredNotificationModel.getTitle());
  }

  /**
   * @param context Application context.
   * @return Pending Intent for competition.
   */
  private static PendingIntent getCompetitionWinnersListPendingIntent(Context context, String compId, String compTitle) {
    Intent intent = new Intent(context, WinnersFeedListActivity.class);
    Bundle bundle = new Bundle();
    intent.putExtras(bundle);
    intent.putExtra(EXTRA_COMPETITION_ID, compId);
    intent.putExtra(EXTRA_COMPETITION_TITLE, compTitle);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addNextIntentWithParentStack(intent);
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public static void showContestCreateNotification(ContestCreatedNotificationModel contestCreatedNotificationModel) {
    Context context = HapRampMain.getContext();
    PendingIntent pendingIntent = getCompetitionListingPendingIntent(context);
    addNotificationToTray(context,
      contestCreatedNotificationModel.getImage(),
      pendingIntent,
      "New contest coming up!",
      contestCreatedNotificationModel.getTitle());
  }

  /**
   * @param context Application context.
   * @return Pending Intent for competition.
   */
  public static PendingIntent getCompetitionListingPendingIntent(Context context) {
    Intent intent = new Intent(context, HomeActivity.class);
    Bundle bundle = new Bundle();
    intent.putExtras(bundle);
    intent.putExtra(EXTRA_TAB_INDEX, 1);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addNextIntentWithParentStack(intent);
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  /**
   * @param context Application context.
   * @return Pending Intent for competition.
   */
  public static PendingIntent getCompetitionParticipatePendingIntent(Context context, String hashtag, String id, String title) {
    Intent intent = new Intent(context, ParticipateEditorActivity.class);
    intent.putExtra(EXTRA_COMPETITION_HASHTAG, hashtag);
    intent.putExtra(EXTRA_COMPETITION_ID, id);
    intent.putExtra(EXTRA_COMPETITION_TITLE, title);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addNextIntentWithParentStack(intent);
    return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
  }
}
