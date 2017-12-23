package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommentModel;
import com.hapramp.utils.ImageHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<CommentModel> commentsList;
    private Context mContext;

    public CommentsAdapter(Context mContext) {
        this.mContext = mContext;
        commentsList = new ArrayList<>();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.comment_view, null);
        return new CommentViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return commentsList!=null?commentsList.size():0;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
            holder.bind(commentsList.get(position));
    }

    public void resetList() {
        commentsList.clear();
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.comment_user_dp)
        ImageView commentUserDp;
        @BindView(R.id.comment_user_name)
        TextView commentUserName;
        @BindView(R.id.comment_time)
        TextView commentTime;
        @BindView(R.id.content)
        TextView content;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(CommentModel comment){

            ImageHandler.loadCircularImage(mContext,commentUserDp,comment.getUserDpUrl());
            commentUserName.setText(comment.getUserName());
            commentTime.setText(comment.getCommentTime());
            content.setText(comment.getComment());

        }
    }

    public void addComment(CommentModel comment) {
        commentsList.add(comment);
        notifyDataSetChanged();
    }


}
