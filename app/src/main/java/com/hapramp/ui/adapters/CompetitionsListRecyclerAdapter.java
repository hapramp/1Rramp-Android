package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.CompetitionModel;
import com.hapramp.views.competition.CompetitionFeedItemView;

import java.util.ArrayList;
import java.util.List;

public class CompetitionsListRecyclerAdapter extends RecyclerView.Adapter<CompetitionsListRecyclerAdapter.CompetitionItemViewHolder> {

  private boolean showDeclarationButton;
  List<CompetitionModel> competitions;
  Context context;

  public CompetitionsListRecyclerAdapter(Context context) {
    this.context = context;
    competitions = new ArrayList<>();
  }

  public void setCompetitions(List<CompetitionModel> competitions) {
    this.competitions = competitions;
    notifyDataSetChanged();
  }

  public void setShowDeclarationButton(boolean showDeclarationButton) {
    this.showDeclarationButton = showDeclarationButton;
  }

  @NonNull
  @Override
  public CompetitionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new CompetitionItemViewHolder(new CompetitionFeedItemView(context));
  }

  @Override
  public void onBindViewHolder(@NonNull CompetitionItemViewHolder holder, int position) {
    holder.bind(competitions.get(position));
  }

  @Override
  public int getItemCount() {
    return competitions.size();
  }

  class CompetitionItemViewHolder extends RecyclerView.ViewHolder {
    CompetitionFeedItemView competitionFeedItemView;

    public CompetitionItemViewHolder(View itemView) {
      super(itemView);
      competitionFeedItemView = (CompetitionFeedItemView) itemView;
    }

    public void bind(CompetitionModel competitionModel) {
      competitionFeedItemView.bindCompetitionData(competitionModel);
    }
  }
}
