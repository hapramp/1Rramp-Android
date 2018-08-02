package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.datamodels.VerificationDataBody;
import com.hapramp.datamodels.VerifiedToken;
import com.hapramp.datamodels.response.UserModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.utils.Constants;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.viewmodel.common.ConnectivityViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
  public static final String TAG = LoginActivity.class.getSimpleName();
  private static final int ACCESS_TOKEN_REQUEST_CODE = 109;
  @BindView(R.id.connectivityText)
  TextView connectivityText;
  @BindView(R.id.loginButton)
  TextView loginButton;
  @BindView(R.id.createSteemAccountBtn)
  TextView createSteemAccountBtn;
  @BindView(R.id.progress_container)
  RelativeLayout progressContainer;
  @BindView(R.id.shaded_progress_message)
  TextView shadedProgressMessage;

  private ConnectivityViewModel connectivityViewModel;
  private SteemConnect steemConnect;
  private ProgressDialog progressDialog;
  private String loginUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    init();
    attachListeners();
    checkLastLoginAndMoveAhead();
  }

  @Override
  protected void onStart() {
    super.onStart();
    AnalyticsUtil.getInstance(this).setCurrentScreen(this, AnalyticsParams.SCREEN_LOGIN, null);
  }

  private void checkLastLoginAndMoveAhead() {
    if (HaprampPreferenceManager.getInstance().isLoggedIn()) {
      if (HaprampPreferenceManager.getInstance().getUserSelectedCommunityAsJson().length() == 0) {
        syncUserAccount();
      } else {
        navigateToHomePage();
      }
    }
  }

  private void init() {
    Crashlytics.setString(CrashReporterKeys.UI_ACTION, "login init");
    progressDialog = new ProgressDialog(this);
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
  }

  private void syncUserAccount() {
    showShadedProgress("Trying to know your communities...");
    RetrofitServiceGenerator.getService()
      .fetchUserCommunities(HaprampPreferenceManager.getInstance().getCurrentSteemUsername()).enqueue(new Callback<UserModel>() {
      @Override
      public void onResponse(Call<UserModel> call, Response<UserModel> response) {
        hideShadedProgress();
        if (response.isSuccessful()) {
          List<CommunityModel> communityModels = response.body().communityModels;
          if (communityModels.size() > 0) {
            HaprampPreferenceManager
              .getInstance()
              .saveUserSelectedCommunitiesAsJson(new Gson().toJson(new CommunityListWrapper(response.body().communityModels)));
            navigateToHomePage();
          } else {
            navigateToCommunityPage();
          }
        } else {
          showToast("Failed to get your communites");
        }
      }

      @Override
      public void onFailure(Call<UserModel> call, Throwable t) {
        hideShadedProgress();
        showToast("Failed to get your communites");
      }
    });
  }

  private void navigateToHomePage() {
    Intent i = new Intent(this, HomeActivity.class);
    startActivity(i);
    finish();
  }

  private void enableSigninButton() {
    loginButton.setEnabled(true);
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

  private void showConnectivityErrorBar() {
    connectivityText.setVisibility(View.VISIBLE);
    connectivityText.setText(R.string.connectivity_lost);
    connectivityText.setBackgroundColor(getResources().getColor(R.color.ConnectivityRed));
  }

  private void navigateToWebLogin() {
    try {
      loginUrl = steemConnect.getLoginUrl(false);
      Intent i = new Intent(this, WebloginActivity.class);
      i.putExtra(Constants.EXTRA_LOGIN_URL, loginUrl);
      startActivityForResult(i, ACCESS_TOKEN_REQUEST_CODE);
    }
    catch (SteemConnectException e) {
      e.printStackTrace();
    }
  }

  private void openSteemitSignUp() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.steemit_signup)));
    startActivity(browserIntent);
  }

  private void showShadedProgress(String msg) {
    if (progressContainer != null) {
      shadedProgressMessage.setText(msg);
      progressContainer.setVisibility(View.VISIBLE);
    }
  }

  private void hideShadedProgress() {
    if (progressContainer != null) {
      progressContainer.setVisibility(View.GONE);
    }
  }

  private void navigateToCommunityPage() {
    Intent i = new Intent(this, CommunitySelectionActivity.class);
    startActivity(i);
    finish();
  }

  private void showToast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ACCESS_TOKEN_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        //save token
        String accessToken = data.getStringExtra(Constants.EXTRA_ACCESS_TOKEN);
        String username = data.getStringExtra(Constants.EXTRA_USERNAME);
        HaprampPreferenceManager.getInstance().setSC2AccessToken(accessToken);
        requestUserTokenFromAppServer(accessToken, username);
      } else {
        showToast("Login Failed");
      }
    }
  }

  private void requestUserTokenFromAppServer(final String userAccessToken, final String username) {
    showShadedProgress("Preparing for your security...");
    VerificationDataBody verificationDataBody = new VerificationDataBody(userAccessToken, username);
    RetrofitServiceGenerator.getService().verifyUser(verificationDataBody).enqueue(new Callback<VerifiedToken>() {
      @Override
      public void onResponse(Call<VerifiedToken> call, Response<VerifiedToken> response) {
        hideShadedProgress();
        if (response.isSuccessful()) {
          //save token
          HaprampPreferenceManager.getInstance().saveCurrentSteemUsername(username);
          HaprampPreferenceManager.getInstance().saveUserToken(response.body().token);
          HaprampPreferenceManager.getInstance().setLoggedIn(true);
          syncUserAccount();
        } else {
          Toast.makeText(LoginActivity.this, "Verification failed!!", Toast.LENGTH_LONG).show();
        }
        hideProgressDialog();
      }

      @Override
      public void onFailure(Call<VerifiedToken> call, Throwable t) {
        Toast.makeText(LoginActivity.this, "Verification failed!!", Toast.LENGTH_LONG).show();
        hideProgressDialog();
      }
    });
  }

  private void hideProgressDialog() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  private void openSteemitInNativeBrowser() {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.steemit_url)));
    startActivity(browserIntent);
  }

  private void showProgressDialog() {
    progressDialog.setTitle("Verification");
    progressDialog.setMessage("Getting ready your account...");
    progressDialog.show();
  }

}
