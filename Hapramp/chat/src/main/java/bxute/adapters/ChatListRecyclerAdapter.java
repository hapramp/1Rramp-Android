package bxute.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.chat.R;
import bxute.chat.R2;
import bxute.models.ChatRoom;

/**
 * Created by Ankit on 9/9/2017.
 */

public class ChatListRecyclerAdapter extends RecyclerView.Adapter<ChatListRecyclerAdapter.ChatListItemViewHolder> {

    @BindView(R2.id.chat_list_avatar)
    SimpleDraweeView chatListAvatar;
    @BindView(R2.id.chat_list_title)
    TextView chatListTitle;
    @BindView(R2.id.chat_list_last_message)
    TextView chatListLastMessage;
    @BindView(R2.id.online_status)
    TextView onlineStatus;
    private Context context;
    private ArrayList<ChatRoom> chatRooms;

    public ChatListRecyclerAdapter(Context context) {
        this.context = context;
        chatRooms = new ArrayList<>();
    }

    public void setChatRooms(ArrayList<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
        notifyDataSetChanged();
    }

    @Override
    public ChatListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_item, null, false);
        return null;
    }

    @Override
    public void onBindViewHolder(ChatListItemViewHolder chatListItemViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    class ChatListItemViewHolder extends RecyclerView.ViewHolder {

        public ChatListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ChatRoom chatRoom) {

        }

    }
}
