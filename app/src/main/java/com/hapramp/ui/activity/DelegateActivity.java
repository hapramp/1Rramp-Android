package com.hapramp.ui.activity;

import android.content.Intent;
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
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.WalletOperations;
import com.hapramp.views.UserMentionSuggestionListView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelegateActivity extends AppCompatActivity implements UserSearchCallback {
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
    receiver_usernameEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchTerm = receiver_usernameEt.getText().toString().trim().toLowerCase();
        if (searchTerm.length() > 0 && receiver_usernameEt.getSelectionEnd() > 0) {
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

  private void fetchSuggestions(String query) {
    if (ConnectionUtils.isConnected(DelegateActivity.this)) {
      dataStore.requestUsernames(query, this);
    } else {
      Toast.makeText(this, "No Connectivity", Toast.LENGTH_LONG).show();
    }
    AnalyticsUtil.logEvent(AnalyticsParams.EVENT_SEARCH_USER);
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

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

  private String getVests(double amount) {
    return String.format(Locale.US,
      "%.4f VESTS",
      amount * HaprampPreferenceManager.getInstance().getVestsPerSteem());
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
