package com.hapramp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.activity.LoginActivity;

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
  private Context mContext;

  public SettingsFragment() {
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    AnalyticsUtil.getInstance(getActivity()).setCurrentScreen((Activity) context, AnalyticsParams.SCREEN_SETTINGS, null);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_settings, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;

  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    logoutBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showAlertDialogForLogout();
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

  private void openTermsPage() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1ramp.io/terms.html"));
    startActivity(browserIntent);
  }

  private void showAlertDialogForLogout() {
    new AlertDialog.Builder(mContext)
      .setTitle("Logout")
      .setMessage("Do you want to Logout ? ")
      .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          logout();
        }
      })
      .setNegativeButton("Cancel", null)
      .show();
  }

  private void logout() {
    //clear preferences
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_LOGOUT);
    HaprampPreferenceManager.getInstance().clearPreferences();
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
