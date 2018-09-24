package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.analytics.EventReporter;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.JSONParser;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.User;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.ui.fragments.EarningFragment;
import com.hapramp.ui.fragments.HomeFragment;
import com.hapramp.ui.fragments.ProfileFragment;
import com.hapramp.ui.fragments.SettingsFragment;
import com.hapramp.utils.BackstackManager;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FollowingsSyncUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.viewmodel.common.ConnectivityViewModel;
import com.hapramp.views.extraa.CreateNewButtonView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements CreateNewButtonView.ItemClickListener {
  private final int BOTTOM_MENU_HOME = 7;
  private final int BOTTOM_MENU_COMP = 8;
  private final int BOTTOM_MENU_PROFILE = 9;
  private final int BOTTOM_MENU_SETTINGS = 10;
  private final int BOTTOM_MENU_EARNINGS = 11;
  private final int FRAGMENT_HOME = 12;
  private final int FRAGMENT_PROFILE = 14;
  private final int FRAGMENT_SETTINGS = 15;
  private final int FRAGMENT_EARNINGS = 16;
  @BindView(R.id.contentPlaceHolder)
  FrameLayout contentPlaceHolder;
  @BindView(R.id.connectivity_text)
  TextView connectivityText;
  @BindView(R.id.connectivity_message_container)
  FrameLayout connectivityMessageContainer;
  @BindView(R.id.search_icon)
  ImageView searchIcon;
  @BindView(R.id.haprampIcon)
  ImageView haprampIcon;
  @BindView(R.id.notification_icon)
  ImageView notificationIcon;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.toolbar_drop_shadow)
  FrameLayout toolbarDropShadow;
  @BindView(R.id.shadow)
  ImageView shadow;
  @BindView(R.id.bottomBar_home)
  ImageView bottomBarHome;
  @BindView(R.id.bottomBar_wallet)
  ImageView bottomBarWallet;
  @BindView(R.id.bottomBar_profile)
  ImageView bottomBarProfile;
  @BindView(R.id.bottomBar_settings)
  ImageView bottomBarSettings;
  @BindView(R.id.bottombar_container)
  LinearLayout bottombarContainer;
  @BindView(R.id.createNewBtn)
  CreateNewButtonView createNewBtn;
  private int lastMenuSelection = BOTTOM_MENU_HOME;
  private Typeface materialTypface;
  private FragmentManager fragmentManager;
  private HomeFragment homeFragment;
  private ProfileFragment profileFragment;
  private SettingsFragment settingsFragment;
  private EarningFragment earningFragment;
  private ProgressDialog progressDialog;
  private ConnectivityViewModel connectivityViewModel;
  private boolean backPressedOnce = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    initObjects();
    syncBasicInfo();
    BackstackManager.pushItem(FRAGMENT_HOME);
    transactFragment(FRAGMENT_HOME);
    saveDeviceWidth();
    setupToolbar();
    attachListeners();
    observeConnection();
  }

  private void initObjects() {
    fragmentManager = getSupportFragmentManager();
    homeFragment = new HomeFragment();
    profileFragment = new ProfileFragment();
    settingsFragment = new SettingsFragment();
    earningFragment = new EarningFragment();
    progressDialog = new ProgressDialog(this);
    progressDialog.setCancelable(false);
  }

  private void syncBasicInfo() {
    if (HaprampPreferenceManager.getInstance().getCurrentUserInfoAsJson().length() == 0) {
      showInterruptedProgressBar("Fetching profile info...");
    }
    EventReporter.reportDeviceId();
    checkTokenValidity();
    DataStore.performAllCommunitySync();
    DataStore.requestSyncLastPostCreationTime();
    syncUserFollowings();
  }

  private void observeConnection() {
    connectivityViewModel = ViewModelProviders.of(this).get(ConnectivityViewModel.class);
    connectivityViewModel.getConnectivityState().observeForever(new Observer<Boolean>() {
      @Override
      public void onChanged(@Nullable Boolean isConnected) {
        if (isConnected) {
          hideConnectivityBar();
        } else {
          revealConnectivityBar();
        }
      }
    });
  }

  private void saveDeviceWidth() {
    Resources resources = getResources();
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    int deviceWidth = displayMetrics.widthPixels;
    HaprampPreferenceManager.getInstance().setDeviceWidth(deviceWidth);
  }

  private void syncUserFollowings() {
    FollowingsSyncUtils.syncFollowings(this);
  }

  private void setupToolbar() {
    materialTypface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
  }

  private void logout() {
    Toast.makeText(this, "Token Expired! Please login again.", Toast.LENGTH_LONG).show();
    HaprampPreferenceManager.getInstance().clearPreferences();
    Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  private void attachListeners() {
    haprampIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // check for the current selection
        if (lastMenuSelection == BOTTOM_MENU_HOME)
          return;
        BackstackManager.pushItem(FRAGMENT_HOME);
        transactFragment(FRAGMENT_HOME);
      }
    });

    bottomBarHome.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // check for the current selection
        if (lastMenuSelection == BOTTOM_MENU_HOME)
          return;
        BackstackManager.pushItem(FRAGMENT_HOME);
        transactFragment(FRAGMENT_HOME);
      }
    });


    bottomBarWallet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (lastMenuSelection == BOTTOM_MENU_EARNINGS)
          return;
        BackstackManager.pushItem(FRAGMENT_EARNINGS);
        transactFragment(FRAGMENT_EARNINGS);
      }
    });


    bottomBarProfile.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (lastMenuSelection == BOTTOM_MENU_PROFILE)
          return;
        BackstackManager.pushItem(FRAGMENT_PROFILE);
        transactFragment(FRAGMENT_PROFILE);
      }
    });

    bottomBarSettings.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (lastMenuSelection == BOTTOM_MENU_SETTINGS)
          return;
        BackstackManager.pushItem(FRAGMENT_SETTINGS);
        transactFragment(FRAGMENT_SETTINGS);
      }
    });

    createNewBtn.setItemClickListener(this);

    searchIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(HomeActivity.this, UserSearchActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
      }
    });
  }

  private void showInterruptedProgressBar(String msg) {
    if (progressDialog != null) {
      progressDialog.setMessage(msg);
      progressDialog.show();
    }
  }

  @Override
  public void onBackPressed() {
    int topItem = BackstackManager.getTop();
    if (topItem == FRAGMENT_HOME) {
      showExistAlert();
    } else {
      BackstackManager.popItem();
      transactFragment(BackstackManager.getTop());
    }
  }

  private void showExistAlert() {
    if (backPressedOnce) {
      finish();
      return;
    }
    backPressedOnce = true;
    EventReporter.reportEvent(this);
    Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        backPressedOnce = false;
      }
    }, 2000);
  }

  private void transactFragment(int fragment) {
    switch (fragment) {
      case FRAGMENT_HOME:
        swapSelection(BOTTOM_MENU_HOME);
        fragmentManager.beginTransaction()
          .addToBackStack("home")
          .replace(R.id.contentPlaceHolder, homeFragment)
          .commit();
        break;

      case FRAGMENT_PROFILE:
        swapSelection(BOTTOM_MENU_PROFILE);
        fragmentManager.beginTransaction()
          .addToBackStack("profile")
          .replace(R.id.contentPlaceHolder, profileFragment)
          .commit();
        break;
      case FRAGMENT_SETTINGS:
        swapSelection(BOTTOM_MENU_SETTINGS);
        fragmentManager.beginTransaction()
          .addToBackStack("setting")
          .replace(R.id.contentPlaceHolder, settingsFragment)
          .commit();
        break;

      case FRAGMENT_EARNINGS:
        swapSelection(BOTTOM_MENU_EARNINGS);
        fragmentManager.beginTransaction()
          .addToBackStack("earning")
          .replace(R.id.contentPlaceHolder, earningFragment)
          .commit();
        break;
      default:
        break;
    }
  }

  private void swapSelection(int newSelectedMenu) {
    if (newSelectedMenu == lastMenuSelection)
      return;
    resetLastSelection(lastMenuSelection);
    switch (newSelectedMenu) {
      case BOTTOM_MENU_HOME:
        bottomBarHome.setImageResource(R.drawable.home_icon_selected);
        lastMenuSelection = BOTTOM_MENU_HOME;
        break;
      case BOTTOM_MENU_COMP:
        bottomBarWallet.setImageResource(R.drawable.wallet_icon_selected);
        lastMenuSelection = BOTTOM_MENU_COMP;
        break;
      case BOTTOM_MENU_PROFILE:
        bottomBarProfile.setImageResource(R.drawable.user_icon_selected);
        lastMenuSelection = BOTTOM_MENU_PROFILE;
        break;
      case BOTTOM_MENU_SETTINGS:
        bottomBarSettings.setImageResource(R.drawable.settings_icon_selected);
        lastMenuSelection = BOTTOM_MENU_SETTINGS;
        break;
      case BOTTOM_MENU_EARNINGS:
        bottomBarWallet.setImageResource(R.drawable.wallet_icon_selected);
        lastMenuSelection = BOTTOM_MENU_EARNINGS;
        break;
      default:
        break;
    }
  }

  private void resetLastSelection(int lastMenuSelection) {
    switch (lastMenuSelection) {
      case BOTTOM_MENU_HOME:
        bottomBarHome.setImageResource(R.drawable.home_icon);
        break;
      case BOTTOM_MENU_COMP:
        bottomBarWallet.setImageResource(R.drawable.wallet_icon);
        break;
      case BOTTOM_MENU_PROFILE:
        bottomBarProfile.setImageResource(R.drawable.user_icon);
        break;
      case BOTTOM_MENU_SETTINGS:
        bottomBarSettings.setImageResource(R.drawable.settings_icon);
        break;
      case BOTTOM_MENU_EARNINGS:
        bottomBarWallet.setImageResource(R.drawable.wallet_icon);
        break;
      default:
        break;
    }
  }

  @Override
  public void onCreateArticleButtonClicked() {
    Intent intent = new Intent(this, CreateArticleActivity.class);
    startActivity(intent);
    overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);
  }

  @Override
  public void onCreatePostButtonClicked() {
    Intent intent = new Intent(this, CreatePostActivity.class);
    startActivity(intent);
    overridePendingTransition(R.anim.slide_up_enter, R.anim.slide_up_exit);
  }

  private void hideInterruptedProgressBar() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  private void hideConnectivityBar() {
    try {
      connectivityMessageContainer.setVisibility(View.GONE);
    }
    catch (Exception e) {
      Log.d("Exception", e.toString());
    }
  }

  private void revealConnectivityBar() {
    try {
      connectivityMessageContainer.setVisibility(View.VISIBLE);
    }
    catch (Exception e) {
      Log.d("Exception", e.toString());
    }
  }

  private void checkTokenValidity() {
    final SteemConnect steemConnect = SteemConnectUtils
      .getSteemConnectInstance(HaprampPreferenceManager.getInstance().getSC2AccessToken());
    final Handler mHandler = new Handler();
    new Thread() {
      @Override
      public void run() {
        steemConnect.me(new SteemConnectCallback() {
          @Override
          public void onResponse(String response) {
            JSONParser jsonParser = new JSONParser();
            final User user = jsonParser.parseSC2UserJson(response);
            HaprampPreferenceManager.getInstance().saveCurrentUserInfoAsJson(new Gson().toJson(user));
            hideInterruptedProgressBar();
          }

          @Override
          public void onError(final SteemConnectException e) {
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                hideInterruptedProgressBar();
                if (ConnectionUtils.isConnected(HomeActivity.this)) {
                  logout();
                }
              }
            });
          }
        });
      }
    }.start();
  }
}

