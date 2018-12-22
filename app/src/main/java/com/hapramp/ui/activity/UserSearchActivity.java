package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.api.URLS;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.SteemRequestBody;
import com.hapramp.models.LookupAccount;
import com.hapramp.ui.adapters.UserListAdapter;
import com.hapramp.ui.adapters.ViewPagerAdapter;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FollowingsSyncUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class UserSearchActivity extends AppCompatActivity {
  private static boolean SEARCH_MODE = false;
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

  CompositeDisposable compositeDisposable = new CompositeDisposable();
  PublishSubject<String> publishSubject = PublishSubject.create();

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

    DisposableObserver<LookupAccount> accountDisposableObserver = usernamesObserver();

    compositeDisposable.add(
      RxTextView
        .textChangeEvents(usernameSearchInputField)
        .skipInitialValue()
        .debounce(300, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(searchUsernameTextWatcher()));

    compositeDisposable.add(
      publishSubject
        .debounce(300, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .switchMapSingle(new Function<String, Single<LookupAccount>>() {
          @Override
          public Single<LookupAccount> apply(String username) {
            return RetrofitServiceGenerator
              .getService()
              .getUsernames(URLS.STEEMIT_API_URL, SteemRequestBody.lookupAccounts(username))
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
          }
        })
        .subscribeWith(accountDisposableObserver));

    compositeDisposable.add(accountDisposableObserver);

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

  private DisposableObserver<LookupAccount> usernamesObserver() {
    return new DisposableObserver<LookupAccount>() {
      @Override
      public void onNext(LookupAccount lookupAccount) {
        onUserSuggestionsAvailable(lookupAccount.getmResult());
      }

      @Override
      public void onError(Throwable e) {
      }

      @Override
      public void onComplete() {

      }
    };
  }

  private DisposableObserver<TextViewTextChangeEvent> searchUsernameTextWatcher() {
    return new DisposableObserver<TextViewTextChangeEvent>() {
      @Override
      public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
        String searchTerm = textViewTextChangeEvent.text().toString().trim().toLowerCase();
        if (searchTerm.length() > 0) {
          if (ConnectionUtils.isConnected(UserSearchActivity.this)) {
            onSearchingUsernames();
            publishSubject.onNext(searchTerm);
          } else {
            Toast.makeText(UserSearchActivity.this, "No Connectivity", Toast.LENGTH_LONG).show();
          }
          setSearchMode();
        } else {
          hideKeyboardByDefault();
        }
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
      }

      @Override
      public void onComplete() {

      }
    };
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

  private void fetchSuggestions(String query) {
    if (suggestionsContainer != null) {
      suggestionsContainer.setVisibility(View.VISIBLE);
    }

    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_SEARCH_USER);
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
  protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
  }
}
