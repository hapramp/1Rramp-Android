package com.hapramp.views.comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.views.extraa.StarView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsItemView extends FrameLayout {
  @BindView(R.id.comment_owner_pic)
  ImageView commentOwnerPic;
  @BindView(R.id.comment_owner_username)
  TextView commentOwnerUsername;
  @BindView(R.id.dot_separator)
  TextView dotSeparator;
  @BindView(R.id.timestamp)
  TextView timestamp;
  @BindView(R.id.comment_content)
  TextView commentContent;
  @BindView(R.id.commentBtn)
  TextView commentBtn;
  @BindView(R.id.commentCount)
  TextView commentCount;
  @BindView(R.id.payoutBtn)
  TextView payoutBtn;
  @BindView(R.id.payoutValue)
  TextView payoutValue;
  @BindView(R.id.starView)
  StarView starView;

  public CommentsItemView(@NonNull Context context) {
    super(context);
  }

  public CommentsItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  private void init(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.comment_item_view, this);
    ButterKnife.bind(this, view);
  }
}
