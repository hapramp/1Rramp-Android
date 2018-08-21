package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.steem.SteemCommentModel;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

  private ArrayList<SteemCommentModel> commentsList;
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
  public void onBindViewHolder(CommentViewHolder holder, int position) {
    holder.bind(commentsList.get(position));
  }

  @Override
  public int getItemCount() {
    return commentsList != null ? commentsList.size() : 0;
  }

  public void resetList() {
    commentsList.clear();
    notifyDataSetChanged();
  }

  public void addComments(ArrayList<SteemCommentModel> comments) {
    commentsList.addAll(comments);
    notifyDataSetChanged();
  }

  public void addSingleComment(SteemCommentModel steemCommentModel) {
    commentsList.add(0, steemCommentModel);
    notifyItemInserted(0);
  }

  class CommentViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.popupMenuDots)
    TextView popupMenuDots;
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
      popupMenuDots.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    }

    public void bind(SteemCommentModel comment) {
      ImageHandler.loadCircularImage(mContext, commentAvatar, String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format), comment.commentAuthor));
      commentOwnerName.setText(comment.getCommentAuthor());
      if (comment.getCreatedAt().length() > 0) {
        createdTime.setText(MomentsUtils.getFormattedTime(comment.getCreatedAt()));
      } else {
        createdTime.setText("Now");
      }
      commentTv.setText(comment.getComment());
    }
  }
}
