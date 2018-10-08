package com.hapramp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.notification.model.BaseNotificationModel;
import com.hapramp.notification.model.FollowNotificationModel;
import com.hapramp.notification.model.MentionNotificationModel;
import com.hapramp.notification.model.ReblogNotificationModel;
import com.hapramp.notification.model.ReplyNotificationModel;
import com.hapramp.notification.model.TransferNotificationModel;
import com.hapramp.notification.model.VoteNotificationModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.ui.activity.AccountHistoryActivity;
import com.hapramp.ui.activity.DetailedActivity;
import com.hapramp.ui.activity.ProfileActivity;
import com.hapramp.utils.Constants;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hapramp.notification.NotificationKey.NOTIFICATION_TYPE_FOLLOW;
import static com.hapramp.notification.NotificationKey.NOTIFICATION_TYPE_MENTION;
import static com.hapramp.notification.NotificationKey.NOTIFICATION_TYPE_REBLOG;
import static com.hapramp.notification.NotificationKey.NOTIFICATION_TYPE_REPLY;
import static com.hapramp.notification.NotificationKey.NOTIFICATION_TYPE_TRANSFER;
import static com.hapramp.notification.NotificationKey.NOTIFICATION_TYPE_VOTE;
import static com.hapramp.ui.activity.AccountHistoryActivity.EXTRA_USERNAME;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int TYPE_FOLLOW = 0;
  public static final int TYPE_MENTION = 1;
  public static final int TYPE_REBLOG = 2;
  public static final int TYPE_REPLY = 3;
  public static final int TYPE_TRANSFER = 4;
  public static final int TYPE_VOTE = 5;
  public static final int TYPE_NONE = 6;

  ArrayList<BaseNotificationModel> notificationModels;


  public NotificationAdapter() {
    notificationModels = new ArrayList<>();
  }

  public void setNotificationModels(ArrayList<BaseNotificationModel> notificationModels) {
    this.notificationModels = notificationModels;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_view, parent, false);
    if (viewType == TYPE_FOLLOW) {
      return new FollowItemViewHolder(view);
    } else if (viewType == TYPE_MENTION) {
      return new MentionItemViewHolder(view);
    } else if (viewType == TYPE_REBLOG) {
      return new ReblogItemViewHolder(view);
    } else if (viewType == TYPE_REPLY) {
      return new ReplyItemViewHolder(view);
    } else if (viewType == TYPE_TRANSFER) {
      return new TransferItemViewHolder(view);
    } else if (viewType == TYPE_VOTE) {
      return new VoteItemViewHolder(view);
    }
    return new NoTypeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof VoteItemViewHolder) {
      ((VoteItemViewHolder) holder).bind((VoteNotificationModel) notificationModels.get(position));
    } else if (holder instanceof FollowItemViewHolder) {
      ((FollowItemViewHolder) holder).bind((FollowNotificationModel) notificationModels.get(position));
    } else if (holder instanceof MentionItemViewHolder) {
      ((MentionItemViewHolder) holder).bind((MentionNotificationModel) notificationModels.get(position));
    } else if (holder instanceof ReblogItemViewHolder) {
      ((ReblogItemViewHolder) holder).bind((ReblogNotificationModel) notificationModels.get(position));
    } else if (holder instanceof ReplyItemViewHolder) {
      ((ReplyItemViewHolder) holder).bind((ReplyNotificationModel) notificationModels.get(position));
    } else if (holder instanceof TransferItemViewHolder) {
      ((TransferItemViewHolder) holder).bind((TransferNotificationModel) notificationModels.get(position));
    }
  }

  @Override
  public int getItemViewType(int position) {
    switch (notificationModels.get(position).getType()) {
      case NOTIFICATION_TYPE_VOTE:
        return TYPE_VOTE;
      case NOTIFICATION_TYPE_FOLLOW:
        return TYPE_FOLLOW;
      case NOTIFICATION_TYPE_MENTION:
        return TYPE_MENTION;
      case NOTIFICATION_TYPE_REBLOG:
        return TYPE_REBLOG;
      case NOTIFICATION_TYPE_REPLY:
        return TYPE_REPLY;
      case NOTIFICATION_TYPE_TRANSFER:
        return TYPE_TRANSFER;
      default:
        return TYPE_NONE;
    }
  }

  @Override
  public int getItemCount() {
    return notificationModels.size();
  }

  private void navigateToProfilePage(Context context,
                                     String username,
                                     String notifId) {
    Intent intent = new Intent(context, ProfileActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.EXTRAA_KEY_STEEM_USER_NAME, username);
    bundle.putString(Constants.EXTRAA_KEY_NOTIFICATION_ID, notifId);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  private void navigateToDetailsPage(Context context,
                                     String notificationId,
                                     String author,
                                     String parentPermlink,
                                     String permlink) {
    Intent intent = new Intent(context, DetailedActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.EXTRAA_KEY_NOTIFICATION_ID, notificationId);
    bundle.putString(Constants.EXTRAA_KEY_POST_AUTHOR, author);
    bundle.putString(Constants.EXTRAA_KEY_PARENT_PERMLINK, parentPermlink);
    bundle.putString(Constants.EXTRAA_KEY_POST_PERMLINK, permlink);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  private void navigateToTransferPage(Context context,
                                      String notificationId) {
    Intent intent = new Intent(context, AccountHistoryActivity.class);
    Bundle bundle = new Bundle();
    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
    bundle.putString(EXTRA_USERNAME, username);
    bundle.putString(Constants.EXTRAA_KEY_NOTIFICATION_ID, notificationId);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  class FollowItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.text)
    TextView text;

    public FollowItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final FollowNotificationModel followNotificationModel) {
      String userImageUrl = String.format(itemView.getContext()
        .getString(R.string.steem_user_profile_pic_format_large), followNotificationModel.getFollower());
      ImageHandler.loadCircularImage(itemView.getContext(), userImage, userImageUrl);
      text.setText(String.format("%s Started following you.", followNotificationModel.getFollower()));
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToProfilePage(itemView.getContext(),
            followNotificationModel.getFollower(),
            followNotificationModel.getNotificationId());
        }
      });
    }
  }

  class MentionItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.timestamp)
    TextView timestamp;

    public MentionItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final MentionNotificationModel mentionNotificationModel) {
      String userImageUrl = String.format(itemView.getContext()
        .getString(R.string.steem_user_profile_pic_format_large), mentionNotificationModel.getAuthor());
      ImageHandler.loadCircularImage(itemView.getContext(), userImage, userImageUrl);
      text.setText(String.format("%s Mentioned you in a post", mentionNotificationModel.getAuthor()));
      timestamp.setText(MomentsUtils.getFormattedTime(mentionNotificationModel.getTimestamp()));
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToDetailsPage(itemView.getContext(),
            mentionNotificationModel.getNotificationId(),
            mentionNotificationModel.getAuthor(),
            mentionNotificationModel.getParent_permlink(),
            mentionNotificationModel.getPermlink());
        }
      });
    }
  }

  class ReblogItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.timestamp)
    TextView timestamp;

    public ReblogItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final ReblogNotificationModel reblogNotificationModel) {
      String userImageUrl = String.format(itemView.getContext()
        .getString(R.string.steem_user_profile_pic_format_large), reblogNotificationModel.getAccount());
      ImageHandler.loadCircularImage(itemView.getContext(), userImage, userImageUrl);
      text.setText(String.format("%s Reblogged your post", reblogNotificationModel.getAccount()));
      timestamp.setText(MomentsUtils.getFormattedTime(reblogNotificationModel.getTimestamp()));
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //you are the author of the post
          final String author = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
          navigateToDetailsPage(itemView.getContext(),
            reblogNotificationModel.getNotificationId(),
            author,
            reblogNotificationModel.getParentPermlink(),
            reblogNotificationModel.getPermlink());
        }
      });
    }
  }

  class ReplyItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.timestamp)
    TextView timestamp;

    public ReplyItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final ReplyNotificationModel replyNotificationModel) {
      String userImageUrl = String.format(itemView.getContext()
        .getString(R.string.steem_user_profile_pic_format_large), replyNotificationModel.getAuthor());
      ImageHandler.loadCircularImage(itemView.getContext(), userImage, userImageUrl);
      text.setText(String.format("%s Replied on your post.", replyNotificationModel.getAuthor()));
      timestamp.setText(MomentsUtils.getFormattedTime(replyNotificationModel.getTimestamp()));
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToDetailsPage(itemView.getContext(),
            replyNotificationModel.getNotificationId(),
            replyNotificationModel.getAuthor(),
            replyNotificationModel.getParent_permlink(),
            replyNotificationModel.getPermlink());
        }
      });
    }
  }

  class TransferItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.timestamp)
    TextView timestamp;

    public TransferItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final TransferNotificationModel transferNotificationModel) {
      String userImageUrl = String.format(itemView.getContext()
        .getString(R.string.steem_user_profile_pic_format_large), transferNotificationModel.getFrom());
      ImageHandler.loadCircularImage(itemView.getContext(), userImage, userImageUrl);
      timestamp.setText(MomentsUtils.getFormattedTime(transferNotificationModel.getTimestamp()));
      text.setText(String.format("%s Sent %s to you.", transferNotificationModel.getFrom(),
        transferNotificationModel.getAmount()));
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          navigateToTransferPage(itemView.getContext(),
            transferNotificationModel.getNotificationId());
        }
      });
    }
  }

  class VoteItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.timestamp)
    TextView timestamp;

    public VoteItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final VoteNotificationModel voteNotificationModel) {
      String userImageUrl = String.format(itemView.getContext()
        .getString(R.string.steem_user_profile_pic_format_large), voteNotificationModel.getVoter());
      ImageHandler.loadCircularImage(itemView.getContext(), userImage, userImageUrl);
      timestamp.setText(MomentsUtils.getFormattedTime(voteNotificationModel.getTimestamp()));
      text.setText(String.format("%s Voted your post", voteNotificationModel.getVoter()));
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //you are the author of the post
          final String author = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
          navigateToDetailsPage(itemView.getContext(),
            voteNotificationModel.getNotificationId(),
            author,
            voteNotificationModel.getParent_permlink(),
            voteNotificationModel.getPermlink());
        }
      });
    }
  }

  class NoTypeViewHolder extends RecyclerView.ViewHolder {

    public NoTypeViewHolder(View itemView) {
      super(itemView);
    }
  }
}
