package com.hapramp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.activity.LoginActivity;
import com.hapramp.utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SettingsFragment extends Fragment {
  @BindView(R.id.logoutIcon)
  TextView logoutIcon;
  Unbinder unbinder;
  @BindView(R.id.logoutContainer)
  LinearLayout logoutContainer;
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
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    logoutIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    logoutContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showAlertDialogForLogout();
      }
    });
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
