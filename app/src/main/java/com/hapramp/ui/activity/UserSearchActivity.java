package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.analytics.EventReporter;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserSearchCallback;
import com.hapramp.ui.adapters.UserListAdapter;
import com.hapramp.ui.adapters.ViewPagerAdapter;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FollowingsSyncUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserSearchActivity extends AppCompatActivity implements UserSearchCallback {
  private static boolean SEARCH_MODE = false;
  private final String backTextIcon = "\uF04D";
  private final String closeSearchTextIcon = "\uF156";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.searchInput)
  EditText usernameSearchInputField;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.suggestionsListView)
  ListView suggestionsListView;
  @BindView(R.id.suggestionsProgressBar)
  ProgressBar suggestionsProgressBar;
  @BindView(R.id.messagePanel)
  TextView messagePanel;
  @BindView(R.id.toolbar_drop_shadow)
  FrameLayout toolbarDropShadow;
  @BindView(R.id.appBar)
  RelativeLayout appBar;
  @BindView(R.id.tabs)
  TabLayout tabs;
  @BindView(R.id.viewpager)
  ViewPager viewpager;
  @BindView(R.id.suggestionsContainer)
  RelativeLayout suggestionsContainer;
  UserListAdapter adapter;
  DataStore dataStore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);
    init();
    attachListener();
    fetchFollowingsAndCache();
    EventReporter.addEvent(AnalyticsParams.SCREEN_USER_SEARCH);
  }

  private void init() {
    dataStore = new DataStore();
    setupViewPager(viewpager);
    tabs.setupWithViewPager(viewpager);
    tabs.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
    adapter = new UserListAdapter(this);
    suggestionsListView.setAdapter(adapter);
    hideKeyboardByDefault();
  }

  private void attachListener() {
    usernameSearchInputField.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        usernameSearchInputField.setCursorVisible(true);
      }
    });

    usernameSearchInputField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchTerm = usernameSearchInputField.getText().toString().trim().toLowerCase();
        if (searchTerm.length() > 0) {
          fetchSuggestions(searchTerm);
          setSearchMode();
        } else {
          hideKeyboardByDefault();
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (SEARCH_MODE) {
          setBrowseMode();
        } else {
          close();
        }
      }
    });
  }

  private void fetchFollowingsAndCache() {
    FollowingsSyncUtils.syncFollowings(this);
  }

  private void setupViewPager(ViewPager viewPager) {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(adapter);
  }

  private void hideKeyboardByDefault() {
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    usernameSearchInputField.clearFocus();
    usernameSearchInputField.setCursorVisible(false);
  }

  private void fetchSuggestions(String query) {
    if (suggestionsContainer != null) {
      suggestionsContainer.setVisibility(View.VISIBLE);
    }
    if (ConnectionUtils.isConnected(UserSearchActivity.this)) {
      dataStore.requestUsernames(query, this);
    } else {
      Toast.makeText(this, "No Connectivity", Toast.LENGTH_LONG).show();
    }
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_SEARCH_USER);
  }

  private void setSearchMode() {
    SEARCH_MODE = true;
    if (suggestionsContainer != null) {
      suggestionsContainer.setVisibility(View.VISIBLE);
    }
    //change back icon to cross
    if (backBtn != null) {
      backBtn.setImageResource(R.drawable.close_icon);
    }
  }

  private void setBrowseMode() {
    SEARCH_MODE = false;
    // hide suggestion
    if (suggestionsContainer != null) {
      suggestionsContainer.setVisibility(View.GONE);
    }
    //clear search view
    if (usernameSearchInputField != null) {
      usernameSearchInputField.clearComposingText();
    }
    //change cross icon to back
    if (backBtn != null) {
      backBtn.setImageResource(R.drawable.back);
    }
  }

  private void close() {
    finish();
    overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
  }

  @Override
  public void onBackPressed() {
    close();
  }

  @Override
  protected void onResume() {
    super.onResume();
    setBrowseMode();
  }

  @Override
  public void onSearchingUsernames() {
    try {
      suggestionsListView.setVisibility(View.GONE);
      messagePanel.setVisibility(View.VISIBLE);
      suggestionsProgressBar.setVisibility(View.VISIBLE);
      messagePanel.setText("Searching...");
    }
    catch (Exception e) {
    }
  }

  @Override
  public void onUserSuggestionsAvailable(List<String> usernames) {
    try {
      if (suggestionsContainer != null) {
        suggestionsContainer.setVisibility(View.VISIBLE);
      }
      suggestionsListView.setVisibility(View.VISIBLE);
      messagePanel.setVisibility(View.GONE);
      suggestionsProgressBar.setVisibility(View.GONE);
      adapter.setUsernames((ArrayList<String>) usernames);
    }
    catch (Exception e) {

    }
  }

  @Override
  public void onUserSuggestionsError(String msg) {

  }
}
