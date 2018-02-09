package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.activity.NotificationsActivity;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.MarkAsReadNotificationCallback;
import com.hapramp.models.response.NotificationResponse;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 12/27/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> implements MarkAsReadNotificationCallback {

    List<NotificationResponse.Notification> notifications;
    private Context mContext;

    public NotificationsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setNotifications(List<NotificationResponse.Notification> notifications) {
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
        holder.bind(notifications.get(position),this);
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    public void markAllRead() {
        for (NotificationResponse.Notification n:notifications){
            n.is_read = true;
        }
        notifyDataSetChanged();
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
            ButterKnife.bind(this,itemView);
        }

        public void bind(final NotificationResponse.Notification notification , final NotificationsAdapter adapter){

            notificationContent.setText(notification.content);
            moment.setText(MomentsUtils.getFormattedTime(notification.created_at));
            if(notification.is_read){
                markAsRead.setVisibility(View.GONE);
            }else{
                markAsRead.setVisibility(View.VISIBLE);
                markAsRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.markAsRead(notification.id,getAdapterPosition());
                    }
                });
            }
        }

    }

    private void markAsRead(int notification_id,int pos) {
        DataServer.markNotificationAsRead(notification_id,pos,this);
    }


    @Override
    public void onNotificationMarkedAsRead(int pos) {
        notifications.get(pos).is_read = true;
        notifyItemChanged(pos);
    }

    @Override
    public void onNotificationMarkAsReadFailed() {

    }

}
