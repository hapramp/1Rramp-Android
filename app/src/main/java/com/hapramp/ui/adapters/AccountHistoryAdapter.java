package com.hapramp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.search.TranserHistoryManager;
import com.hapramp.steem.models.TransferHistoryModel;
import com.hapramp.utils.ImageHandler;

import java.util.ArrayList;

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
				Log.d("Adapter", "Data" + transferHistoryModels.toString());
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
		public int getItemCount() {
				return transferHistoryModels.size();
		}

		@Override
		public int getItemViewType(int position) {
				switch (transferHistoryModels.get(position).getOperation()) {
						case TranserHistoryManager.KEYS.OPERATION_TRANSFER:
								return TYPE_TRANSFER;
						case TranserHistoryManager.KEYS.OPERATION_AUTHOR_REWARD:
								return TYPE_AUTHOR_REWARD;
						case TranserHistoryManager.KEYS.OPERATION_CLAIM_REWARD_BALANCE:
								return TYPE_CLAIM;
						case TranserHistoryManager.KEYS.OPERATION_COMMENT_BENEFACTOR_REWARD:
								return TYPE_COMMENT_BENEFACTOR;
						case TranserHistoryManager.KEYS.OPERATION_CURATION_REWARD:
								return TYPE_CURATION;
				}
				return super.getItemViewType(position);
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
						if (isSent(transferHistoryModel.getUserAccount(), transfer.from)) {
								//sent
								ImageHandler.loadCircularImage(mContext, userImage,
										String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format), transfer.to));
								messageLabel.setText("Transferred to");
								remoteUser.setText(transfer.to);
								amount.setText(String.format("- %s", transfer.amount));
								amount.setTextColor(Color.parseColor("#bf0707"));
						} else {
								//received
								ImageHandler.loadCircularImage(mContext, userImage,
										String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format), transfer.from));
								messageLabel.setText("Received from");
								remoteUser.setText(transfer.from);
								amount.setText(String.format("+ %s", transfer.amount));
								amount.setTextColor(Color.parseColor("#157c18"));
						}
						timestamp.setText(transferHistoryModel.getTimeStamp());
						message.setText(transfer.memo);
				}
		}

		private boolean isSent(String userAccount, String from) {
				return userAccount.equals(from);
		}

		class AuthorRewardViewHolder extends RecyclerView.ViewHolder {
				@BindView(R.id.message)
				TextView message;
				@BindView(R.id.sbd)
				TextView sbd;
				@BindView(R.id.steem)
				TextView steem;
				@BindView(R.id.sp)
				TextView sp;
				@BindView(R.id.timestamp)
				TextView timestamp;

				public AuthorRewardViewHolder(View itemView) {
						super(itemView);
						ButterKnife.bind(this, itemView);
				}

				public void bind(TransferHistoryModel transferHistoryModel) {
						TransferHistoryModel.AuthorReward authorReward = transferHistoryModel.getAuthorReward();
						sbd.setText(authorReward.getSbd_payout());
						steem.setText(authorReward.getSteem_payout());
						sp.setText(authorReward.getVesting_payout());
						timestamp.setText(transferHistoryModel.getTimeStamp());
						message.setText("Author Reward");
				}
		}

		class CommentBenefactorViewHolder extends RecyclerView.ViewHolder {
				@BindView(R.id.messsage)
				TextView message;
				@BindView(R.id.benefactor)
				TextView benefactor;
				@BindView(R.id.author)
				TextView author;
				@BindView(R.id.reward)
				TextView reward;
				@BindView(R.id.timestamp)
				TextView timestamp;

				public CommentBenefactorViewHolder(View itemView) {
						super(itemView);
						ButterKnife.bind(this, itemView);
				}

				public void bind(TransferHistoryModel transferHistoryModel) {
						TransferHistoryModel.CommentBenefactor commentBenefactor = transferHistoryModel.getCommentBenefactor();
						reward.setText(commentBenefactor.getReward());
						author.setText(commentBenefactor.getAuthor());
						benefactor.setText(commentBenefactor.getBenefactor());
						timestamp.setText(transferHistoryModel.getTimeStamp());
						message.setText("Comment Benefactor");
				}
		}

		class ClaimBalanceViewHolder extends RecyclerView.ViewHolder {
				@BindView(R.id.amount)
				TextView amount;
				@BindView(R.id.timestamp)
				TextView timestamp;

				public ClaimBalanceViewHolder(View itemView) {
						super(itemView);
						ButterKnife.bind(this, itemView);
				}

				public void bind(TransferHistoryModel transferHistoryModel) {
						TransferHistoryModel.ClaimRewardBalance claimRewardBalance = transferHistoryModel.getClaimRewardBalance();
						timestamp.setText(transferHistoryModel.getTimeStamp());
						amount.setText(String.format("%s\n%s\n%s", claimRewardBalance.getReward_sbd(), claimRewardBalance.getReward_steem(), claimRewardBalance.getReward_vests()));
				}
		}

		class CurationViewHolder extends RecyclerView.ViewHolder {
				@BindView(R.id.message)
				TextView message;
				@BindView(R.id.curator)
				TextView curator;
				@BindView(R.id.author)
				TextView author;
				@BindView(R.id.reward)
				TextView reward;
				@BindView(R.id.timestamp)
				TextView timestamp;

				public CurationViewHolder(View itemView) {
						super(itemView);
						ButterKnife.bind(this, itemView);
				}

				public void bind(TransferHistoryModel transferHistoryModel) {
						TransferHistoryModel.CurationReward curationReward = transferHistoryModel.getCurationReward();
						curator.setText(curationReward.getCurator());
						reward.setText(curationReward.getReward());
						author.setText(curationReward.getComment_author());
						timestamp.setText(transferHistoryModel.getTimeStamp());
						message.setText("Curation Reward");
				}
		}
}
