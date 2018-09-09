package com.hapramp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.WalletOperations;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.ui.activity.TransferActivity.EXTRA_STEEM_BALANCE;

public class PowerUpActivity extends AppCompatActivity {

  @BindView(R.id.backBtn)
  TextView backBtn;
  @BindView(R.id.action_bar_title)
  TextView actionBarTitle;
  @BindView(R.id.action_bar_container)
  RelativeLayout actionBarContainer;
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
  @BindView(R.id.appBar)
  RelativeLayout appBar;
  private double mSteemBalance;
  private String finalTransferAmount;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_power_up);
    ButterKnife.bind(this);
    collectExtra();
    attachListeners();
    backBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
  }

  private void collectExtra() {
    Intent intent = getIntent();
    if (intent != null) {
      String _balance = intent.getExtras().getString(EXTRA_STEEM_BALANCE, "0 STEEM");
      mSteemBalance = Double.parseDouble(_balance.split(" ")[0]);
      balanceTv.setText(String.format("Your balance: %s", _balance));
    }
  }

  private void attachListeners() {
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

  private boolean validateAmount() {
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
    if (amount > mSteemBalance) {
      toast("Not enough STEEM balance");
      return false;
    }
    finalTransferAmount = amount + " STEEM";
    return true;
  }

  private void openTransactionPage() {
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    String url = WalletOperations.getPowerUpUrl(username, finalTransferAmount);
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(browserIntent);
    finish();
  }

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }
}
