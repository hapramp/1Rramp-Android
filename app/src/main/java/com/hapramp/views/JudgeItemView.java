package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.JudgeModel;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JudgeItemView extends FrameLayout {
  @BindView(R.id.judge_image)
  ImageView judgeImage;
  @BindView(R.id.judge_name)
  TextView judgeName;
  private Context mContext;

  public JudgeItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(mContext).inflate(R.layout.judge_item_view_type2, this);
    ButterKnife.bind(this, view);
  }

  public JudgeItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public JudgeItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setJudgeInfo(final JudgeModel judgeInfo) {
    ImageHandler.loadCircularImage(mContext, judgeImage,
      String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
        judgeInfo.getmUsername()));

    judgeName.setText(judgeInfo.getmFullName());
  }
}
