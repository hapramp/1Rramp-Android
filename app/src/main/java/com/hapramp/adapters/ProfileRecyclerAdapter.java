package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.R;
import com.hapramp.models.Feed;
import com.hapramp.models.ProfileHeaderModel;
import com.hapramp.models.response.PostResponse;
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
    private final int VIEW_TYPE_SHIMMER = 3;
    private final int VIEW_TYPE_PROFILE_HEADER = 1;

    public Context mContext;
    private boolean hasMoreToLoad = true;
    public ProfileHeaderModel profileHeaderModel;
    private int s;
    private List<Feed> feeds;

    public ProfileRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        feeds = new ArrayList<>();
    }

    public void setProfileHeaderModel(ProfileHeaderModel profileHeaderModel) {
        this.profileHeaderModel = profileHeaderModel;
        notifyDataSetChanged();
    }

    public void appendResult(List<Feed> newPosts) {
        feeds.addAll(newPosts);
        notifyItemInserted(feeds.size() - (newPosts.size() - 1));

    }

    public void setHasMoreToLoad(boolean hasMoreToLoad) {
        this.hasMoreToLoad = hasMoreToLoad;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPE_PROFILE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == VIEW_TYPE_ITEM) {

            View view = new PostItemView(mContext);
            viewHolder = new PostViewHolder(view);

        } else if (viewType == VIEW_TYPE_PROFILE_HEADER) {

            View view = new ProfileHeaderView(mContext);
            viewHolder = new ProfileHeaderViewHolder(view);

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {

        if (viewHolder instanceof PostViewHolder) {

            // Log.d("Adapter", "Binding Post at " + pos);
            ((PostViewHolder) viewHolder).bind(feeds.get(pos - 1));

        } else if (viewHolder instanceof ProfileHeaderViewHolder) {

            ((ProfileHeaderViewHolder) viewHolder).bind(profileHeaderModel);

        }
    }

    @Override
    public int getItemCount() {

        // since we have additional item at the top + one at the bottom
        s = feeds.size();

        return s == 0 ? 0 : s + 1;

    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        PostItemView postItemView;

        public PostViewHolder(View itemView) {
            super(itemView);
            postItemView = (PostItemView) itemView;
        }

        public void bind(final Feed postData) {
            postItemView.setPostData(postData);
        }

    }

    class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {

        ProfileHeaderView profileHeaderView;

        public ProfileHeaderViewHolder(View itemView) {
            super(itemView);
            profileHeaderView = (ProfileHeaderView) itemView;
        }


        public void bind(ProfileHeaderModel profileHeaderData) {

            profileHeaderView.setProfileHeaderData(profileHeaderData);

        }
    }

}
