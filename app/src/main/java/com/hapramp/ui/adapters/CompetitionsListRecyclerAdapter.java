package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hapramp.R;
import com.hapramp.models.CompetitionModel;
import com.hapramp.views.competition.CompetitionFeedItemView;

import java.util.ArrayList;
import java.util.List;

public class CompetitionsListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int VIEW_TYPE_COMPETITION = 0;
  public static final int VIEW_TYPE_LOADING = 1;

  List<CompetitionModel> competitions;
  Context context;
  private boolean hasMoreCompetitionsToLoad = true;
  private CompetitionFeedItemView.CompetitionItemDeleteListener mDeleteListener;
  private LoadMoreCompetitionsCallback loadMoreCallback;

  public CompetitionsListRecyclerAdapter(Context context) {
    this.context = context;
    competitions = new ArrayList<>();
  }

  public void setCompetitions(List<CompetitionModel> competitions) {
    this.competitions = competitions;
    notifyDataSetChanged();
  }

  public void setDeleteListener(CompetitionFeedItemView.CompetitionItemDeleteListener deleteListener) {
    this.mDeleteListener = deleteListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_COMPETITION) {
      return new CompetitionItemViewHolder(new CompetitionFeedItemView(context));
    } else {
      View view = LayoutInflater.from(context).inflate(R.layout.more_competitions_loading, parent, false);
      return new CompetitionLoadingItemViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof CompetitionItemViewHolder) {
      ((CompetitionItemViewHolder) holder).bind(competitions.get(position), mDeleteListener);
    } else if (holder instanceof CompetitionLoadingItemViewHolder) {
      ((CompetitionLoadingItemViewHolder) holder).showProgress(true);
      if (loadMoreCallback != null) {
        loadMoreCallback.loadMoreCompetitions();
      }
    }
  }

  @Override
  public int getItemViewType(int position) {
    return position < competitions.size() ? VIEW_TYPE_COMPETITION : VIEW_TYPE_LOADING;
  }

  @Override
  public int getItemCount() {
    return competitions.size() == 0 ? 0 : competitions.size() + (hasMoreCompetitionsToLoad ? 1 : 0);
  }

  public void appendCompetitions(ArrayList<CompetitionModel> competitionModels) {
    if (competitionModels.size() == 0) {
      noMoreCompetitionsAvailableToLoad();
    } else {
      int lastIndex = competitions.size();
      competitions.addAll(competitionModels);
      notifyItemRangeChanged(lastIndex, competitionModels.size());
    }
  }

  public void noMoreCompetitionsAvailableToLoad() {
    hasMoreCompetitionsToLoad = false;
    notifyItemRangeChanged(competitions.size(), 1);
  }

  public void setLoadMoreCallback(LoadMoreCompetitionsCallback loadMoreCallback) {
    this.loadMoreCallback = loadMoreCallback;
  }

  public interface LoadMoreCompetitionsCallback {
    void loadMoreCompetitions();
  }

  class CompetitionItemViewHolder extends RecyclerView.ViewHolder {
    CompetitionFeedItemView competitionFeedItemView;

    public CompetitionItemViewHolder(View itemView) {
      super(itemView);
      competitionFeedItemView = (CompetitionFeedItemView) itemView;
    }

    public void bind(CompetitionModel competitionModel, CompetitionFeedItemView.CompetitionItemDeleteListener deleteListener) {
      competitionFeedItemView.bindCompetitionData(competitionModel);
      competitionFeedItemView.setDeleteListener(deleteListener);
    }
  }

  class CompetitionLoadingItemViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public CompetitionLoadingItemViewHolder(View itemView) {
      super(itemView);
      progressBar = itemView.findViewById(R.id.loading_progress_bar);
    }

    public void showProgress(boolean show) {
      if (show) {
        progressBar.setVisibility(View.VISIBLE);
      } else {
        progressBar.setVisibility(View.GONE);
      }
    }
  }
}
