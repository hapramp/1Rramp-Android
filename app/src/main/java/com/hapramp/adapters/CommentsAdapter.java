package com.hapramp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommentModel;
import com.hapramp.models.UserResponse;
import com.hapramp.models.response.CommentCreateResponse;
import com.hapramp.models.response.CommentsResponse;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {


    private List<CommentsResponse.Results> commentsList;
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
        return commentsList != null ? commentsList.size() : 0;
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


        @BindView(R.id.commentAvatar)
        ImageView commentAvatar;
        @BindView(R.id.commentOwnerName)
        TextView commentOwnerName;
        @BindView(R.id.commentMetaHolder)
        LinearLayout commentMetaHolder;
        @BindView(R.id.commentTv)
        TextView commentTv;
        @BindView(R.id.created_time)
        TextView createdTime;

        public CommentViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(CommentsResponse.Results comment) {

            ImageHandler.loadCircularImage(mContext, commentAvatar, comment.user.image_uri);
            commentOwnerName.setText(comment.user.full_name);

            if (comment.created_at.length() != 0) {
                createdTime.setText(MomentsUtils.getFormattedTime(comment.created_at));
            } else {
                createdTime.setText("Now");
            }

            commentTv.setText(comment.content);

        }
    }

    public void addComment(CommentCreateResponse comment) {

        UserResponse user = HaprampPreferenceManager.getInstance().getUser();

        commentsList.add(0,
                new CommentsResponse.Results(
                        comment.id,
                        comment.created_at,
                        comment.content, false, 0
                        , new CommentsResponse.User(user.id, user.username, user.full_name, user.image_uri)));

        notifyItemInserted(0);

    }

    public void addComments(List<CommentsResponse.Results> comments) {

        commentsList.addAll(comments);
        notifyItemInserted(commentsList.size() - comments.size() - 1);

    }


}
