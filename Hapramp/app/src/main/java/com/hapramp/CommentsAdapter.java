package com.hapramp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hapramp.models.response.CommentsResponse;

import java.util.List;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentsAdapter extends ArrayAdapter<CommentsResponse.Comments> {

    private List<CommentsResponse.Comments> commentsList;
    private Context mContext;
    private CommentViewHolder commentViewHolder;

    public void setCommentsList(List<CommentsResponse.Comments> commentsList) {
        this.commentsList = commentsList;
        notifyDataSetChanged();
    }

    public CommentsAdapter(@NonNull Context context) {
        super(context,0);
        this.mContext =  context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_view,null);
            commentViewHolder = new CommentViewHolder();
            commentViewHolder.content = (TextView) convertView.findViewById(R.id.content);
            commentViewHolder.time = (TextView) convertView.findViewById(R.id.comment_time);
            convertView.setTag(commentViewHolder);
        }else{
            commentViewHolder = (CommentViewHolder) convertView.getTag();
        }

        bindData(position,commentViewHolder);

        return convertView;
    }

    private void bindData(int position, CommentViewHolder commentViewHolder) {
        commentViewHolder.time.setText(commentsList.get(position).created_at);
        commentViewHolder.content.setText(commentsList.get(position).content);
    }

    class CommentViewHolder {
        TextView content;
        TextView time;
    }

    @Override
    public int getCount() {
        return commentsList!=null?commentsList.size():0;
    }
}
