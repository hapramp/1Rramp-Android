package com.hapramp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hapramp.models.response.CompetitionResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CompetitionPostsRecyclerAdapter extends RecyclerView.Adapter<CompetitionPostsRecyclerAdapter.CompetitionPostViewHolder> {

    private List<CompetitionsPostReponse.Posts> competitionsPostReponses;
    private Context mContext;

    public CompetitionPostsRecyclerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setCompetitionsPostReponses(List<CompetitionsPostReponse.Posts> competitionsPostReponses) {

        this.competitionsPostReponses = competitionsPostReponses;
        notifyDataSetChanged();

    }

    @Override
    public CompetitionPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.competition_post_item_view, null);
        return new CompetitionPostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CompetitionPostViewHolder holder, int position) {
        holder.bind(competitionsPostReponses.get(position));
    }

    @Override
    public int getItemCount() {
        return competitionsPostReponses != null ? competitionsPostReponses.size() : 0;
    }

    class CompetitionPostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.feed_owner_pic)
        SimpleDraweeView feedOwnerPic;
        @BindView(R.id.feed_owner_title)
        TextView feedOwnerTitle;
        @BindView(R.id.feed_owner_subtitle)
        TextView feedOwnerSubtitle;
        @BindView(R.id.clubsContainer)
        ClubTagView clubsContainer;
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

        public CompetitionPostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final CompetitionsPostReponse.Posts post) {

            feedOwnerPic.setImageURI(post.user.image_uri);
            feedOwnerTitle.setText(post.user.full_name);
            feedOwnerSubtitle.setText(post.user.username);
            featuredImagePost.setImageURI(post.media_uri);
            postSnippet.setText(post.content);
            likeCount.setText(String.valueOf(post.vote_count));
            commentCount.setText(String.valueOf(post.comment_count));
            hapcoinsCount.setText("H");
            clubsContainer.setClubs(SkillsConverter.getSkillCharacter(post.skills.get(0).id),"","");

        }

    }

}
