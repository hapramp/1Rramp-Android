package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hapramp.R;
import com.hapramp.steem.models.Feed;
import com.hapramp.views.post.PostItemView;
import com.hapramp.views.profile.ProfileHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 10/25/2017.
 */

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final int VIEW_TYPE_ITEM = 2;
  private final int VIEW_TYPE_PROFILE_HEADER = 1;
  public static final int VIEW_TYPE_LOADING = 3;
  private final String mUsername;
  public Context mContext;
  private int s;
  private List<Feed> feeds;
  private boolean isLoading;
  private boolean hasChanceOfMoreFeeds;
  private boolean profileHeaderInitialized;

  public ProfileRecyclerAdapter(Context mContext, String username) {
    this.mContext = mContext;
    this.mUsername = username;
    feeds = new ArrayList<>();
  }

  private OnLoadMoreListener onLoadMoreListener;

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    RecyclerView.ViewHolder viewHolder = null;
    if (viewType == VIEW_TYPE_ITEM) {
      View view = new PostItemView(mContext);
      viewHolder = new PostViewHolder(view);
    } else if (viewType == VIEW_TYPE_PROFILE_HEADER) {
      View view = new ProfileHeaderView(mContext);
      viewHolder = new ProfileHeaderViewHolder(view);
    } else if (viewType == VIEW_TYPE_LOADING) {
      View _v = LayoutInflater.from(mContext).inflate(R.layout.feed_load_more_item, viewGroup, false);
      viewHolder = new LoadMoreViewHolder(_v);
    }
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int pos) {
    if (viewHolder instanceof PostViewHolder) {
      ((PostViewHolder) viewHolder).bind(feeds.get(pos - 1), new PostItemView.PostActionListener() {
        @Override
        public void onPostDeleted() {
          removeItemAt(pos);
        }
      });
    } else if (viewHolder instanceof ProfileHeaderViewHolder) {
      if (!profileHeaderInitialized) {
        ((ProfileHeaderViewHolder) viewHolder).setUsername(mUsername);
        profileHeaderInitialized = true;
      }
    } else if (viewHolder instanceof LoadMoreViewHolder) {
      if (!isLoading && onLoadMoreListener != null) {
        if (hasChanceOfMoreFeeds) {
          isLoading = true;
          onLoadMoreListener.onLoadMore();
          ((LoadMoreViewHolder) viewHolder).showLoadingView();
        } else {
          ((LoadMoreViewHolder) viewHolder).hideLoadingView();
        }
      }
    }
  }

  private void removeItemAt(int pos) {
    feeds.remove(pos - 1);
    notifyItemRemoved(pos);
  }

  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return VIEW_TYPE_PROFILE_HEADER;
    } else if (position > 0 && position <= feeds.size()) {
      return VIEW_TYPE_ITEM;
    } else {
      return VIEW_TYPE_LOADING;
    }
  }

  @Override
  public int getItemCount() {
    s = feeds.size();
    return s == 0 ? 1 : s + 2;
  }

  class PostViewHolder extends RecyclerView.ViewHolder {
    PostItemView postItemView;
    public PostViewHolder(View itemView) {
      super(itemView);
      postItemView = (PostItemView) itemView;
    }

    public void bind(final Feed postData, final PostItemView.PostActionListener postActionListener) {
      postItemView.setPostActionListener(postActionListener);
      postItemView.setPostData(postData);
    }

  }

  public void setPosts(List<Feed> newPosts) {
    isLoading = false;
    if (newPosts.size() == 0) {
      hasChanceOfMoreFeeds = false;
    } else {
      this.feeds = newPosts;
      hasChanceOfMoreFeeds = true;
      notifyItemRangeChanged(1, newPosts.size());
    }
  }

  public void appendPosts(List<Feed> appendable) {
    isLoading = false;
    int oldSize = feeds.size();
    if (appendable.size() == 0) {
      hasChanceOfMoreFeeds = false;
      notifyItemChanged(oldSize + 1);
    } else {
      hasChanceOfMoreFeeds = true;
      feeds.addAll(appendable);
      notifyItemInserted(oldSize + 1);
    }
  }

  public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    this.onLoadMoreListener = onLoadMoreListener;
  }

  public interface OnLoadMoreListener {
    void onLoadMore();
  }

  class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {
    ProfileHeaderView profileHeaderView;

    public ProfileHeaderViewHolder(View itemView) {
      super(itemView);
      profileHeaderView = (ProfileHeaderView) itemView;
    }

    public void setUsername(String username) {
      profileHeaderView.setUsername(username);
    }
  }


  class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.load_more_progress_view)
    ProgressBar loadingViewProgressBar;

    public LoadMoreViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void showLoadingView() {
      if (loadingViewProgressBar != null) {
        loadingViewProgressBar.setVisibility(View.VISIBLE);
      }
    }

    public void hideLoadingView() {
      if (loadingViewProgressBar != null) {
        loadingViewProgressBar.setVisibility(View.GONE);
      }
    }
  }
}
