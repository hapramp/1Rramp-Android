package bxute.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.FontManager;
import bxute.LocalTimeFormatter;
import bxute.chat.R;
import bxute.chat.R2;
import bxute.config.ChatConfig;
import bxute.interfaces.ChatListitemClickListener;
import bxute.models.ChatRoom;
import bxute.views.OnlineIndicatorView;

/**
 * Created by Ankit on 9/9/2017.
 */

public class ChatListRecyclerAdapter extends RecyclerView.Adapter<ChatListRecyclerAdapter.ChatListItemViewHolder> {

    private Context context;
    private ArrayList<ChatRoom> chatRooms;
    private ChatListitemClickListener listitemClickListener;
    private Typeface typeface;
    private LocalTimeFormatter timeFormatter;

    public ChatListRecyclerAdapter(Context context, ChatListitemClickListener chatListitemClickListener) {
        this.context = context;
        this.listitemClickListener = chatListitemClickListener;
        chatRooms = new ArrayList<>();
        timeFormatter = new LocalTimeFormatter();
        typeface = new FontManager(context).getDefault();
    }

    public void setChatRooms(ArrayList<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
        notifyDataSetChanged();
    }

    @Override
    public ChatListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_item, null, false);
        return new ChatListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatListItemViewHolder chatListItemViewHolder, int i) {
        chatListItemViewHolder.bind(chatRooms.get(i), listitemClickListener);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    class ChatListItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.chat_list_avatar)
        SimpleDraweeView chatListAvatar;
        @BindView(R2.id.chat_list_title)
        TextView chatListTitle;
        @BindView(R2.id.chat_list_last_message)
        TextView chatListLastMessage;
        @BindView(R2.id.online_status)
        OnlineIndicatorView onlineStatus;
        @BindView(R2.id.unread_count)
        TextView unreadCount;
        View container;
        @BindView(R2.id.chat_list_last_message_time)
        TextView chatListLastMessageTime;

        public ChatListItemViewHolder(View itemView) {
            super(itemView);
            container = itemView.getRootView();
            ButterKnife.bind(this, itemView);
        }

        public void bind(final ChatRoom chatRoom, final ChatListitemClickListener listitemClickListener) {

            chatListAvatar.setImageURI(chatRoom.getChatRoomAvatar());
            chatListTitle.setText(chatRoom.getChatRoomName());
            chatListLastMessage.setText(chatRoom.getLastMessage().getContent());
            if (chatRoom.getUnreadCount() > 0) {
                unreadCount.setVisibility(View.VISIBLE);
                unreadCount.setText(String.valueOf(chatRoom.getUnreadCount()));
            } else {
                unreadCount.setVisibility(View.INVISIBLE);
            }
            chatListLastMessageTime.setText(timeFormatter.getFormattedTimeStamp(chatRoom.getLastMessage().getSent_time(),LocalTimeFormatter.FORMAT_CHAT_WALL));

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listitemClickListener.onItemClicked(ChatConfig.getCompanionIdFromChatRoomId(chatRoom.getChatRoomId()));
                }
            });

            onlineStatus.setUserId(ChatConfig.getCompanionIdFromChatRoomId(chatRoom.getChatRoomId()), typeface);

        }

    }

}
