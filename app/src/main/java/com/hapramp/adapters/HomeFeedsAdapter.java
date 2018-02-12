package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.R;
import com.hapramp.models.Feed;
import com.hapramp.models.response.PostResponse;
import com.hapramp.views.post.PostItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 2/12/2018.
 */

public class HomeFeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public static final int VIEW_TYPE_FEED = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    private boolean hasMoreToLoad = false;
    private List<Feed> feeds;

    public HomeFeedsAdapter(Context mContext) {

        this.mContext = mContext;
        feeds = new ArrayList<>();

    }

    public void setFeeds(List<Feed> feeds){

        this.feeds = feeds;
        notifyDataSetChanged();

    }

    public void appendFeeds(List<Feed> additionalFeeds){

        for(int i=0;i<additionalFeeds.size();i++){
            feeds.add(additionalFeeds.get(i));
            notifyItemInserted(feeds.size()+i);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (feeds.get(position) == null) {

            return VIEW_TYPE_LOADING;

        } else {

            return VIEW_TYPE_FEED;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FeedViewHolder) {

            ((FeedViewHolder) holder).bind(feeds.get(position));

        } else if (holder instanceof LoadMoreViewHolder) {
            Log.d("Adapter","Binding Loading View");
            ((LoadMoreViewHolder) holder).startSimmer();

        }

    }

    @Override
    public int getItemCount() {

        return feeds.size();

    }

    public void setHasMoreToLoad(boolean hasMoreToLoad) {

    }

    public void clearList() {

    }

    class FeedViewHolder extends RecyclerView.ViewHolder {

        PostItemView postItemView;

        public FeedViewHolder(View itemView) {
            super(itemView);
            postItemView = (PostItemView) itemView;
        }

        public void bind(final Feed postData) {
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
            shimmerFrameLayout.startShimmerAnimation();

        }

        public void hideView() {
            shimmerFrameLayout.setVisibility(View.GONE);
        }

    }

}
