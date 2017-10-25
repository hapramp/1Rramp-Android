package com.hapramp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.models.response.PostResponse;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Ankit on 10/25/2017.
 */

public class PostsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context mContext;
    public List<PostResponse> postResponses;

    public PostsRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setPostResponses(List<PostResponse> postResponses) {
        this.postResponses = postResponses;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_view, null);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.feed_owner_pic)
        SimpleDraweeView feedOwnerPic;
        @BindView(R.id.feed_owner_title)
        TextView feedOwnerTitle;
        @BindView(R.id.feed_owner_subtitle)
        TextView feedOwnerSubtitle;
        @BindView(R.id.post_header_container)
        RelativeLayout postHeaderContainer;
        @BindView(R.id.featured_image_post)
        SimpleDraweeView featuredImagePost;
        @BindView(R.id.post_title)
        TextView postTitle;
        @BindView(R.id.tags)
        TextView tags;
        @BindView(R.id.post_snippet)
        TextView postSnippet;
        @BindView(R.id.faded_edge)
        FrameLayout fadedEdge;
        @BindView(R.id.likeBtn)
        TextView likeBtn;
        @BindView(R.id.likeCount)
        TextView likeCount;
        @BindView(R.id.commentBtn)
        TextView commentBtn;
        @BindView(R.id.commentCount)
        TextView commentCount;
        @BindView(R.id.hapcoinBtn)
        TextView hapcoinBtn;
        @BindView(R.id.hapcoins_count)
        TextView hapcoinsCount;
        @BindView(R.id.shareBtn)
        TextView shareBtn;
        @BindView(R.id.share_count)
        TextView shareCount;
        @BindView(R.id.post_meta_container)
        RelativeLayout postMetaContainer;

        public PostViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(PostResponse postResponse){
            // TODO: 10/25/2017 bind data
        }
    }

    interface OnPostElementsClickListener{
        void onReadMoreTapped();
    }
}
