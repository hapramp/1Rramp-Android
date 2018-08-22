package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hapramp.R;
import com.hapramp.steem.models.Feed;
import com.hapramp.views.post.PostItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 2/12/2018.
 */

public class HomeFeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int VIEW_TYPE_FEED = 0;
  public static final int VIEW_TYPE_LOADING = 1;
  private Context mContext;
  private List<Feed> feeds;
  private boolean isLoading;
  private boolean hasChanceOfMoreFeeds;
  private OnLoadMoreListener mOnLoadMoreListener;

  public HomeFeedsAdapter(Context mContext) {
    this.mContext = mContext;
    feeds = new ArrayList<>();
    isLoading = true;
  }

  public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
    this.mOnLoadMoreListener = mOnLoadMoreListener;
  }

  public void setFeeds(List<Feed> feeds) {
    isLoading = false;
    if (feeds.size() == 0) {
      hasChanceOfMoreFeeds = false;
    } else {
      this.feeds = feeds;
      hasChanceOfMoreFeeds = true;
      notifyDataSetChanged();
    }
  }

  public void appendFeeds(List<Feed> additionalFeeds) {
    isLoading = false;
    int oldSize = feeds.size();
    if (additionalFeeds.size() == 0) {
      hasChanceOfMoreFeeds = false;
      notifyItemChanged(oldSize);
    } else {
      hasChanceOfMoreFeeds = true;
      feeds.addAll(additionalFeeds);
      notifyItemInserted(oldSize);
    }
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
      if (!isLoading && mOnLoadMoreListener != null) {
        if (hasChanceOfMoreFeeds) {
          //call for more data
          isLoading = true;
          mOnLoadMoreListener.onLoadMore();
          ((LoadMoreViewHolder) holder).showLoadingView();
        } else {
          Log.d("HomFragment","hinding view");
          //hide the loading view
          ((LoadMoreViewHolder) holder).hideLoadingView();
        }
      }
    }
  }

  private void removeItemAt(int position) {
    feeds.remove(position);
    notifyItemRemoved(position);
  }

  @Override
  public int getItemViewType(int position) {
    if (position == feeds.size()) {
      return VIEW_TYPE_LOADING;
    } else {
      return VIEW_TYPE_FEED;
    }
  }

  @Override
  public int getItemCount() {
    return feeds.size() + 1;
  }

  public int getFeedsCount() {
    return feeds.size();
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
    @BindView(R.id.loading_view_root)
    RelativeLayout loadingViewRoot;
    public LoadMoreViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void showLoadingView() {
      if (loadingViewRoot != null) {
        loadingViewRoot.setVisibility(View.VISIBLE);
      }
    }

    public void hideLoadingView() {
      if (loadingViewRoot != null) {
        loadingViewRoot.setVisibility(View.GONE);
      }
    }
  }
}
