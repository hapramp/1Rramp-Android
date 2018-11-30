package com.hapramp.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.hapramp.main.HapRampMain;
import com.hapramp.ui.activity.HomeActivity;
import com.hapramp.ui.activity.WinnersFeedListActivity;

import static com.hapramp.notification.SteemActionsNotificationHandler.addNotificationToTray;
import static com.hapramp.ui.activity.HomeActivity.EXTRA_TAB_INDEX;
import static com.hapramp.ui.activity.WinnersFeedListActivity.EXTRA_COMPETITION_ID;
import static com.hapramp.ui.activity.WinnersFeedListActivity.EXTRA_COMPETITION_TITLE;

public class CompetitionNotificationHandler {

  /**
   * adds a notification to tray, on clicking it takes user to competition listing page.
   *
   * @param title       title for the notification.
   * @param description description of the notification.
   */
  public static void showCompetitionWinnersListDirectedNotification(String compId, String compTitle, String title, String description) {
    Context context = HapRampMain.getContext();
    PendingIntent pendingIntent = getCompetitionWinnersListPendingIntent(context, compId, compTitle);
    addNotificationToTray(context, pendingIntent, title, description);
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

  /**
   * adds a notification to tray, on clicking it takes user to competition listing page.
   *
   * @param title       title for the notification.
   * @param description description of the notification.
   */
  public static void showCompetitionListingDirectedNotification(String title, String description) {
    Context context = HapRampMain.getContext();
    PendingIntent pendingIntent = getCompetitionListingPendingIntent(context);
    addNotificationToTray(context, pendingIntent, title, description);
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
}
