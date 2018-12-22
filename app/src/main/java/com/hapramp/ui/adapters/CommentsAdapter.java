package com.hapramp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.CommentModel;
import com.hapramp.parser.MarkdownHandler;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.MomentsUtils;
import com.hapramp.utils.TextViewImageGetter;
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
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    if (holder instanceof CommentViewHolder) {
      ((CommentViewHolder) holder).bind(commentsList.get(position), position, new CommentsItemView.CommentActionListener() {
        @Override
        public void onCommentDeleted(int itemIndex) {
          removeItemAt(itemIndex);
        }
      });
    } else if (holder instanceof NestedCommentItemViewHolder) {
      //if hasParent
      final int p = position - (hasParent ? 1 : 0);
      ((NestedCommentItemViewHolder) holder).bind(commentsList.get(p), p, new CommentsItemView.CommentActionListener() {
        @Override
        public void onCommentDeleted(int itemIndex) {
          removeItemAt(itemIndex);
        }
      });
    } else if (holder instanceof ParentCommentItemViewHolder) {
      //if hasParent
      ((ParentCommentItemViewHolder) holder).bind(parentAuthor, parentContent, parentTimestamp);
    }
  }

  private void removeItemAt(int p) {
    commentsList.remove(p);
    notifyItemRemoved(p);
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

  public void addComments(List<CommentModel> comments) {
    resetList();
    commentsList.addAll(comments);
    if (hasParent) {
      notifyItemRangeChanged(1, comments.size());
    } else {
      notifyDataSetChanged();
    }
  }

  public void resetList() {
    commentsList.clear();
  }

  public void addSingleComment(CommentModel steemCommentModel) {
    commentsList.add(0, steemCommentModel);
    notifyItemInserted(0);
  }

  class CommentViewHolder extends RecyclerView.ViewHolder {
    public CommentViewHolder(View itemView) {
      super(itemView);
    }

    public void bind(CommentModel comment, int itemIndex, CommentsItemView.CommentActionListener commentActionListener) {
      ((CommentsItemView) itemView).setComment(comment);
      ((CommentsItemView) itemView).setItemIndex(itemIndex);
      ((CommentsItemView) itemView).setCommenttActionListener(commentActionListener);
    }
  }

  class NestedCommentItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.comment_item)
    CommentsItemView commentsItemView;

    public NestedCommentItemViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(CommentModel commentModel, int itemIndex, CommentsItemView.CommentActionListener commentActionListener) {
      commentsItemView.setComment(commentModel);
      commentsItemView.setItemIndex(itemIndex);
      commentsItemView.setCommenttActionListener(commentActionListener);
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
      setSpannedString(commentContent,body);
    }
  }

  private void setSpannedString(TextView commentContent, String commentBody){
    Spannable html;
    try {
      String htmlContent = MarkdownHandler.getHtmlFromMarkdown(commentBody);
      TextViewImageGetter imageGetter = new TextViewImageGetter(mContext, commentContent);
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        html = (Spannable) Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
      } else {
        html = (Spannable) Html.fromHtml(htmlContent, imageGetter, null);
      }
      commentContent.setText(html);
    }catch (Exception e){
      commentContent.setText(commentBody);
      e.printStackTrace();
    }
    commentContent.setMovementMethod(LinkMovementMethod.getInstance());
  }
}
