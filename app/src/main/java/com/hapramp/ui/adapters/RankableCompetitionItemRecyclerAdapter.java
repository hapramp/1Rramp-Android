package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.RankableCompetitionFeedItem;
import com.hapramp.views.competition.RankableCompetitionFeedItemView;

import java.util.ArrayList;
import java.util.Map;

public class RankableCompetitionItemRecyclerAdapter extends RecyclerView.Adapter<RankableCompetitionItemRecyclerAdapter.RankableCompetitionItemViewHolder> {
  private Context context;
  private ArrayList<RankableCompetitionFeedItem> rankableCompetitionFeedItems;
  private RankableCompetitionFeedItemView.RankableCompetitionItemListener rankableCompetitionItemListener;

  public RankableCompetitionItemRecyclerAdapter(Context context) {
    this.context = context;
    rankableCompetitionFeedItems = new ArrayList<>();
  }

  public void setRankableCompetitionFeedItems(ArrayList<RankableCompetitionFeedItem> rankableCompetitionFeedItems) {
    this.rankableCompetitionFeedItems = rankableCompetitionFeedItems;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public RankableCompetitionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new RankableCompetitionItemViewHolder(new RankableCompetitionFeedItemView(context));
  }

  @Override
  public void onBindViewHolder(@NonNull RankableCompetitionItemViewHolder holder, int position) {
    holder.bind(rankableCompetitionFeedItems.get(position), rankableCompetitionItemListener);
  }

  @Override
  public int getItemCount() {
    return rankableCompetitionFeedItems.size();
  }

  public void setRankList(Map<String, Integer> rankMap) {
    for (int i = 0; i < rankableCompetitionFeedItems.size(); i++) {
      String id = rankableCompetitionFeedItems.get(i).getItemId();
      if (rankMap.containsKey(id)) {
        rankableCompetitionFeedItems.get(i).setRank(rankMap.get(id));
      } else {
        rankableCompetitionFeedItems.get(i).setRank(0);
      }
    }
    notifyDataSetChanged();
  }

  public void setRankableCompetitionItemListener(RankableCompetitionFeedItemView.RankableCompetitionItemListener rankableCompetitionItemListener) {
    this.rankableCompetitionItemListener = rankableCompetitionItemListener;
  }

  class RankableCompetitionItemViewHolder extends RecyclerView.ViewHolder {

    RankableCompetitionFeedItemView rankableCompetitionFeedItemView;

    public RankableCompetitionItemViewHolder(View itemView) {
      super(itemView);
      rankableCompetitionFeedItemView = (RankableCompetitionFeedItemView) itemView;
    }

    public void bind(RankableCompetitionFeedItem data,
                     RankableCompetitionFeedItemView.RankableCompetitionItemListener rankableCompetitionItemListener) {
      rankableCompetitionFeedItemView.bindData(data);
      rankableCompetitionFeedItemView.setRankableCompetitionItemListener(rankableCompetitionItemListener);
    }
  }
}
