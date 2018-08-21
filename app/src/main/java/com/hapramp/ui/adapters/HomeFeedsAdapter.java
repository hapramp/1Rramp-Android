package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.R;
import com.hapramp.steem.models.Feed;
import com.hapramp.views.post.PostItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 2/12/2018.
 */

public class HomeFeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int VIEW_TYPE_FEED = 0;
  public static final int VIEW_TYPE_LOADING = 1;
  private Context mContext;
  private boolean hasMoreToLoad = false;
  private List<Feed> feeds;
  private int totalItemCount;
  private int lastVisibleItem;
  private boolean isLoading = false;
  private int visibleThreshold = 2;
  private OnLoadMoreListener mOnLoadMoreListener;

  public HomeFeedsAdapter(Context mContext, RecyclerView mRecyclerView) {
    this.mContext = mContext;
    feeds = new ArrayList<>();
    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        totalItemCount = linearLayoutManager.getItemCount();
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) && hasMoreToLoad) {
          if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
          }
          isLoading = true;
        }
      }
    });
  }

  public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
    this.mOnLoadMoreListener = mOnLoadMoreListener;
  }

  public void setFeeds(List<Feed> feeds) {
    this.feeds = feeds;
    notifyItemRangeChanged(0, feeds.size());
    notifyDataSetChanged();
  }

  public void appendFeeds(List<Feed> additionalFeeds) {
    int oldSize = feeds.size();
    feeds.addAll(additionalFeeds);
    notifyItemRangeChanged(oldSize - 1, feeds.size());
    isLoading = false;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder viewHolder = null;
    if (viewType == VIEW_TYPE_FEED) {
      viewHolder = new FeedViewHolder(new PostItemView(mContext));
    } else if (viewType == VIEW_TYPE_LOADING) {
      View _v = LayoutInflater.from(mContext).inflate(R.layout.feed_load_more_item, null);
      viewHolder = new LoadMoreViewHolder(_v);
    }
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    if (holder instanceof FeedViewHolder) {
      ((FeedViewHolder) holder).bind(feeds.get(position), new PostItemView.PostActionListener() {
        @Override
        public void onPostDeleted() {
          removeItemAt(position);
        }
      });
    } else if (holder instanceof LoadMoreViewHolder) {
      ((LoadMoreViewHolder) holder).startSimmer();
    }
  }

  private void removeItemAt(int position) {
    feeds.remove(position);
    notifyItemRemoved(position);
  }

  @Override
  public int getItemViewType(int position) {
    if (position >= feeds.size()) {
      return VIEW_TYPE_LOADING;
    } else {
      return VIEW_TYPE_FEED;
    }
  }

  @Override
  public int getItemCount() {
    return feeds.size() + (hasMoreToLoad ? 1 : 0);
  }

  public int getFeedsCount() {
    return feeds.size();
  }

  public void setHasMoreToLoad(boolean hasMoreToLoad) {
    this.hasMoreToLoad = hasMoreToLoad;
  }

  public void resetList() {
    feeds = new ArrayList<>();
    notifyDataSetChanged();
  }

  public interface OnLoadMoreListener {
    void onLoadMore();
  }

  class FeedViewHolder extends RecyclerView.ViewHolder {
    PostItemView postItemView;

    public FeedViewHolder(View itemView) {
      super(itemView);
      postItemView = (PostItemView) itemView;
    }

    public void bind(final Feed postData, final PostItemView.PostActionListener postActionListener) {
      postItemView.setPostActionListener(postActionListener);
      postItemView.setPostData(postData);
    }
  }

  class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    ShimmerFrameLayout shimmerFrameLayout;

    public LoadMoreViewHolder(View itemView) {
      super(itemView);
      shimmerFrameLayout = itemView.findViewById(R.id.shimmer_view_container);
    }

    public void startSimmer() {
      shimmerFrameLayout.setVisibility(View.VISIBLE);
    }

    public void hideView() {
      shimmerFrameLayout.setVisibility(View.GONE);
    }

  }
}
