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
import com.hapramp.analytics.EventReporter;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.CommunitiesCallback;
import com.hapramp.models.CommunityModel;
import com.hapramp.models.VerificationDataBody;
import com.hapramp.models.VerifiedToken;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.utils.AccessTokenValidator;
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
  private DataStore dataStore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    init();
    attachListeners();
  }

  @Override
  protected void onStart() {
    super.onStart();
    EventReporter.addEvent(AnalyticsParams.SCREEN_LOGIN);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ACCESS_TOKEN_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        //save token
        String accessToken = data.getStringExtra(Constants.EXTRA_ACCESS_TOKEN);
        String username = data.getStringExtra(Constants.EXTRA_USERNAME);
        HaprampPreferenceManager.getInstance()
          .setTokenExpireTime(AccessTokenValidator.getNextExpiryTime());
        HaprampPreferenceManager.getInstance().setSC2AccessToken(accessToken);
        requestUserTokenFromAppServer(accessToken, username);
      } else {
        showToast("Login Failed");
      }
    }
  }

  private void cacheAllCommunities(List<CommunityModel> communities) {
    for (int i = 0; i < communities.size(); i++) {
      CommunityModel cm = communities.get(i);
      HaprampPreferenceManager.getInstance().setCommunityTagToColorPair(cm.getmTag(), cm.getmColor());
      HaprampPreferenceManager.getInstance().setCommunityTagToNamePair(cm.getmTag(), cm.getmName());
    }
    HaprampPreferenceManager.getInstance()
      .saveAllCommunityListAsJson(new Gson()
        .toJson(new CommunityListWrapper(communities)));
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

  private void init() {
    progressDialog = new ProgressDialog(this);
    dataStore = new DataStore();
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

  private void requestUserTokenFromAppServer(final String userAccessToken, final String username) {
    showShadedProgress(getString(R.string.token_request_loading_message));
    VerificationDataBody verificationDataBody = new VerificationDataBody(userAccessToken, username);
    RetrofitServiceGenerator.getService().verifyUser(verificationDataBody).
      enqueue(new Callback<VerifiedToken>() {
      @Override
      public void onResponse(Call<VerifiedToken> call, Response<VerifiedToken> response) {
        hideShadedProgress();
        if (response.isSuccessful()) {
          HaprampPreferenceManager.getInstance().saveCurrentSteemUsername(username);
          HaprampPreferenceManager.getInstance().saveUserToken(response.body().token);
          HaprampPreferenceManager.getInstance().setLoggedIn(true);
          syncAllCommunities();
        } else {
          Crashlytics.log("LoginError:" + response.toString());
          Toast.makeText(LoginActivity.this, "Verification failed!!",
            Toast.LENGTH_LONG).show();
        }
        hideProgressDialog();
      }

      @Override
      public void onFailure(Call<VerifiedToken> call, Throwable t) {
        hideShadedProgress();
        Crashlytics.log("LoginError:" + t.toString());
        Toast.makeText(LoginActivity.this, "Verification failed!!",
          Toast.LENGTH_LONG).show();
        hideProgressDialog();
      }
    });
  }

  private void syncAllCommunities() {
    showShadedProgress(getString(R.string.loading_community_message));
    dataStore.requestAllCommunities(new CommunitiesCallback() {
      @Override
      public void onCommunityFetching() {

      }

      @Override
      public void onCommunitiesAvailable(List<CommunityModel> communityModelList, boolean isFreshData) {
        cacheAllCommunities(communityModelList);
        syncUserSelectedCommunity();
      }

      @Override
      public void onCommunitiesFetchError(String err) {
        navigateToCommunityPage();
      }
    });
  }

  private void hideProgressDialog() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  private void syncUserSelectedCommunity() {
    showShadedProgress(getString(R.string.loading_community_message));
    dataStore.requestUserCommunities(HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
      new CommunitiesCallback() {
        @Override
        public void onCommunityFetching() {
        }

        @Override
        public void onCommunitiesAvailable(List<CommunityModel> communities, boolean isFreshData) {
          hideShadedProgress();
          if (communities.size() > 0) {
            HaprampPreferenceManager
              .getInstance()
              .saveUserSelectedCommunitiesAsJson(new Gson().
                toJson(new CommunityListWrapper(communities)));
            navigateToHomePage();
          } else {
            navigateToCommunityPage();
          }
        }

        @Override
        public void onCommunitiesFetchError(String err) {
          showToast("Failed to get your communities. Try again!");
        }
      });
  }
}
