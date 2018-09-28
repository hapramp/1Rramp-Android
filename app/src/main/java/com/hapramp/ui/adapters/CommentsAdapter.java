package com.hapramp.ui.adapters;

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
import com.hapramp.utils.MomentsUtils;
import com.hapramp.views.comments.CommentsItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  public static final int VIEW_TYPE_COMMENT = 0;
  public static final int VIEW_TYPE_NESTED_COMMENT = 1;
  public static final int VIEW_TYPE_PARENT_COMMENT = 2;

  private ArrayList<CommentModel> commentsList;
  private Context mContext;
  private boolean hasParent;
  private String parentAuthor;
  private String parentTimestamp;
  private String parentContent;

  public CommentsAdapter(Context mContext) {
    this.mContext = mContext;
    commentsList = new ArrayList<>();
  }

  public void setParentData(String author, String timestamp, String content) {
    this.hasParent = true;
    this.parentAuthor = author;
    this.parentTimestamp = timestamp;
    this.parentContent = content;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v;
    if (viewType == VIEW_TYPE_COMMENT) {
      v = new CommentsItemView(parent.getContext());
      return new CommentViewHolder(v);
    } else if (viewType == VIEW_TYPE_NESTED_COMMENT) {
      v = LayoutInflater.from(mContext).inflate(R.layout.nested_comment_item_view, parent, false);
      return new NestedCommentItemViewHolder(v);
    } else if (viewType == VIEW_TYPE_PARENT_COMMENT) {
      v = LayoutInflater.from(mContext).inflate(R.layout.parent_comment_item_view, parent, false);
      return new ParentCommentItemViewHolder(v);
    }
    return null;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof CommentViewHolder) {
      ((CommentViewHolder) holder).bind(commentsList.get(position));
    } else if (holder instanceof NestedCommentItemViewHolder) {
      //if hasParent
      ((NestedCommentItemViewHolder) holder).bind(commentsList.get(position - (hasParent ? 1 : 0)));
    } else if (holder instanceof ParentCommentItemViewHolder) {
      //if hasParent
      ((ParentCommentItemViewHolder) holder).bind(parentAuthor, parentContent, parentTimestamp);
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (hasParent) {
      if (position == 0) {
        return VIEW_TYPE_PARENT_COMMENT;
      } else {
        return VIEW_TYPE_NESTED_COMMENT;
      }
    } else {
      return VIEW_TYPE_COMMENT;
    }
  }

  @Override
  public int getItemCount() {
    return hasParent ? commentsList.size() + 1 : commentsList.size();
  }

  public void resetList() {
    commentsList.clear();
  }

  public void addComments(List<CommentModel> comments) {
    resetList();
    commentsList.addAll(comments);
    if (hasParent) {
      notifyItemRangeChanged(1,comments.size());
    } else {
      notifyDataSetChanged();
    }
  }

  public void addSingleComment(CommentModel steemCommentModel) {
    commentsList.add(0, steemCommentModel);
    notifyItemInserted(0);
  }

  class CommentViewHolder extends RecyclerView.ViewHolder {
    public CommentViewHolder(View itemView) {
      super(itemView);
    }

    public void bind(CommentModel comment) {
      ((CommentsItemView) itemView).setComment(comment);
    }
  }

  class NestedCommentItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.comment_item)
    CommentsItemView commentsItemView;

    public NestedCommentItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(CommentModel commentModel) {
      commentsItemView.setComment(commentModel);
    }
  }

  class ParentCommentItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.comment_owner_pic)
    ImageView commentOwnerPic;
    @BindView(R.id.comment_owner_username)
    TextView commentOwnerUsername;
    @BindView(R.id.timestamp)
    TextView timestampTv;
    @BindView(R.id.comment_content)
    TextView commentContent;

    public ParentCommentItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(String author, String body, String timestamp) {
      ImageHandler.loadCircularImage(mContext
        , commentOwnerPic,
        String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
          author));
      timestampTv.setText(MomentsUtils.getFormattedTime(timestamp));
      commentOwnerUsername.setText(author);
      commentContent.setText(body);
    }
  }
}
