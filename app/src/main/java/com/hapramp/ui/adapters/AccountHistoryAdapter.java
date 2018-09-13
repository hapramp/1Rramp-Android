package com.hapramp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.datastore.TransferHistoryParser;
import com.hapramp.steem.models.TransferHistoryModel;
import com.hapramp.ui.activity.ProfileActivity;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.SteemPowerCalc;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int TYPE_TRANSFER = 0;
  private static final int TYPE_AUTHOR_REWARD = 1;
  private static final int TYPE_COMMENT_BENEFACTOR = 2;
  private static final int TYPE_CLAIM = 3;
  private static final int TYPE_CURATION = 4;
  private final Context mContext;


  private ArrayList<TransferHistoryModel> transferHistoryModels;

  public AccountHistoryAdapter(Context context) {
    this.mContext = context;
    this.transferHistoryModels = new ArrayList<>();
  }

  public void setTransferHistoryModels(ArrayList<TransferHistoryModel> transferHistoryModels) {
    this.transferHistoryModels = transferHistoryModels;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view;
    switch (viewType) {
      case TYPE_AUTHOR_REWARD:
        view = LayoutInflater.from(mContext).inflate(R.layout.account_history_author_reward_item_view, null, false);
        return new AuthorRewardViewHolder(view);
      case TYPE_CLAIM:
        view = LayoutInflater.from(mContext).inflate(R.layout.account_history_claim_item_view, null, false);
        return new ClaimBalanceViewHolder(view);
      case TYPE_COMMENT_BENEFACTOR:
        view = LayoutInflater.from(mContext).inflate(R.layout.account_history_comment_benefactor_item_view, null, false);
        return new CommentBenefactorViewHolder(view);
      case TYPE_CURATION:
        view = LayoutInflater.from(mContext).inflate(R.layout.account_history_curation_item_view, null, false);
        return new CurationViewHolder(view);
      case TYPE_TRANSFER:
        view = LayoutInflater.from(mContext).inflate(R.layout.account_history_transfer_item_view, parent, false);
        return new TransferViewHolder(view);
    }
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof TransferViewHolder) {
      ((TransferViewHolder) holder).bind(transferHistoryModels.get(position));
    } else if (holder instanceof AuthorRewardViewHolder) {
      ((AuthorRewardViewHolder) holder).bind(transferHistoryModels.get(position));
    } else if (holder instanceof CommentBenefactorViewHolder) {
      ((CommentBenefactorViewHolder) holder).bind(transferHistoryModels.get(position));
    } else if (holder instanceof ClaimBalanceViewHolder) {
      ((ClaimBalanceViewHolder) holder).bind(transferHistoryModels.get(position));
    } else if (holder instanceof CurationViewHolder) {
      ((CurationViewHolder) holder).bind(transferHistoryModels.get(position));
    }
  }

  @Override
  public int getItemViewType(int position) {
    switch (transferHistoryModels.get(position).getOperation()) {
      case TransferHistoryParser.KEYS.OPERATION_TRANSFER:
        return TYPE_TRANSFER;
      case TransferHistoryParser.KEYS.OPERATION_AUTHOR_REWARD:
        return TYPE_AUTHOR_REWARD;
      case TransferHistoryParser.KEYS.OPERATION_CLAIM_REWARD_BALANCE:
        return TYPE_CLAIM;
      case TransferHistoryParser.KEYS.OPERATION_COMMENT_BENEFACTOR_REWARD:
        return TYPE_COMMENT_BENEFACTOR;
      case TransferHistoryParser.KEYS.OPERATION_CURATION_REWARD:
        return TYPE_CURATION;
    }
    return super.getItemViewType(position);
  }

  @Override
  public int getItemCount() {
    return transferHistoryModels.size();
  }

  private boolean isSent(String userAccount, String from) {
    return userAccount.equals(from);
  }

  private void openIntent(String url) {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    mContext.startActivity(browserIntent);
  }

  class TransferViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.message_label)
    TextView messageLabel;
    @BindView(R.id.remote_user)
    TextView remoteUser;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.timestamp)
    TextView timestamp;
    @BindView(R.id.message)
    TextView message;

    public TransferViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(TransferHistoryModel transferHistoryModel) {
      TransferHistoryModel.Transfer transfer = transferHistoryModel.getTransfer();
      final Intent profileIntent = new Intent(mContext, ProfileActivity.class);
      if (isSent(transferHistoryModel.getUserAccount(), transfer.from)) {
        //sent
        ImageHandler.loadCircularImage(mContext, userImage,
          String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format), transfer.to));
        messageLabel.setText("Transferred to");
        remoteUser.setText(transfer.to);
        profileIntent.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME, transfer.to);
        amount.setText(String.format("- %s", transfer.amount));
        amount.setTextColor(Color.parseColor("#bf0707"));
      } else {
        //received
        ImageHandler.loadCircularImage(mContext, userImage,
          String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format), transfer.from));
        messageLabel.setText("Received from");
        remoteUser.setText(transfer.from);
        profileIntent.putExtra(Constants.EXTRAA_KEY_STEEM_USER_NAME, transfer.from);
        amount.setText(String.format("+ %s", transfer.amount));
        amount.setTextColor(Color.parseColor("#157c18"));
      }
      timestamp.setText(MomentsUtils.getFormattedTime(transferHistoryModel.getTimeStamp()));
      if (transfer.memo.length() > 0) {
        message.setVisibility(View.VISIBLE);
        message.setText(transfer.memo);
      } else {
        message.setVisibility(View.GONE);
      }
      remoteUser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          mContext.startActivity(profileIntent);
        }
      });
    }
  }

  class AuthorRewardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.timestamp)
    TextView timestamp;
    @BindView(R.id.steem_tv)
    TextView steemTv;
    @BindView(R.id.steem_info_container)
    RelativeLayout steemInfoContainer;
    @BindView(R.id.sbd_tv)
    TextView sbdTv;
    @BindView(R.id.sbd_info_container)
    RelativeLayout sbdInfoContainer;
    @BindView(R.id.steem_power_tv)
    TextView steemPowerTv;
    @BindView(R.id.sp_info_container)
    RelativeLayout spInfoContainer;
    @BindView(R.id.goto_btn)
    TextView gotoBtn;


    public AuthorRewardViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(TransferHistoryModel transferHistoryModel) {
      TransferHistoryModel.AuthorReward authorReward = transferHistoryModel.getAuthorReward();
      timestamp.setText(MomentsUtils.getFormattedTime(transferHistoryModel.getTimeStamp()));
      double sbd = Double.parseDouble(authorReward.getSbd_payout().split(" ")[0]);
      double steem = Double.parseDouble(authorReward.getSteem_payout().split(" ")[0]);
      double vests = Double.parseDouble(authorReward.getVesting_payout().split(" ")[0]);
      double sp = SteemPowerCalc.calculateSteemPower(
        vests,
        transferHistoryModel.getTotal_vesting_fund_steem(),
        transferHistoryModel.getTotal_vesting_shares());

      if (sbd == 0) {
        sbdInfoContainer.setVisibility(View.GONE);
      } else {
        sbdTv.setText(String.format(Locale.US, "%.3f SBD", sbd));
      }

      if (steem == 0) {
        steemInfoContainer.setVisibility(View.GONE);
      } else {
        steemTv.setText(String.format(Locale.US, "%.3f STEEM", steem));
      }
      steemPowerTv.setText(String.format(Locale.US, "%.3f SP", sp));

      final String postUrl = String.format("https://steemit.com/@%s/%s", authorReward.getAuthor(), authorReward.getPermlink());
      gotoBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          openIntent(postUrl);
        }
      });
    }
  }

  class CommentBenefactorViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.timestamp)
    TextView timestamp;

    public CommentBenefactorViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(TransferHistoryModel transferHistoryModel) {
      TransferHistoryModel.CommentBenefactor commentBenefactor = transferHistoryModel.getCommentBenefactor();
      timestamp.setText(MomentsUtils.getFormattedTime(transferHistoryModel.getTimeStamp()));
      amount.setText(commentBenefactor.getReward());
    }
  }

  class ClaimBalanceViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.timestamp)
    TextView timestamp;
    @BindView(R.id.steem_tv)
    TextView steemTv;
    @BindView(R.id.steem_info_container)
    RelativeLayout steemInfoContainer;
    @BindView(R.id.sbd_tv)
    TextView sbdTv;
    @BindView(R.id.sbd_info_container)
    RelativeLayout sbdInfoContainer;
    @BindView(R.id.steem_power_tv)
    TextView steemPowerTv;
    @BindView(R.id.sp_info_container)
    RelativeLayout spInfoContainer;

    public ClaimBalanceViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(TransferHistoryModel transferHistoryModel) {
      TransferHistoryModel.ClaimRewardBalance claimRewardBalance = transferHistoryModel.getClaimRewardBalance();
      timestamp.setText(MomentsUtils.getFormattedTime(transferHistoryModel.getTimeStamp()));
      double sbd = Double.parseDouble(claimRewardBalance.getReward_sbd().split(" ")[0]);
      double steem = Double.parseDouble(claimRewardBalance.getReward_steem().split(" ")[0]);
      double vests = Double.parseDouble(claimRewardBalance.getReward_vests().split(" ")[0]);
      double sp = SteemPowerCalc.calculateSteemPower(
        vests,
        transferHistoryModel.getTotal_vesting_fund_steem(),
        transferHistoryModel.getTotal_vesting_shares());

      if (sbd == 0) {
        sbdInfoContainer.setVisibility(View.GONE);
      } else {
        sbdInfoContainer.setVisibility(View.VISIBLE);
        sbdTv.setText(String.format(Locale.US, "%.3f SBD", sbd));
      }

      if (steem == 0) {
        steemInfoContainer.setVisibility(View.GONE);
      } else {
        steemInfoContainer.setVisibility(View.VISIBLE);
        steemTv.setText(String.format(Locale.US, "%.3f STEEM", steem));
      }
      steemPowerTv.setText(String.format(Locale.US, "%.3f SP", sp));
    }
  }

  class CurationViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.steem_power_tv)
    TextView steemPowerTv;
    @BindView(R.id.sp_info_container)
    RelativeLayout spInfoContainer;
    @BindView(R.id.timestamp)
    TextView timestamp;
    @BindView(R.id.goto_btn)
    TextView gotoBtn;

    public CurationViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(TransferHistoryModel transferHistoryModel) {
      TransferHistoryModel.CurationReward curationReward = transferHistoryModel.getCurationReward();
      timestamp.setText(MomentsUtils.getFormattedTime(transferHistoryModel.getTimeStamp()));
      double vests = Double.valueOf(curationReward.getReward().split(" ")[0]);
      double sp = SteemPowerCalc.calculateSteemPower(
        vests,
        transferHistoryModel.getTotal_vesting_fund_steem(),
        transferHistoryModel.getTotal_vesting_shares());
      steemPowerTv.setText(String.format(Locale.US, "%.3f SP", sp));
      final String postUrl = String.format("https://steemit.com/@%s/%s", curationReward.getComment_author(),
        curationReward.getComment_permlink());
      gotoBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          openIntent(postUrl);
        }
      });
    }
  }
}
