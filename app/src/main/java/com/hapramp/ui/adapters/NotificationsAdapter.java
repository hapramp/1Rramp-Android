package com.hapramp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.MarkAsReadNotificationCallback;
import com.hapramp.push.NotificationPayloadModel;
import com.hapramp.ui.activity.DetailedActivity;
import com.hapramp.utils.Constants;
import com.hapramp.utils.MomentsUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 12/27/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> implements MarkAsReadNotificationCallback {

  List<NotificationPayloadModel> notifications;
  private Context mContext;

  public NotificationsAdapter(Context mContext) {
    this.mContext = mContext;
  }

  public void setNotifications(List<NotificationPayloadModel> notifications) {
    this.notifications = notifications;
    notifyDataSetChanged();
  }


  @Override
  public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item_view, null);
    return new NotificationViewHolder(view);

  }

  @Override
  public void onBindViewHolder(NotificationViewHolder holder, int position) {
    holder.bind(mContext, notifications.get(position), this);
  }

  @Override
  public int getItemCount() {
    return notifications != null ? notifications.size() : 0;
  }

  public void markAllRead() {
    for (NotificationPayloadModel n : notifications) {
      n.setIsRead(true);
    }
    notifyDataSetChanged();
  }

  private void markAsRead(int notification_id, int pos) {
    DataServer.markNotificationAsRead(notification_id, pos, this);
  }

  @Override
  public void onNotificationMarkedAsRead(int pos) {
    notifications.get(pos).setIsRead(true);
    notifyItemChanged(pos);
  }

  @Override
  public void onNotificationMarkAsReadFailed() {

  }

  class NotificationViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.notification_content)
    TextView notificationContent;
    @BindView(R.id.markAsRead)
    TextView markAsRead;
    @BindView(R.id.moment)
    TextView moment;

    public NotificationViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final Context context, final NotificationPayloadModel notification, final NotificationsAdapter adapter) {

      notificationContent.setText(notification.getContent());
      moment.setText(MomentsUtils.getFormattedTime(notification.getCreatedAt()));
      if (notification.getIsRead()) {
        markAsRead.setVisibility(View.GONE);
      } else {
        markAsRead.setVisibility(View.VISIBLE);
        markAsRead.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            adapter.markAsRead(notification.getId(), getAdapterPosition());
          }
        });
      }

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent notificationIntent = new Intent(mContext, DetailedActivity.class);
          notificationIntent.putExtra(Constants.EXTRAA_KEY_POST_PERMLINK, notification.getArg1());
          context.startActivity(notificationIntent);
        }
      });
    }

  }

}
