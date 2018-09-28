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
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.WalletOperations;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PowerDownActivity extends AppCompatActivity {

  public static final String EXTRA_SP_BALANCE = "extra_sp_balance";
  @BindView(R.id.backBtn)
  ImageView backBtn;
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
  private String finalTransferAmount;
  private double mSPBalance;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_power_down);
    ButterKnife.bind(this);
    collectExtra();
    attachListeners();
  }

  private void collectExtra() {
    Intent intent = getIntent();
    if (intent != null) {
      String _balance = intent.getExtras().getString(EXTRA_SP_BALANCE, "0 SP");
      mSPBalance = Double.parseDouble(_balance.split(" ")[0]);
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
    if (amount < 0.001) {
      toast("Amount should be greater than or equal to 0.001");
      return false;
    }
    if (amount > mSPBalance) {
      toast("Not enough STEEM POWER");
      return false;
    }
    finalTransferAmount = getVests(amount);
    return true;
  }

  private void openTransactionPage() {
    String url = WalletOperations.getPowerDownUrl(finalTransferAmount);
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    startActivity(browserIntent);
    finish();
  }

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  }

  private String getVests(double amount) {
    return String.format(Locale.US,
      "%.4f VESTS", amount * HaprampPreferenceManager.getInstance().getVestsPerSteem());
  }
}
