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
import com.hapramp.models.ProfileHeaderModel;
import com.hapramp.models.response.PostResponse;
import com.hapramp.views.PostItemView;
import com.hapramp.views.ProfileHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 10/25/2017.
 */

public class PostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_BLANK_TOP = 0;
    private final int VIEW_TYPE_ITEM = 2;
    private final int VIEW_TYPE_SHIMMER = 3;
    private final int VIEW_TYPE_PROFILE_HEADER = 1;

    public Context mContext;
    public List<PostResponse.Results> postResponses;
    public ProfileHeaderModel profileHeaderModel;

    public PostsRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        postResponses = new ArrayList<>();
    }

    public void setProfileHeaderModel(ProfileHeaderModel profileHeaderModel) {
        this.profileHeaderModel = profileHeaderModel;
        notifyDataSetChanged();
    }

    public boolean itIsForProfile() {
        return profileHeaderModel != null;
    }

    public void appendResult(List<PostResponse.Results> newPosts) {
        postResponses.addAll(newPosts);
       notifyItemInserted(postResponses.size() - newPosts.size());

    }

    @Override
    public int getItemViewType(int position) {

        if (itIsForProfile()) {
            if (position == 0) {
                return VIEW_TYPE_PROFILE_HEADER;
            }
        } else {
            // for non-profile we have blank views at top
            if (position == 0) {
                return VIEW_TYPE_BLANK_TOP;
            }
        }

        return (position > postResponses.size()) ? VIEW_TYPE_SHIMMER : VIEW_TYPE_ITEM;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == VIEW_TYPE_ITEM) {

            View view = new PostItemView(mContext);
            viewHolder = new PostViewHolder(view);

        } else if (viewType == VIEW_TYPE_SHIMMER) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.load_more_progress_view, null);
            viewHolder = new LoadMoreViewHolder(view);

        } else if (viewType == VIEW_TYPE_PROFILE_HEADER) {

            View view = new ProfileHeaderView(mContext);
            viewHolder = new ProfileHeaderViewHolder(view);

        } else if (viewType == VIEW_TYPE_BLANK_TOP) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.blank_view, null);
            viewHolder = new BlankTopViewHolder(view);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {

        Log.d("Adapter","Binding at "+pos);
        if (viewHolder instanceof LoadMoreViewHolder) {

            ((LoadMoreViewHolder) viewHolder).startSimmer();

        } else if (viewHolder instanceof PostViewHolder) {

            ((PostViewHolder) viewHolder).bind(postResponses.get(pos-1));

        } else if (viewHolder instanceof ProfileHeaderViewHolder) {

            ((ProfileHeaderViewHolder) viewHolder).bind(profileHeaderModel);

        } else if (viewHolder instanceof BlankTopViewHolder) {
            // do
            ((BlankTopViewHolder) viewHolder).blank.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {

        // since we have additional item at the top + one at the bottom
        return postResponses.size()+2;

    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        PostItemView postItemView;

        public PostViewHolder(View itemView) {
            super(itemView);
            postItemView = (PostItemView) itemView;
        }

        public void bind(final PostResponse.Results postData) {
            postItemView.setPostData(postData);
        }

    }

    class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        ShimmerFrameLayout shimmerFrameLayout;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);

            shimmerFrameLayout = (ShimmerFrameLayout) itemView.findViewById(R.id.shimmer_view_container);

        }

        public void startSimmer() {

            shimmerFrameLayout.startShimmerAnimation();

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

    class BlankTopViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.blank)
        FrameLayout blank;
        public BlankTopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
