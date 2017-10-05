package bxute.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import bxute.FontManager;
import bxute.chat.R;
import bxute.chat.R2;
import bxute.config.MessageStatus;
import bxute.models.Message;

/**
 * Created by Ankit on 9/12/2017.
 */

public class ChatRoomRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Message> messages;
    private int VIEW_TYPE_INCOMMING = 12;
    private int VIEW_TYPE_OUTGOING = 11;
    Typeface typeface;

    public ChatRoomRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        messages = new ArrayList<>();
        typeface = new FontManager(mContext).getDefault();
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).isIncommingMessage()) {
            return VIEW_TYPE_INCOMMING;
        } else {
            return VIEW_TYPE_OUTGOING;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_INCOMMING) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_bubble_incomming, null, false);
            return new IncommingBubbleViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_bubble_outgoing, null, false);
            return new OutgoingBubbleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof OutgoingBubbleViewHolder) {
            ((OutgoingBubbleViewHolder) viewHolder).bind(messages.get(position));
        } else {
            ((IncommingBubbleViewHolder) viewHolder).bind(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class IncommingBubbleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.content)
        TextView content;
        @BindView(R2.id.time)
        TextView time;

        public IncommingBubbleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Message message) {
            content.setText(message.getContent());
            time.setText(message.getSent_time());
        }
    }

    class OutgoingBubbleViewHolder extends RecyclerView.ViewHolder {


        @BindView(R2.id.content)
        TextView content;
        @BindView(R2.id.time)
        TextView time;
        @BindView(R2.id.msg_state)
        TextView msgState;

        public OutgoingBubbleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Message message) {
            content.setText(message.getContent());
            time.setText(message.getSent_time());
            msgState.setTypeface(typeface);

            switch (message.getStatus()){
                case MessageStatus.STATUS_SENT:
                    msgState.setTextColor(Color.GRAY);
                    msgState.setText(FontManager.SENT_ICON);
                    break;
                case MessageStatus.STATUS_DELIVERED:
                    msgState.setTextColor(Color.GRAY);
                    msgState.setText(FontManager.DELIVERED_ICON);
                    break;
                case MessageStatus.STATUS_SEEN:
                    msgState.setText(FontManager.DELIVERED_ICON);
                    msgState.setTextColor(Color.GREEN);
                    break;
            }
        }
    }

}
