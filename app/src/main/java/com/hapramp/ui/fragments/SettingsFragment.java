package com.hapramp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.EventReporter;
import com.hapramp.notification.NotificationSubscriber;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.ui.activity.LoginActivity;
import com.hapramp.utils.ConnectionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SettingsFragment extends Fragment {
  @BindView(R.id.img_notification)
  ImageView imgNotification;
  @BindView(R.id.push_notification_row)
  RelativeLayout pushNotificationRow;
  @BindView(R.id.img_friend)
  ImageView imgFriend;
  @BindView(R.id.invite_a_friend_row)
  RelativeLayout inviteAFriendRow;
  @BindView(R.id.img_feedback)
  ImageView imgFeedback;
  @BindView(R.id.feedback_row)
  RelativeLayout feedbackRow;
  @BindView(R.id.img_help)
  ImageView imgHelp;
  @BindView(R.id.help_row)
  RelativeLayout helpRow;
  @BindView(R.id.img_terms)
  ImageView imgTerms;
  @BindView(R.id.term_row)
  RelativeLayout termRow;
  @BindView(R.id.img_logout)
  ImageView imgLogout;
  @BindView(R.id.logout_row)
  RelativeLayout logoutRow;
  @BindView(R.id.push_notification_switch)
  Switch pushNotificationSwitch;
  private Context mContext;
  private ProgressDialog progressDialog;
  private Unbinder unbinder;

  public SettingsFragment() {
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    EventReporter.addEvent(AnalyticsParams.SCREEN_SETTINGS);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, container, false);
    initProgressDialog();
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  private void initProgressDialog() {
    progressDialog = new ProgressDialog(mContext);
    progressDialog.setCancelable(false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (HaprampPreferenceManager.getInstance().shouldShowPushNotifications()) {
      pushNotificationSwitch.setChecked(true);
    } else {
      pushNotificationSwitch.setChecked(false);
    }
    inviteAFriendRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        inviteAFriend();
      }
    });
    logoutRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showAlertDialogForLogout();
      }
    });
    helpRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openHelpPage();
      }
    });
    feedbackRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        shareFeedbackOrReportIssue();
      }
    });
    termRow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openTermsPage();
      }
    });

    pushNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        showPushNotifications(isChecked);
      }
    });
  }

  private void inviteAFriend() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT,
      "Join communities, share your work and earn rewards on 1Ramp." +
        " Steem powered social media for creators. Try it now! at https://goo.gl/AADhaC");
    intent.setType("text/plain");
    startActivity(Intent.createChooser(intent, "Invite a friend"));
  }

  private void showAlertDialogForLogout() {
    new AlertDialog.Builder(mContext)
      .setTitle("Logout")
      .setMessage("Do you want to Logout ? ")
      .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          logout();
        }
      })
      .setNegativeButton("Cancel", null)
      .show();
  }

  private void openHelpPage() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1ramp.io/faq.html"));
    startActivity(browserIntent);
  }

  private void shareFeedbackOrReportIssue() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    String[] recipients = {"feedback@1ramp.io"};
    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
    intent.putExtra(Intent.EXTRA_SUBJECT, "[Issue/Feedback] Regarding 1ramp Android App.");
    intent.putExtra(Intent.EXTRA_TEXT, "");
    intent.setType("text/html");
    intent.setPackage("com.google.android.gm");
    startActivity(Intent.createChooser(intent, "Send mail"));
  }

  private void openTermsPage() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1ramp.io/terms.html"));
    startActivity(browserIntent);
  }

  private void showPushNotifications(boolean subscribe) {
    HaprampPreferenceManager.getInstance().setShowPushNotifications(subscribe);
  }

  private void logout() {
    EventReporter.addEvent(AnalyticsParams.EVENT_LOGOUT);
    EventReporter.reportEventSession(mContext);
    revokeAccessToken();
  }

  private void revokeAccessToken() {
    final Handler mHanlder = new Handler();
    showLogoutProgress();
    final SteemConnect steemConnect = SteemConnectUtils.getSteemConnectInstance(
      HaprampPreferenceManager.getInstance().getSC2AccessToken()
    );
    NotificationSubscriber.unsubscribeForUserTopic();
    NotificationSubscriber.unsubscribeForNewCompetition();
    HaprampPreferenceManager.getInstance().clearPreferences();

    if (!ConnectionUtils.isConnected(mContext)) {
      hideLogoutProgress();
      navigateToLoginPage();
      return;
    }

    new Thread() {
      @Override
      public void run() {
        steemConnect.revokeToken(new SteemConnectCallback() {
          @Override
          public void onResponse(String s) {
            mHanlder.post(
              new Runnable() {
                @Override
                public void run() {
                  hideLogoutProgress();
                  navigateToLoginPage();
                }
              }
            );
          }

          @Override
          public void onError(final SteemConnectException e) {
            mHanlder.post(new Runnable() {
              @Override
              public void run() {
                hideLogoutProgress();
                navigateToLoginPage();
              }
            });
          }
        });
      }
    }.start();
  }

  private void showLogoutProgress() {
    if (progressDialog != null) {
      progressDialog.setMessage("Logging out...");
      progressDialog.show();
    }
  }

  private void hideLogoutProgress() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  private void navigateToLoginPage() {
    Intent intent = new Intent(mContext, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }
}
