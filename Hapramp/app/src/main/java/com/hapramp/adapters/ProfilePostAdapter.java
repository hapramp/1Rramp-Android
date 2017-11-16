package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.R;
import com.hapramp.models.response.PostResponse;
import com.hapramp.utils.FontManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 10/29/2017.
 */

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.PostViewHolder>{

        public Context mContext;
        public List<PostResponse> postResponses;

        public ProfilePostAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public void setPostResponses(List<PostResponse> postResponses) {
            this.postResponses = postResponses;
            notifyDataSetChanged();
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_view, null);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PostViewHolder viewHolder, int i) {
            viewHolder.bind(postResponses.get(i));
        }

        @Override
        public int getItemCount() {
            return postResponses!=null?postResponses.size():0;
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
            @BindView(R.id.post_meta_container)
            RelativeLayout postMetaContainer;
            @BindView(R.id.starBtn)
            TextView starBtn;

            public PostViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                starBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
                likeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
                hapcoinBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
                commentBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
            }

            public void bind(PostResponse postResponse) {
                feedOwnerPic.setImageURI(postResponse.getUser().getImage_uri());
                feedOwnerTitle.setText(postResponse.getUser().getFull_name());
                feedOwnerSubtitle.setText(postResponse.getUser().getUsername());
                featuredImagePost.setImageURI(postResponse.getMedia_uri());
                postTitle.setText("Missing Title");
                postSnippet.setText(postResponse.getContent());

            }
        }

}
