package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.EventReporter;
import com.hapramp.notification.FirebaseNotificationStore;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.fragments.ProfileFragment;
import com.hapramp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
  @BindView(R.id.contentPlaceHolder)
  FrameLayout contentPlaceHolder;
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.profile_user_name)
  TextView profileUserName;
  @BindView(R.id.toolbar_container)
  RelativeLayout toolbarContainer;
  private String username;
  private FragmentManager fragmentManager;
  private ProfileFragment profileFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    ButterKnife.bind(this);
    init();
    EventReporter.addEvent(AnalyticsParams.SCREEN_PROFILE);
  }

  private void init() {
    fragmentManager = getSupportFragmentManager();
    username = getIntent().getExtras().getString(Constants.EXTRAA_KEY_STEEM_USER_NAME,
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    String notifId = getIntent().getExtras().getString(Constants.EXTRAA_KEY_NOTIFICATION_ID, null);
    if (notifId != null) {
      FirebaseNotificationStore.markAsRead(notifId);
    }
    profileFragment = new ProfileFragment();
    profileFragment.setUsername(username);
    transactFragment();
    attachListeners();
  }

  private void transactFragment() {
    fragmentManager.beginTransaction()
      .replace(R.id.contentPlaceHolder, profileFragment)
      .commit();
  }

  private void attachListeners() {
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ProfileActivity.super.onBackPressed();
      }
    });
  }
}
