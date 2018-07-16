package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hapramp.preferences.HaprampPreferenceManager;

public class Splash extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    testRPC();
    if (HaprampPreferenceManager.getInstance().isOnBoardingDone()) {
      navigateToLogin();
    } else {
      navigateToOnBoarding();
    }
  }

  public void testRPC() {

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
