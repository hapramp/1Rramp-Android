package com.hapramp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.datastore.DataStore;
import com.hapramp.datastore.callbacks.UserWalletCallback;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.models.User;
import com.hapramp.steemconnect.SteemConnectUtils;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.ui.activity.AccountHistoryActivity;
import com.hapramp.ui.activity.PowerDownActivity;
import com.hapramp.ui.activity.PowerUpActivity;
import com.hapramp.ui.activity.TransferActivity;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.ReputationCalc;
import com.hapramp.views.extraa.BubbleProgressBar;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xute.cryptocoinview.CoinView;
import xute.cryptocoinview.Coins;

public class EarningFragment extends Fragment implements
  UserWalletCallback {
  public static final String ARG_USERNAME = "username";
  @BindView(R.id.user_image)
  ImageView userImage;
  @BindView(R.id.username)
  TextView username;
  @BindView(R.id.user_fullname)
  TextView userFullname;
  @BindView(R.id.user_reputation)
  TextView userReputation;
  @BindView(R.id.user_fullname_container)
  LinearLayout userFullnameContainer;
  @BindView(R.id.history_btn)
  RelativeLayout seeHistoryBtn;
  @BindView(R.id.steem_icon)
  ImageView steemIcon;
  @BindView(R.id.divider1)
  ImageView divider1;
  @BindView(R.id.steem_power_icon)
  ImageView steemPowerIcon;
  @BindView(R.id.divider2)
  ImageView divider2;
  @BindView(R.id.steem_dollar_icon)
  ImageView steemDollarIcon;
  @BindView(R.id.divider3)
  ImageView divider3;
  @BindView(R.id.steem_saving_icon)
  ImageView steemSavingIcon;
  @BindView(R.id.divider4)
  ImageView divider4;
  @BindView(R.id.estimated_value)
  ImageView estimatedValue;
  @BindView(R.id.wallet_steem_tv)
  TextView walletSteemTv;
  @BindView(R.id.wallet_steem_power_tv)
  TextView walletSteemPowerTv;
  @BindView(R.id.wallet_steem_dollar_tv)
  TextView walletSteemDollarTv;
  @BindView(R.id.wallet_saving_tv)
  TextView walletSavingTv;
  @BindView(R.id.wallet_est_account_value_tv)
  TextView walletEstAccountValueTv;
  @BindView(R.id.steem_progress)
  BubbleProgressBar steemProgress;
  @BindView(R.id.steem_power_progress)
  BubbleProgressBar steemPowerProgress;
  @BindView(R.id.steem_dollar_progress)
  BubbleProgressBar steemDollarProgress;
  @BindView(R.id.saving_progress)
  BubbleProgressBar savingProgress;
  @BindView(R.id.estimated_value_progress)
  BubbleProgressBar estimatedValueProgress;
  @BindView(R.id.account_info_card)
  CardView inforCard;
  @BindView(R.id.sbd_rate)
  CoinView sbdRateView;
  @BindView(R.id.steem_rate)
  CoinView steemRate;
  @BindView(R.id.lablel)
  TextView lablel;
  @BindView(R.id.claim_reward_btn)
  RelativeLayout claimRewardBtn;
  @BindView(R.id.transfer_btn)
  RelativeLayout transferBtn;
  @BindView(R.id.power_up_btn)
  RelativeLayout powerUpBtn;
  @BindView(R.id.power_down_btn)
  RelativeLayout powerDownBtn;
  @BindView(R.id.account_operation_button_container)
  RelativeLayout accountOperationButtonContainer;
  @BindView(R.id.rewardPanelTv)
  TextView rewardPanelTv;
  private Handler mHandler;
  public static final int VALUES_REQUIRED_BEFORE_CALC = 5;
  private Unbinder unbinder;
  private String mUsername;
  private Context mContext;
  private double steem;
  private double sbd;
  private double sp;
  private double sbd_rate;
  private double steem_rate;
  private DataStore dataStore;
  private int valuesAvailable = 0;
  private String finalSBDReward;
  private String finalSteemReward;
  private String finalVestsReward;
  private ProgressDialog progressDialog;


  public EarningFragment() {
    mHandler = new Handler();
    dataStore = new DataStore();
  }

  @Override
  public void onAttach(Context context) {
    this.mContext = context;
    super.onAttach(context);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_earning, container, false);
    unbinder = ButterKnife.bind(this, view);
    initProgressDialog();
    if (getArguments() != null) {
      mUsername = getArguments().getString(ARG_USERNAME);
    } else {
      mUsername = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    }
    if (!mUsername.equals(HaprampPreferenceManager.getInstance().getCurrentSteemUsername())) {
      accountOperationButtonContainer.setVisibility(View.GONE);
    }
    steemRate.setCoinId(Coins.STEEM);
    steemRate.setRateCallback(new CoinView.RateCallback() {
      @Override
      public void onRate(double rate) {
        steem_rate = rate;
        valuesAvailable++;
        showEstimatedEarnings();
      }

      @Override
      public void onErrorFetchingRate() {

      }
    });
    sbdRateView.setCoinId(Coins.SBD);
    sbdRateView.setRateCallback(new CoinView.RateCallback() {
      @Override
      public void onRate(double rate) {
        sbd_rate = rate;
        valuesAvailable++;
        showEstimatedEarnings();
      }

      @Override
      public void onErrorFetchingRate() {

      }
    });
    fetchWalletInfo();
    attachListener();
    return view;
  }

  private void initProgressDialog() {
    progressDialog = new ProgressDialog(mContext);
    progressDialog.setCancelable(false);
  }
  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void showEstimatedEarnings() {
    try {
      estimatedValueProgress.setVisibility(View.GONE);
      if (VALUES_REQUIRED_BEFORE_CALC <= valuesAvailable) {
        double total = (steem_rate * (steem + sp)) + sbd * sbd_rate;
        walletEstAccountValueTv.setText(String.format(Locale.US, "$ %.2f", total));
      }
    }
    catch (Exception e) {
    }
  }

  private void fetchWalletInfo() {
    disableWalletActions();
    walletSavingTv.setText("");
    dataStore.requestWalletInfo(mUsername, this);
  }

  private void attachListener() {
    seeHistoryBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(mContext, AccountHistoryActivity.class);
        intent.putExtra(AccountHistoryActivity.EXTRA_USERNAME, mUsername);
        mContext.startActivity(intent);
      }
    });

    powerUpBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(TransferActivity.EXTRA_STEEM_BALANCE, walletSteemTv.getText().toString());
        Intent intent = new Intent(mContext, PowerUpActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

    powerDownBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(PowerDownActivity.EXTRA_SP_BALANCE, walletSteemPowerTv.getText().toString());
        Intent intent = new Intent(mContext, PowerDownActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

    transferBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(TransferActivity.EXTRA_STEEM_BALANCE, walletSteemTv.getText().toString());
        bundle.putString(TransferActivity.EXTRA_SBD_BALANCE, walletSteemDollarTv.getText().toString());
        Intent intent = new Intent(mContext, TransferActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    });

    claimRewardBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        requestClaimReward();
      }
    });
  }

  private void disableWalletActions() {
    try {
      transferBtn.setEnabled(false);
      powerDownBtn.setEnabled(false);
      powerUpBtn.setEnabled(false);
      claimRewardBtn.setEnabled(false);
    }
    catch (Exception e) {

    }
  }

  private void requestClaimReward() {
    if (progressDialog != null) {
      progressDialog.setMessage("Claiming Reward...");
      progressDialog.show();
    }
    final SteemConnect steemConnect = SteemConnectUtils
      .getSteemConnectInstance(HaprampPreferenceManager.getInstance().getSC2AccessToken());
    new Thread() {
      @Override
      public void run() {
        steemConnect.claimRewardBalance(
          HaprampPreferenceManager.getInstance().getCurrentSteemUsername(),
          finalSteemReward,
          finalSBDReward,
          finalVestsReward,
          new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
              mHandler.post(
                new Runnable() {
                  @Override
                  public void run() {
                    rewardClaimed();
                  }
                }
              );
            }

            @Override
            public void onError(SteemConnectException e) {
              mHandler.post(new Runnable() {
                @Override
                public void run() {
                  rewardClaimedFailed();
                }
              });
            }
          }
        );
      }
    }.start();
  }

  @Override
  public void onFetchingWalletInfo() {
  }

  @Override
  public void onUser(User user) {
    try {
      bindData(user);
    }
    catch (Exception e) {
    }
  }

  @Override
  public void onUserSteem(final String steem) {
    this.steem = Double.parseDouble(steem.split(" ")[0]);
    valuesAvailable++;
    try {
      steemProgress.setVisibility(View.GONE);
      walletSteemTv.setText(steem);
    }
    catch (Exception e) {
    }
  }

  private void rewardClaimed() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
    Toast.makeText(mContext, "Successfully claimed reward!", Toast.LENGTH_LONG).show();
    fetchWalletInfo();
  }

  @Override
  public void onUserSteemPower(final String steemPower) {
    this.sp = Double.parseDouble(steemPower.split(" ")[0]);
    valuesAvailable++;
    try {
      steemPowerProgress.setVisibility(View.GONE);
      walletSteemPowerTv.setText(steemPower);
      showEstimatedEarnings();
    }
    catch (Exception e) {
    }
  }

  @Override
  public void onUserSavingSteem(final String savingSteem) {
    try {
      savingProgress.setVisibility(View.GONE);
      walletSavingTv.append(savingSteem + ", ");
    }
    catch (Exception e) {
    }
  }

  private void rewardClaimedFailed() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
    Toast.makeText(mContext, "Failed to claim reward!", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onUserSteemDollar(final String dollar) {
    this.sbd = Double.parseDouble(dollar.split(" ")[0]);
    valuesAvailable++;
    try {
      steemDollarProgress.setVisibility(View.GONE);
      walletSteemDollarTv.setText(dollar);
      enableWalletActions();
    }
    catch (Exception e) {
    }
  }

  @Override
  public void onUserSavingSBD(final String savingSBD) {
    try {
      savingProgress.setVisibility(View.GONE);
      walletSavingTv.append(savingSBD + " ");
    }
    catch (Exception e) {
    }
  }

  @Override
  public void onUserRewards(String _sbdReward, String _steemReward, String _rewardVests) {
    StringBuilder rewardPanelText = new StringBuilder();
    boolean rewardExists = false;
    double sbdReward = Double.parseDouble(_sbdReward.split(" ")[0]);
    double steemReward = Double.parseDouble(_steemReward.split(" ")[0]);
    double vestsReward = Double.parseDouble(_rewardVests.split(" ")[0]);
    finalSBDReward = _sbdReward;
    finalSteemReward = _steemReward;
    finalVestsReward = _rewardVests;
    try {
      if (sbdReward > 0) {
        rewardPanelText
          .append(finalSBDReward)
          .append(" ");
        rewardExists = true;
      }
      if (steemReward > 0) {
        rewardPanelText
          .append(finalSteemReward)
          .append(" ");
        rewardExists = true;
      }
      if (vestsReward > 0) {
        rewardPanelText.append(String.format(Locale.US, "%.3f SP",
          vestsReward / HaprampPreferenceManager.getInstance().getVestsPerSteem()));
        rewardExists = true;
      }
      if (rewardExists) {
        showAndEnableRewardButton();
        rewardPanelTv.setText(rewardPanelText.toString());
      } else {
        hideRewardButton();
      }
    }
    catch (Exception e) {

    }
  }

  private void showAndEnableRewardButton() {
    if (claimRewardBtn != null) {
      claimRewardBtn.setVisibility(View.VISIBLE);
      claimRewardBtn.setEnabled(true);
    }
  }

  private void hideRewardButton() {
    if (claimRewardBtn != null) {
      claimRewardBtn.setVisibility(View.GONE);
      claimRewardBtn.setEnabled(false);
    }
  }

  private void enableWalletActions() {
    try {
      transferBtn.setEnabled(true);
      powerDownBtn.setEnabled(true);
      powerUpBtn.setEnabled(true);
      claimRewardBtn.setEnabled(true);
    }
    catch (Exception e) {

    }
  }

  @Override
  public void onUserWalletDataError(String error) {
  }

  private void bindData(User data) {
    try {
      if (data != null) {
        username.setText(mUsername);
        userFullname.setText(data.getFullname());
        long rawReputation = data.getReputation();
        userReputation.setText(String.format(Locale.US, "(%.2f)",
          ReputationCalc.calculateReputation(rawReputation)));
        String profile_pic = String.format(getResources().getString(R.string.steem_user_profile_pic_format_large), mUsername);
        ImageHandler.loadCircularImage(mContext, userImage, profile_pic);
      }
    }
    catch (Exception e) {
    }
  }
}
