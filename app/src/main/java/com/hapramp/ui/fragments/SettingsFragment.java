package com.hapramp.ui.fragments;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.analytics.EventReporter;
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
  Unbinder unbinder;
  @BindView(R.id.feedback_btn)
  TextView feedbackBtn;
  @BindView(R.id.logoutBtn)
  TextView logoutBtn;
  @BindView(R.id.tos)
  TextView tos;
  @BindView(R.id.invite_btn)
  TextView inviteBtn;
  @BindView(R.id.helpBtn)
  TextView helpBtn;
  private Context mContext;
  private ProgressDialog progressDialog;

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
    inviteBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        inviteAFriend();
      }
    });
    logoutBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showAlertDialogForLogout();
      }
    });
    helpBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openHelpPage();
      }
    });
    feedbackBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        shareFeedbackOrReportIssue();
      }
    });
    tos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openTermsPage();
      }
    });
  }

  private void openHelpPage() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1ramp.io/faq.html"));
    startActivity(browserIntent);
  }

  private void inviteAFriend() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT,
      "Join communities, share your work and earn rewards on 1Ramp." +
        " Steem powered social media for creators. Try it now! at https://goo.gl/AADhaC");
    intent.setType("text/plain");
    startActivity(Intent.createChooser(intent, "Invite a friend"));
  }

  private void openTermsPage() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1ramp.io/terms.html"));
    startActivity(browserIntent);
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

  private void logout() {
    EventReporter.addEvent(AnalyticsParams.EVENT_LOGOUT);
    EventReporter.reportEvent(mContext);
    revokeAccessToken();
  }

  private void revokeAccessToken() {
    if (!ConnectionUtils.isConnected(mContext)) {
      Toast.makeText(mContext, "Cannot revoke Token. Please connect to internet!",
        Toast.LENGTH_LONG).show();
      return;
    }
    final Handler mHanlder = new Handler();
    showLogoutProgress();
    final SteemConnect steemConnect = SteemConnectUtils.getSteemConnectInstance(
      HaprampPreferenceManager.getInstance().getSC2AccessToken()
    );
    HaprampPreferenceManager.getInstance().clearPreferences();
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
            Log.d("PostItemView", "Logout Error " + e.toString());
            mHanlder.post(new Runnable() {
              @Override
              public void run() {
                hideLogoutProgress();
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
