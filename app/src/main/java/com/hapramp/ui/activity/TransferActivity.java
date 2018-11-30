package com.hapramp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
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
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.api.URLS;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.SteemRequestBody;
import com.hapramp.models.LookupAccount;
import com.hapramp.utils.WalletOperations;
import com.hapramp.views.UserMentionSuggestionListView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

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

public class TransferActivity extends AppCompatActivity {

  public static final String EXTRA_SBD_BALANCE = "extra_sbd_balance";
  public static final String EXTRA_STEEM_BALANCE = "extra_steem_balance";
  public static final int Cmode_sbd = 0;
  public static final int Cmode_steem = 1;
  @BindView(R.id.backBtn)
  ImageView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
  @BindView(R.id.username_label)
  TextView usernameLabel;
  @BindView(R.id.username_et)
  EditText usernameEt;
  @BindView(R.id.amount_label)
  TextView amountLabel;
  @BindView(R.id.amount_et)
  EditText amountEt;
  @BindView(R.id.balanceTv)
  TextView balanceTv;
  @BindView(R.id.memo_label)
  TextView memoLabel;
  @BindView(R.id.memo_et)
  EditText memoEt;
  @BindView(R.id.continueBtn)
  TextView continueBtn;
  @BindView(R.id.cancelBtn)
  TextView cancelBtn;
  @BindView(R.id.currency_selector_sbd)
  TextView currencySelectorSbd;
  @BindView(R.id.currency_selector_steem)
  TextView currencySelectorSteem;
  @BindView(R.id.currency_selector_container)
  RelativeLayout currencySelectorContainer;
  @BindView(R.id.user_suggestions)
  UserMentionSuggestionListView userMentionsSuggestionsView;
  DataStore dataStore;
  PublishSubject<String> publishSubject = PublishSubject.create();
  CompositeDisposable compositeDisposable = new CompositeDisposable();
  private int currentCurrencyMode = 0;
  private double mSteemBalance = 0;
  private double mSBDBalance = 0;
  private String finalTransferAmount = "";
  private DisposableObserver<LookupAccount> usernameObserver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transfer);
    ButterKnife.bind(this);
    collectExtra();
    attachListeners();
    dataStore = new DataStore();
  }

  private void collectExtra() {
    Intent intent = getIntent();
    if (intent != null) {
      mSBDBalance = Double.parseDouble(intent.getExtras().getString(EXTRA_SBD_BALANCE, "0 SBD")
        .split(" ")[0]);
      mSteemBalance = Double.parseDouble(intent.getExtras().getString(EXTRA_STEEM_BALANCE, "0 STEEM")
        .split(" ")[0]);
      setCurrencyMode(Cmode_sbd);
    }
  }

  private void attachListeners() {
    DisposableObserver<LookupAccount> usernameObserver = getUsernameObserver();
    compositeDisposable.add(publishSubject
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
      })
      .subscribeWith(usernameObserver));

    compositeDisposable.add(RxTextView
      .textChangeEvents(usernameEt)
      .skipInitialValue()
      .debounce(200, TimeUnit.MILLISECONDS)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeWith(userNameTextWatcher()));

    compositeDisposable.add(usernameObserver);

    userMentionsSuggestionsView.setMentionsSuggestionPickListener(new UserMentionSuggestionListView.MentionsSuggestionPickListener() {
      @Override
      public void onUserPicked(String username) {
        if (userMentionsSuggestionsView != null) {
          userMentionsSuggestionsView.setVisibility(View.GONE);
          usernameEt.setText(username);
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
    currencySelectorSbd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setCurrencyMode(Cmode_sbd);
      }
    });

    currencySelectorSteem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setCurrencyMode(Cmode_steem);
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
    cancelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }

  private void setCurrencyMode(int mode) {
    currentCurrencyMode = mode;
    if (mode == Cmode_sbd) {
      currencySelectorSbd.setBackgroundColor(Color.parseColor("#3F72AF"));
      currencySelectorSteem.setBackgroundColor(Color.parseColor("#1f000000"));
      currencySelectorSbd.setTextColor(Color.parseColor("#ffffff"));
      currencySelectorSteem.setTextColor(Color.parseColor("#8a000000"));
      balanceTv.setText(String.format("Your Balance: %s SBD", mSBDBalance));
    } else {
      currencySelectorSteem.setBackgroundColor(Color.parseColor("#3F72AF"));
      currencySelectorSbd.setBackgroundColor(Color.parseColor("#1f000000"));
      currencySelectorSteem.setTextColor(Color.parseColor("#ffffff"));
      currencySelectorSbd.setTextColor(Color.parseColor("#8a000000"));
      balanceTv.setText(String.format("Your Balance: %s STEEM", mSteemBalance));
    }
  }

  public DisposableObserver<LookupAccount> getUsernameObserver() {
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

  private DisposableObserver<TextViewTextChangeEvent> userNameTextWatcher() {
    return new DisposableObserver<TextViewTextChangeEvent>() {
      @Override
      public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
        String searchTerm = textViewTextChangeEvent.text().toString().trim().toLowerCase();
        if (searchTerm.length() > 0 && usernameEt.getSelectionEnd() > 0) {
          onSearchingUser();
          publishSubject.onNext(searchTerm);
        } else {
          userMentionsSuggestionsView.setVisibility(View.GONE);
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
    if (usernameEt.getText().toString().trim().length() == 0) {
      toast("Username missing");
      return false;
    }
    String inputAmount = amountEt.getText().toString();
    if (inputAmount.length() == 0) {
      toast("Enter amount!");
      return false;
    }
    double amount = Double.parseDouble(inputAmount);
    if (amount == 0) {
      toast("Amount should be greater than 0");
      return false;
    }
    if (currentCurrencyMode == Cmode_sbd) {
      if (amount > mSBDBalance) {
        toast("Not enough SBD balance");
        return false;
      }
      finalTransferAmount = amount + " SBD";
      return true;
    } else {
      if (amount > mSteemBalance) {
        toast("Not enough STEEM balance");
        return false;
      }
      finalTransferAmount = amount + " STEEM";
      return true;
    }
  }

  private void openTransactionPage() {
    String url = WalletOperations.getTransferUrl(usernameEt.getText().toString(),
      memoEt.getText().toString(),
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

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

  private void onSearchingUser(){
    if (userMentionsSuggestionsView != null) {
      userMentionsSuggestionsView.setVisibility(View.VISIBLE);
      userMentionsSuggestionsView.onSearching();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
  }
}
