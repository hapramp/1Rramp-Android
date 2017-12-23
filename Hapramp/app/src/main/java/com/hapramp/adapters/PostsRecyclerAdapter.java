package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.VoteDeleteCallback;
import com.hapramp.interfaces.VotePostCallback;
import com.hapramp.models.requests.VoteRequestBody;
import com.hapramp.models.response.PostResponse;
import com.hapramp.utils.Constants;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.views.ClubTagView;
import com.hapramp.views.StarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by Ankit on 10/25/2017.
 */

public class PostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VotePostCallback, VoteDeleteCallback {

    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_SHIMMER = 0;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    public Context mContext;
    public List<PostResponse.Results> postResponses;
    public postListener postElementsClickListener;

    public PostsRecyclerAdapter(Context mContext, RecyclerView recyclerView) {
        this.mContext = mContext;
        postResponses = new ArrayList<>();
        addScrollListener(recyclerView);
    }

    private void addScrollListener(RecyclerView recyclerView) {

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    // Log.d("Adapter"," loading "+loading+" totalItemCount "+totalItemCount+" lastV "+lastVisibleItem+" thres "+visibleThreshold);
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        postElementsClickListener.onLoadMore();
                        //   Log.d("Adapter","load More req");
                        loading = true;

                    }

                }
            });
        }

    }

    public void appendResult(List<PostResponse.Results> newPosts) {
        postResponses.addAll(newPosts);
        notifyItemInserted(postResponses.size() - newPosts.size());
        setLoaded();
    }

    private void setLoaded() {
        loading = false;
    }


    public void setListener(postListener postElementsClickListener) {
        this.postElementsClickListener = postElementsClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return postResponses.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_SHIMMER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_view, null);
            viewHolder = new PostViewHolder(view);

        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.load_more_progress_view, null);
            viewHolder = new LoadMoreViewHolder(view);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {

        if (viewHolder instanceof LoadMoreViewHolder) {
            Log.d("Adapter", "binding shimmer");
            ((LoadMoreViewHolder) viewHolder).startSimmer();
        } else {

            Log.d("Adapter", "binding post");
            ((PostViewHolder) viewHolder).bind(postResponses.get(pos), postElementsClickListener, this);
        }

    }

    @Override
    public int getItemCount() {
        return postResponses.size();
    }

    public void removeItem(int pos) {
        postResponses.remove(pos);
        notifyItemRemoved(pos);
    }

    private void deleteVote(int postId) {
        DataServer.deleteVote(postId, this);
    }

    private void vote(int postId, int vote) {
        DataServer.votePost(String.valueOf(postId), new VoteRequestBody((int) vote), this);
    }

    @Override
    public void onPostVoted(PostResponse.Results updatedPost) {

        updatePost(updatedPost);
    }

    private void updatePost(PostResponse.Results updatedPost) {

        for (int i = 0; i < postResponses.size(); i++) {
            if (postResponses.get(i).id == updatedPost.id) {
                postResponses.set(i, updatedPost);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onPostVoteError() {

    }

    @Override
    public void onVoteDeleted(PostResponse.Results updatedPost) {
        updatePost(updatedPost);
    }

    @Override
    public void onVoteDeleteError() {

    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.feed_owner_pic)
        ImageView feedOwnerPic;
        @BindView(R.id.feed_owner_title)
        TextView feedOwnerTitle;
        @BindView(R.id.starView)
        StarView starView;
        @BindView(R.id.feed_owner_subtitle)
        TextView feedOwnerSubtitle;
        @BindView(R.id.post_header_container)
        RelativeLayout postHeaderContainer;
        @BindView(R.id.featured_image_post)
        ImageView featuredImagePost;
        @BindView(R.id.post_title)
        TextView postTitle;
        @BindView(R.id.tags)
        TextView tags;
        @BindView(R.id.post_snippet)
        TextView postSnippet;
        @BindView(R.id.commentBtn)
        TextView commentBtn;
        @BindView(R.id.commentCount)
        TextView commentCount;
        @BindView(R.id.hapcoinBtn)
        TextView hapcoinBtn;
        @BindView(R.id.readMoreBtn)
        TextView readMoreBtn;
        @BindView(R.id.hapcoins_count)
        TextView hapcoinsCount;
        @BindView(R.id.post_meta_container)
        RelativeLayout postMetaContainer;
        @BindView(R.id.clubsContainer)
        ClubTagView clubTagView;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            hapcoinBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
            commentBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        }

        public void bind(final PostResponse.Results post,
                         final postListener postListener,
                         final PostsRecyclerAdapter postsAdapter) {


            // set basic meta-info
            ImageHandler.loadCircularImage(mContext, feedOwnerPic, post.user.image_uri);
            feedOwnerTitle.setText(post.user.full_name);
            feedOwnerSubtitle.setText(post.user.username);

            // classify the type of content
            if (post.post_type == Constants.CONTENT_TYPE_ARTICLE) {

                // set Title
                postTitle.setVisibility(View.VISIBLE);
                //todo post title required
                //postTitle.setText(post);

                readMoreBtn.setVisibility(View.VISIBLE);

                readMoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (postListener != null)
                            postListener.onReadMoreTapped(post);
                    }
                });


            } else {
                //(post.post_type==Constants.CONTENT_TYPE_POST)
                // turn off post title
                postTitle.setVisibility(GONE);
                readMoreBtn.setVisibility(GONE);

            }

            // check for image
            if (post.media_uri.length() == 0) {
                featuredImagePost.setVisibility(GONE);
            } else {
                ImageHandler.load(mContext, featuredImagePost, post.media_uri);
                featuredImagePost.setVisibility(View.VISIBLE);
            }

            hapcoinsCount.setText(String.format("%1.3f",post.hapcoins));
            String _comment_info = post.comment_count > 1 ? String.valueOf(post.comment_count).concat(" comments") : String.valueOf(post.comment_count).concat(" comment");
            commentCount.setText(_comment_info);
            postSnippet.setText(post.content);

            // initialize the starview
            starView.setVoteState(
                    new StarView.Vote(
                            post.is_voted,
                            post.id,
                            post.current_vote,
                            post.vote_count,
                            post.vote_sum))
                    .setOnVoteUpdateCallback(new StarView.onVoteUpdateCallback() {
                        @Override
                        public void onVoted(int postId, int vote) {
                            postsAdapter.vote(postId,vote);
                        }

                        @Override
                        public void onVoteDeleted(int postId) {
                            postsAdapter.deleteVote(postId);
                        }
                    });

            // print(post.skills,post.getUser().getUsername());
            clubTagView.setPostSkills(post.skills);

            postHeaderContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postListener != null) {
                        postListener.onUserInfoTapped(post.user.id);
                    }
                }
            });

            starView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    starView.onStarIndicatorTapped();
                }
            });

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

    public interface onPostUpdateListener {
        void onPostUpdated(int pos, PostResponse.Results updatePost);
    }

    public interface postListener {

        void onReadMoreTapped(PostResponse.Results postResponse);

        void onUserInfoTapped(int userId);

        void onLoadMore();

        void onOverflowIconTapped(View view, int postId, int position);

    }


}
