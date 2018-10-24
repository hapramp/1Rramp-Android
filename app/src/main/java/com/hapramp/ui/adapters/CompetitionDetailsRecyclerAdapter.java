package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hapramp.models.CompetitionModel;
import com.hapramp.steem.models.Feed;
import com.hapramp.views.CompetitionDetailsHeaderView;
import com.hapramp.views.post.PostItemView;

import java.util.ArrayList;
import java.util.List;

public class CompetitionDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int TYPE_HEADER = 0;
  public static final int TYPE_SUBMISSION = 1;
  CompetitionModel competitionHeaderModel;
  List<Feed> submissions;
  private Context context;

  public CompetitionDetailsRecyclerAdapter(Context context, CompetitionModel header) {
    this.competitionHeaderModel = header;
    this.context = context;
    submissions = new ArrayList<>();
  }

  public void setCompetitionHeaderModel(CompetitionModel competitionHeaderModel) {
    this.competitionHeaderModel = competitionHeaderModel;
    notifyItemChanged(0);
  }

  public void addSubmissions(List<Feed> submissions) {
    this.submissions = submissions;
    if (submissions.size() > 0) {
      notifyItemRangeChanged(1, submissions.size());
    }
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == TYPE_HEADER) {
      return new CompetitionHeaderItemViewHolder(new CompetitionDetailsHeaderView(context));
    } else {
      return new SubmissionItemViewHolder(new PostItemView(context));
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof CompetitionHeaderItemViewHolder) {
      ((CompetitionHeaderItemViewHolder) holder).bind(competitionHeaderModel);
    } else if (holder instanceof SubmissionItemViewHolder) {
      ((SubmissionItemViewHolder) holder).bind(submissions.get(position - 1));
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return TYPE_HEADER;
    } else {
      return TYPE_SUBMISSION;
    }
  }

  @Override
  public int getItemCount() {
    return 1 + submissions.size();
  }

  class SubmissionItemViewHolder extends RecyclerView.ViewHolder {
    PostItemView postItemView;

    public SubmissionItemViewHolder(View itemView) {
      super(itemView);
      postItemView = (PostItemView) itemView;
    }

    public void bind(final Feed postData) {
      postItemView.setPostData(postData);
    }
  }

  class CompetitionHeaderItemViewHolder extends RecyclerView.ViewHolder {

    CompetitionDetailsHeaderView competitionDetailsHeaderView;

    public CompetitionHeaderItemViewHolder(View itemView) {
      super(itemView);
      competitionDetailsHeaderView = (CompetitionDetailsHeaderView) itemView;
    }

    public void bind(CompetitionModel competition) {
      competitionDetailsHeaderView.bindCompetitionHeaderData(competition);
    }
  }
}
