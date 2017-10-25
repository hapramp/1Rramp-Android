package com.hapramp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.models.response.CompetionResponse;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Ankit on 10/25/2017.
 */

public class CompetionRecyclerAdapter extends RecyclerView.Adapter<CompetionRecyclerAdapter.CompetitionViewHolder> {

    public Context mContext;
    public List<CompetionResponse> competionResponses;

    public CompetionRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setPostResponses(List<CompetionResponse> competionResponses) {
        this.competionResponses = competionResponses;
        notifyDataSetChanged();
    }

    @Override
    public CompetitionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.competition_card, null);
        return new CompetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompetitionViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return competionResponses != null ? competionResponses.size() : 0;
    }

    class CompetitionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.feed_owner_pic)
        SimpleDraweeView feedOwnerPic;
        @BindView(R.id.feed_owner_title)
        TextView feedOwnerTitle;
        @BindView(R.id.feed_owner_subtitle)
        TextView feedOwnerSubtitle;
        @BindView(R.id.post_header_container)
        RelativeLayout postHeaderContainer;
        @BindView(R.id.tags)
        TextView tags;
        @BindView(R.id.entry_fee_icon)
        TextView entryFeeIcon;
        @BindView(R.id.entry_fee_caption)
        TextView entryFeeCaption;
        @BindView(R.id.entry_fee)
        TextView entryFee;
        @BindView(R.id.prize_money_icon)
        TextView prizeMoneyIcon;
        @BindView(R.id.prize_caption)
        TextView prizeCaption;
        @BindView(R.id.prize_money)
        TextView prizeMoney;
        @BindView(R.id.join_now_btn)
        TextView joinNowBtn;
        @BindView(R.id.know_more_btn)
        TextView knowMoreBtn;
        @BindView(R.id.participantIcon)
        TextView participantIcon;
        @BindView(R.id.participantCount)
        TextView participantCount;
        @BindView(R.id.shareIcon)
        TextView shareIcon;
        @BindView(R.id.shareCount)
        TextView shareCount;

        public CompetitionViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(CompetionResponse competionResponse) {
            // TODO: 10/25/2017 bind data
        }
    }

    interface OnPostElementsClickListener {
        void onReadMoreTapped();
    }
}
