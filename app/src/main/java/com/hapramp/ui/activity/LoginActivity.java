package com.hapramp.ui.activity;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.utils.Constants;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.utils.PixelUtils;
import com.hapramp.viewmodel.common.ConnectivityViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
		public static final String TAG = LoginActivity.class.getSimpleName();
		private static final int ACCESS_TOKEN_REQUEST_CODE = 109;
		@BindView(R.id.connectivityText)
		TextView connectivityText;
		@BindView(R.id.loginButton)
		TextView loginButton;
		@BindView(R.id.createSteemAccountBtn)
		TextView createSteemAccountBtn;
		@BindView(R.id.helpBtn)
		TextView helpBtn;
		private ConnectivityViewModel connectivityViewModel;
		private SteemConnect steemConnect;
		private String loginUrl;

		@Override
		protected void onStart() {
				super.onStart();
				AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_LOGIN, null);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_login);
				ButterKnife.bind(this);
				checkLastLoginAndMoveAhead();
				init();
				attachListeners();
		}

		private void checkLastLoginAndMoveAhead() {
				if (HaprampPreferenceManager.getInstance().isLoggedIn()) {
						if (HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson().length() == 0) {
								navigateToCommunityPage();
						} else {
								navigateToHomePage();
						}
				}
		}

		private void init() {
				Crashlytics.setString(CrashReporterKeys.UI_ACTION, "login init");
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				steemConnect = SteemConnectUtils.getSteemConnectInstance();
				connectivityViewModel = ViewModelProviders.of(this).get(ConnectivityViewModel.class);
				connectivityViewModel.getConnectivityState().observeForever(new Observer<Boolean>() {
						@Override
						public void onChanged(@Nullable Boolean isConnected) {
								if (isConnected) {
										enableSigninButton();
										showConnectivityBar();
								} else {
										disableSigninButton();
										showConnectivityErrorBar();
								}
						}
				});
		}

		private void attachListeners() {
				loginButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								navigateToWebLogin();
						}
				});
				createSteemAccountBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								openSteemitSignUp();
						}
				});
				helpBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								showLoginHelpDailog();
						}
				});
		}

		private void navigateToWebLogin() {
				try {
						loginUrl = steemConnect.getLoginUrl();
						Intent i = new Intent(this, WebloginActivity.class);
						i.putExtra(Constants.EXTRA_LOGIN_URL, loginUrl);
						startActivityForResult(i, ACCESS_TOKEN_REQUEST_CODE);
				}
				catch (SteemConnectException e) {
						e.printStackTrace();
				}
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				if (requestCode == ACCESS_TOKEN_REQUEST_CODE) {
						if (resultCode == RESULT_OK) {
								//save token
								HaprampPreferenceManager.getInstance().saveSteemConnectAcessToken(data.getStringExtra(Constants.EXTRA_ACCESS_TOKEN));
								navigateToCommunityPage();
						} else {
								showToast("Login Failed");
						}
				}
		}

		private void navigateToHomePage() {
				Intent i = new Intent(this, HomeActivity.class);
				startActivity(i);
				finish();
		}

		private void navigateToCommunityPage() {
				Intent i = new Intent(this, CommunitySelectionActivity.class);
				startActivity(i);
				finish();
		}

		private void showToast(String msg) {
				Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		}

		private void showLoginHelpDailog() {
				final Dialog dialog = new Dialog(this);
				View v = LayoutInflater.from(this).inflate(R.layout.login_info_dialog_view, null);
				TextView cancel = v.findViewById(R.id.cancelHelpDialog);
				TextView gotoSteemIt = v.findViewById(R.id.gotoSteemit);
				cancel.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								dialog.dismiss();
						}
				});
				gotoSteemIt.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								openSteemitInNativeBrowser();
						}
				});
				dialog.setContentView(v);
				dialog.setCancelable(false);
				dialog.show();
				dialog.getWindow().setLayout((PixelUtils.getDimension(this).widthPixels), LinearLayout.LayoutParams.WRAP_CONTENT);
		}

		private void openSteemitInNativeBrowser() {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.steemit_url)));
				startActivity(browserIntent);
		}

		private void openSteemitSignUp() {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.steemit_signup)));
				startActivity(browserIntent);
		}

		private void showConnectivityErrorBar() {
				connectivityText.setVisibility(View.VISIBLE);
				connectivityText.setText(R.string.connectivity_lost);
				connectivityText.setBackgroundColor(getResources().getColor(R.color.ConnectivityRed));
		}

		private void showConnectivityBar() {
				connectivityText.setVisibility(View.VISIBLE);
				connectivityText.setText(R.string.connectivity_established);
				connectivityText.setBackgroundColor(getResources().getColor(R.color.ConnectivityGreen));
				new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
								connectivityText.setVisibility(View.GONE);
						}
				}, 4000);
		}

		private void disableSigninButton() {
				loginButton.setEnabled(false);
		}

		private void enableSigninButton() {
				loginButton.setEnabled(true);
		}

}
