package com.hapramp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserSearchCallback;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.WalletOperations;
import com.hapramp.views.UserMentionSuggestionListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferActivity extends AppCompatActivity implements UserSearchCallback {

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

  private int currentCurrencyMode = 0;
  private double mSteemBalance = 0;
  private double mSBDBalance = 0;
  private String finalTransferAmount = "";
  DataStore dataStore;

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
    usernameEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchTerm = usernameEt.getText().toString().trim().toLowerCase();
        if (searchTerm.length() > 0 && usernameEt.getSelectionEnd() > 0) {
          fetchSuggestions(searchTerm);
        } else {
          userMentionsSuggestionsView.setVisibility(View.GONE);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
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

  private void fetchSuggestions(String query) {
    if (ConnectionUtils.isConnected(TransferActivity.this)) {
      dataStore.requestUsernames(query, this);
    } else {
      Toast.makeText(this, "No Connectivity", Toast.LENGTH_LONG).show();
    }
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_SEARCH_USER);
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

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onSearchingUsernames() {

  }

  @Override
  public void onUserSuggestionsAvailable(List<String> users) {
    if (userMentionsSuggestionsView != null) {
      userMentionsSuggestionsView.setVisibility(View.VISIBLE);
      userMentionsSuggestionsView.addSuggestions(users);
    }
  }

  @Override
  public void onUserSuggestionsError(String msg) {

  }
}
