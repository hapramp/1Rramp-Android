package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hapramp.analytics.EventReporter;
import com.hapramp.notification.NotificationHandler;
import com.hapramp.preferences.DataStoreCachePreference;
import com.hapramp.preferences.HaprampPreferenceManager;

public class Splash extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    performTasksDelayed();
    DataStoreCachePreference.getInstance();
    NotificationHandler.createNotificationChannel(this);
    if (HaprampPreferenceManager.getInstance().isLoggedIn()) {
      navigateToHomePage();
      return;
    }
    if (HaprampPreferenceManager.getInstance().isOnBoardingDone()) {
      navigateToLogin();
    } else {
      navigateToOnBoarding();
    }
  }

  private void performTasksDelayed() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        EventReporter.reportDeviceId();
        EventReporter.reportOpenEvent();
      }
    }, 4000);
  }

  private void navigateToHomePage() {
    Intent i = new Intent(this, CommunitySelectionActivity.class);
    startActivity(i);
    finish();
  }

  private void navigateToLogin() {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
    finish();
  }

  private void navigateToOnBoarding() {
    Intent intent = new Intent(this, OnBoardingActivity.class);
    startActivity(intent);
    finish();
  }
}
