package com.hapramp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.api.URLS;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.SteemRequestBody;
import com.hapramp.models.LookupAccount;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.WalletOperations;
import com.hapramp.views.UserMentionSuggestionListView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.List;
import java.util.Locale;
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

public class DelegateActivity extends AppCompatActivity {
  public static final String EXTRA_SP_BALANCE = "extra_sp_balance";
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.username_label)
  TextView usernameLabel;
  @BindView(R.id.username_et)
  EditText receiver_usernameEt;
  @BindView(R.id.user_suggestions)
  UserMentionSuggestionListView userMentionsSuggestionsView;
  @BindView(R.id.amount_label)
  TextView amountLabel;
  @BindView(R.id.amount_et)
  EditText amountEt;
  @BindView(R.id.balanceTv)
  TextView balanceTv;
  @BindView(R.id.continueBtn)
  TextView continueBtn;
  @BindView(R.id.cancelBtn)
  TextView cancelBtn;
  @BindView(R.id.steem_power_warning)
  TextView steemPowerWarning;
  @BindView(R.id.show_delegation_btn)
  TextView showDelegationBtn;
  PublishSubject<String> publishSubject = PublishSubject.create();
  CompositeDisposable compositeDisposable = new CompositeDisposable();
  private double mSPBalance;
  private String finalTransferAmount;
  private DataStore dataStore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_delegate);
    ButterKnife.bind(this);
    collectExtra();
    attachListeners();
    dataStore = new DataStore();
  }

  private void collectExtra() {
    Intent intent = getIntent();
    if (intent != null) {
      String _balance = intent.getExtras().getString(EXTRA_SP_BALANCE, "0 SP");
      mSPBalance = Double.parseDouble(_balance.split(" ")[0]);
      balanceTv.setText(String.format(Locale.US, "Your balance: %.2f SP", mSPBalance));
    }
  }

  private void attachListeners() {
    showDelegationBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        navigateToDelegationsListPage();
      }
    });

    DisposableObserver<LookupAccount> usernameObserver = getUsernameObserver();
    compositeDisposable.add(
      RxTextView.textChangeEvents(receiver_usernameEt)
        .skipInitialValue()
        .distinctUntilChanged()
        .debounce(300, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(usernameTextWatcher())
    );

    compositeDisposable.add(
      publishSubject
        .debounce(200, TimeUnit.MILLISECONDS)
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
        }).subscribeWith(usernameObserver)
    );

    compositeDisposable.add(usernameObserver);

    userMentionsSuggestionsView.setMentionsSuggestionPickListener(new UserMentionSuggestionListView.MentionsSuggestionPickListener() {
      @Override
      public void onUserPicked(String username) {
        if (userMentionsSuggestionsView != null) {
          userMentionsSuggestionsView.setVisibility(View.GONE);
          receiver_usernameEt.setText(username);
          amountEt.requestFocus();
        }
      }
    });
    backBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    cancelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    continueBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (validateAmount()) {
          openTransactionPage();
        }
      }
    });
  }

  private void navigateToDelegationsListPage() {
    Intent intent = new Intent(this, DelegationListActivity.class);
    intent.putExtra(DelegationListActivity.EXTRA_KEY_DELEGATOR, HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    startActivity(intent);
  }

  private DisposableObserver<LookupAccount> getUsernameObserver() {
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

  private DisposableObserver<TextViewTextChangeEvent> usernameTextWatcher() {
    return new DisposableObserver<TextViewTextChangeEvent>() {
      @Override
      public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
        String searchTerm = textViewTextChangeEvent.text().toString().trim().toLowerCase();
        if (ConnectionUtils.isConnected(DelegateActivity.this)) {
          if (searchTerm.length() > 0 && receiver_usernameEt.getSelectionEnd() > 0) {
            onSearchingUser();
            publishSubject.onNext(searchTerm);
          }
        } else {
          Toast.makeText(DelegateActivity.this, "No Connectivity", Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    };
  }

  private boolean validateAmount() {
    String inputAmount = amountEt.getText().toString();
    if (inputAmount.length() == 0) {
      toast("Enter amount!");
      return false;
    }
    double amount = Double.parseDouble(inputAmount);
    if (amount > mSPBalance) {
      toast("Not enough STEEM POWER");
      return false;
    }
    if (receiver_usernameEt.getText().toString().trim().length() == 0) {
      toast("Enter delegatee!");
      return false;
    }
    finalTransferAmount = getVests(amount);
    return true;
  }

  private void openTransactionPage() {
    String url = WalletOperations.getDelegateUrl(
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
      receiver_usernameEt.getText().toString().trim(),
      finalTransferAmount);
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(browserIntent);
    finish();
  }

  public void onUserSuggestionsAvailable(List<String> users) {
    if (userMentionsSuggestionsView != null) {
      userMentionsSuggestionsView.setVisibility(View.VISIBLE);
      userMentionsSuggestionsView.addSuggestions(users);
    }
  }

  private void onSearchingUser() {
    if (userMentionsSuggestionsView != null) {
      userMentionsSuggestionsView.setVisibility(View.VISIBLE);
      userMentionsSuggestionsView.onSearching();
    }
  }

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

  private String getVests(double amount) {
    return String.format(Locale.US,
      "%.4f VESTS",
      amount * HaprampPreferenceManager.getInstance().getVestsPerSteem());
  }

  private void fetchSuggestions(String query) {

    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_SEARCH_USER);
  }
}
