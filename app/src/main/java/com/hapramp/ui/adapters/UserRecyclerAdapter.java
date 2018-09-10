package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hapramp.R;
import com.hapramp.views.UserItemView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final int VIEW_TYPE_ITEM = 0;
  private final int VIEW_TYPE_LOADING = 1;
  Context context;
  ArrayList<String> users;
  private boolean isLoading = true;
  private OnLoadMoreUserLoadListener onLoadMoreUserLoadListener;
  private boolean isMoreAvailable = true;
  private UserItemView.FollowStateChangeListener followStateChangeLister;

  public UserRecyclerAdapter(Context context) {
    this.context = context;
    users = new ArrayList<>();
  }

  public void setUsers(ArrayList<String> users) {
    this.users = users;
    notifyDataSetChanged();
    isLoading = false;
    if (users.size() == 0) {
      isMoreAvailable = false;
    }
  }

  public void appendUsers(ArrayList<String> more_users) {
    int change_pos = users.size();
    this.users.addAll(more_users);
    if (more_users.size() > 0) {
      notifyItemInserted(change_pos);
    } else {
      isMoreAvailable = false;
      notifyItemChanged(users.size());
    }
    isLoading = false;
  }

  public void setFollowStateChangeListener(UserItemView.FollowStateChangeListener followStateChangeListener) {
    this.followStateChangeLister = followStateChangeListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_ITEM) {
      UserItemView userSearchItemView = new UserItemView(context);
      return new UserItemViewHolder(userSearchItemView);
    } else {
      View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);
      return new LoadingViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof LoadingViewHolder) {
      if (onLoadMoreUserLoadListener != null && !isLoading) {
        if (isMoreAvailable) {
          isLoading = true;
          onLoadMoreUserLoadListener.onLoadMore();
          ((LoadingViewHolder) holder).setLoadingProgressBarVisibility(true);
        } else {
          ((LoadingViewHolder) holder).setLoadingProgressBarVisibility(false);
        }
      }
    } else if (holder instanceof UserItemViewHolder) {
      ((UserItemViewHolder) holder).setUserName(users.get(position), followStateChangeLister);
    }
  }

  @Override
  public int getItemViewType(int position) {
    return (users.size() == position) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getItemCount() {
    return users.size() + 1;
  }

  public void setOnLoadMoreUserLoadListener(OnLoadMoreUserLoadListener onLoadMoreUserLoadListener) {
    this.onLoadMoreUserLoadListener = onLoadMoreUserLoadListener;
  }

  public interface OnLoadMoreUserLoadListener {
    void onLoadMore();
  }

  class UserItemViewHolder extends RecyclerView.ViewHolder {
    public UserItemViewHolder(View itemView) {
      super(itemView);
    }

    public void setUserName(String username, final UserItemView.FollowStateChangeListener followStateChangeListener) {
      ((UserItemView) itemView).setUsername(username);
      ((UserItemView) itemView).setFollowStateChangeListener(new UserItemView.FollowStateChangeListener() {
        @Override
        public void onFollowStateChanged() {
          if (followStateChangeListener != null) {
            followStateChangeListener.onFollowStateChanged();
          }
        }
      });
    }
  }

  public class LoadingViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.loading_progress_bar)
    ProgressBar loadingProgressBar;

    public LoadingViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    public void setLoadingProgressBarVisibility(boolean show) {
      loadingProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

  }
}
