package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.RankableCompetitionFeedItem;
import com.hapramp.views.WinnerFeedItemView;

import java.util.ArrayList;

public class WinnersFeedListAdapter extends RecyclerView.Adapter<WinnersFeedListAdapter.WinnerFeedItemViewHolder>{
  private Context context;
  private ArrayList<RankableCompetitionFeedItem> winnerFeedItems;

  public WinnersFeedListAdapter(Context context) {
    this.context = context;
    winnerFeedItems = new ArrayList<>();
  }

  public void setWinnerFeedItems(ArrayList<RankableCompetitionFeedItem> winnerFeedItems) {
    this.winnerFeedItems = winnerFeedItems;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public WinnerFeedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new WinnerFeedItemViewHolder(new WinnerFeedItemView(context));
  }

  @Override
  public void onBindViewHolder(@NonNull WinnerFeedItemViewHolder holder, int position) {
    holder.bind(winnerFeedItems.get(position));
  }

  @Override
  public int getItemCount() {
    return winnerFeedItems.size();
  }

  class WinnerFeedItemViewHolder extends RecyclerView.ViewHolder {

    WinnerFeedItemView winnerFeedItemView;

    public WinnerFeedItemViewHolder(View itemView) {
      super(itemView);
      winnerFeedItemView = (WinnerFeedItemView) itemView;
    }

    public void bind(RankableCompetitionFeedItem data) {
      winnerFeedItemView.bindData(data);
    }
  }
}
