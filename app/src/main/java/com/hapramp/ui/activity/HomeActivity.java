package com.hapramp.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserProfileCallback;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.CommunityListWrapper;
import com.hapramp.steem.models.User;
import com.hapramp.ui.fragments.EarningFragment;
import com.hapramp.ui.fragments.HomeFragment;
import com.hapramp.ui.fragments.ProfileFragment;
import com.hapramp.ui.fragments.SettingsFragment;
import com.hapramp.utils.CrashReporterKeys;
import com.hapramp.utils.FollowingsSyncUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.views.extraa.CreateButtonView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements CreateButtonView.ItemClickListener {
  private final int BOTTOM_MENU_HOME = 7;
  private final int BOTTOM_MENU_COMP = 8;
  private final int BOTTOM_MENU_PROFILE = 9;
  private final int BOTTOM_MENU_SETTINGS = 10;
  private final int BOTTOM_MENU_EARNINGS = 11;
  private final int FRAGMENT_HOME = 12;
  private final int FRAGMENT_PROFILE = 14;
  private final int FRAGMENT_SETTINGS = 15;
  private final int FRAGMENT_EARNINGS = 16;
  @BindView(R.id.search_icon)
  TextView searchIcon;
  @BindView(R.id.bottomBar_home)
  TextView bottomBarHome;
  @BindView(R.id.bottomBar_competition)
  TextView bottomBarCompetition;
  @BindView(R.id.createNewBtn)
  CreateButtonView createButtonView;
  @BindView(R.id.bottomBar_profile)
  TextView bottomBarProfile;
  @BindView(R.id.bottomBar_settings)
  TextView bottomBarSettings;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.contentPlaceHolder)
  FrameLayout contentPlaceHolder;
  @BindView(R.id.bottomBar_home_text)
  TextView bottomBarHomeText;
  @BindView(R.id.bottomBar_competition_text)
  TextView bottomBarCompetitionText;
  @BindView(R.id.bottomBar_profile_text)
  TextView bottomBarProfileText;
  @BindView(R.id.bottomBar_settings_text)
  TextView bottomBarSettingsText;
  @BindView(R.id.toolbar_drop_shadow)
  FrameLayout toolbarDropShadow;
  @BindView(R.id.shadow)
  ImageView shadow;
  @BindView(R.id.bottombar_container)
  LinearLayout bottombarContainer;
  @BindView(R.id.haprampIcon)
  ImageView haprampIcon;
  private int lastMenuSelection = BOTTOM_MENU_HOME;
  private Fragment currentVisibleFragment;
  private Typeface materialTypface;
  private FragmentManager fragmentManager;
  private HomeFragment homeFragment;
  private ProfileFragment profileFragment;
  private SettingsFragment settingsFragment;
  private EarningFragment earningFragment;
  private ProgressDialog progressDialog;
  private PostUploadReceiver postUploadReceiver;
  private DataStore dataStore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    initObjects();
    DataStore.performAllCommunitySync();
    transactFragment(FRAGMENT_HOME);
    saveDeviceWidth();
    syncUserFollowings();
    setupToolbar();
    attachListeners();
    postUploadReceiver = new PostUploadReceiver();
    fetchCompleteUserInfo();
    DataStore.requestSyncLastPostCreationTime();
  }

  private void saveDeviceWidth() {
    Resources resources = getResources();
    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
    int deviceWidth = displayMetrics.widthPixels;
    HaprampPreferenceManager.getInstance().setDEviceWidth(deviceWidth);
  }
  private void syncUserFollowings() {
    FollowingsSyncUtils.syncFollowings(this);
  }

  private void setupToolbar() {
    materialTypface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
    searchIcon.setTypeface(materialTypface);
    bottomBarHome.setTypeface(materialTypface);
    bottomBarCompetition.setTypeface(materialTypface);
    bottomBarProfile.setTypeface(materialTypface);
    bottomBarSettings.setTypeface(materialTypface);
  }

  private void initObjects() {
    Crashlytics.setString(CrashReporterKeys.UI_ACTION, "home init");
    Crashlytics.setUserIdentifier(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    dataStore = new DataStore();
    fragmentManager = getSupportFragmentManager();
    homeFragment = new HomeFragment();
    profileFragment = new ProfileFragment();
    settingsFragment = new SettingsFragment();
    earningFragment = new EarningFragment();
    progressDialog = new ProgressDialog(this);
  }

  private void attachListeners() {
    haprampIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // check for the current selection
        if (lastMenuSelection == BOTTOM_MENU_HOME)
          return;
        swapSelection(BOTTOM_MENU_HOME);
        transactFragment(FRAGMENT_HOME);
      }
    });

    bottomBarHome.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // check for the current selection
        if (lastMenuSelection == BOTTOM_MENU_HOME)
          return;
        swapSelection(BOTTOM_MENU_HOME);
        transactFragment(FRAGMENT_HOME);
      }
    });


    bottomBarCompetition.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (lastMenuSelection == BOTTOM_MENU_EARNINGS)
          return;
        swapSelection(BOTTOM_MENU_EARNINGS);
        transactFragment(FRAGMENT_EARNINGS);
      }
    });


    bottomBarProfile.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (lastMenuSelection == BOTTOM_MENU_PROFILE)
          return;
        swapSelection(BOTTOM_MENU_PROFILE);
        transactFragment(FRAGMENT_PROFILE);
      }
    });


    bottomBarSettings.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        if (lastMenuSelection == BOTTOM_MENU_SETTINGS)
          return;
        swapSelection(BOTTOM_MENU_SETTINGS);
        transactFragment(FRAGMENT_SETTINGS);
      }
    });
    createButtonView.setItemClickListener(this);
    searchIcon.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(HomeActivity.this, UserSearchActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit);
      }
    });
  }

  private void fetchCompleteUserInfo() {
    dataStore.requestUserProfile(HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
      new UserProfileCallback() {
      @Override
      public void onUserProfileFetching() {

      }

      @Override
      public void onUserProfileAvailable(User user, boolean isFreshData) {
        HaprampPreferenceManager.getInstance().saveCurrentUserInfoAsJson(new Gson().toJson(user));
      }

      @Override
      public void onUserProfileFetchError(String err) {

      }
    });
  }

  private void transactFragment(int fragment) {
    switch (fragment) {
      case FRAGMENT_HOME:
        currentVisibleFragment = homeFragment;
        fragmentManager.beginTransaction()
          .addToBackStack("home")
          .replace(R.id.contentPlaceHolder, homeFragment)
          .commit();
        break;

      case FRAGMENT_PROFILE:
        currentVisibleFragment = profileFragment;
        fragmentManager.beginTransaction()
          .addToBackStack("profile")
          .replace(R.id.contentPlaceHolder, profileFragment)
          .commit();
        break;
      case FRAGMENT_SETTINGS:

        currentVisibleFragment = settingsFragment;
        fragmentManager.beginTransaction()
          .addToBackStack("setting")
          .replace(R.id.contentPlaceHolder, settingsFragment)
          .commit();
        break;

      case FRAGMENT_EARNINGS:

        currentVisibleFragment = earningFragment;
        fragmentManager.beginTransaction()
          .addToBackStack("earning")
          .replace(R.id.contentPlaceHolder, earningFragment)
          .commit();
        break;
      default:
        break;
    }
  }

  private void cacheCommunitiesList() {
    CommunityListWrapper communityListWrapper = new Gson().fromJson(HaprampPreferenceManager.getInstance().getAllCommunityAsJson(), CommunityListWrapper.class);
    List<CommunityModel> communities = communityListWrapper.getCommunityModels();
    for (int i = 0; i < communities.size(); i++) {
      HaprampPreferenceManager.getInstance().setCommunityTagToColorPair(communities.get(i).getmTag(), communities.get(i).getmColor());
      HaprampPreferenceManager.getInstance().setCommunityTagToNamePair(communities.get(i).getmTag(), communities.get(i).getmName());
    }
  }

  private void swapSelection(int newSelectedMenu) {
    if (newSelectedMenu == lastMenuSelection)
      return;
    resetLastSelection(lastMenuSelection);
    switch (newSelectedMenu) {
      case BOTTOM_MENU_HOME:
        bottomBarHome.setTextColor(getResources().getColor(R.color.colorPrimary));
        bottomBarHomeText.setTextColor(getResources().getColor(R.color.colorPrimary));
        lastMenuSelection = BOTTOM_MENU_HOME;

        break;
      case BOTTOM_MENU_COMP:
        bottomBarCompetition.setTextColor(getResources().getColor(R.color.colorPrimary));
        bottomBarCompetitionText.setTextColor(getResources().getColor(R.color.colorPrimary));
        lastMenuSelection = BOTTOM_MENU_COMP;

        break;
      case BOTTOM_MENU_PROFILE:
        bottomBarProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
        bottomBarProfileText.setTextColor(getResources().getColor(R.color.colorPrimary));
        lastMenuSelection = BOTTOM_MENU_PROFILE;

        break;

      case BOTTOM_MENU_SETTINGS:
        bottomBarSettings.setTextColor(getResources().getColor(R.color.colorPrimary));
        bottomBarSettingsText.setTextColor(getResources().getColor(R.color.colorPrimary));
        lastMenuSelection = BOTTOM_MENU_SETTINGS;

        break;

      case BOTTOM_MENU_EARNINGS:

        bottomBarCompetition.setTextColor(getResources().getColor(R.color.colorPrimary));
        bottomBarCompetitionText.setTextColor(getResources().getColor(R.color.colorPrimary));
        lastMenuSelection = BOTTOM_MENU_EARNINGS;

        break;

      default:
        break;
    }
  }

  private void resetLastSelection(int lastMenuSelection) {

    switch (lastMenuSelection) {
      case BOTTOM_MENU_HOME:
        bottomBarHome.setTextColor(Color.parseColor("#818080"));
        bottomBarHomeText.setTextColor(Color.parseColor("#818080"));
        break;
      case BOTTOM_MENU_COMP:
        bottomBarCompetition.setTextColor(Color.parseColor("#818080"));
        bottomBarCompetitionText.setTextColor(Color.parseColor("#818080"));
        break;
      case BOTTOM_MENU_PROFILE:
        bottomBarProfile.setTextColor(Color.parseColor("#818080"));
        bottomBarProfileText.setTextColor(Color.parseColor("#818080"));
        break;
      case BOTTOM_MENU_SETTINGS:
        bottomBarSettings.setTextColor(Color.parseColor("#818080"));
        bottomBarSettingsText.setTextColor(Color.parseColor("#818080"));
        break;
      case BOTTOM_MENU_EARNINGS:
        bottomBarCompetition.setTextColor(Color.parseColor("#818080"));
        bottomBarCompetitionText.setTextColor(Color.parseColor("#818080"));
        break;
      default:
        break;
    }
  }

  @Override
  public void onBackPressed() {
    showExistAlert();
  }

  private void showExistAlert() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this)
      .setTitle(R.string.app_exit_alert_message)
      .setPositiveButton("YES", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          finish();
        }
      })
      .setNegativeButton("No", null);
    builder.show();
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

  private class PostUploadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (profileFragment.isAdded())
        profileFragment.reloadPosts();
    }
  }
}

