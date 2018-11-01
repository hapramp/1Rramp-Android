package com.hapramp.views;

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
import com.hapramp.models.JudgeModel;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JudgeRemovableItemView extends FrameLayout {
  @BindView(R.id.judge_image)
  ImageView judgeImage;
  @BindView(R.id.remove_btn)
  ImageView removeBtn;
  @BindView(R.id.judge_name)
  TextView judgeName;
  private Context context;
  private JudgeModel mJudge;
  private JudgeRemoveListener mJudgeRemoveListener;
  private int mViewIndex;

  public JudgeRemovableItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    View view = LayoutInflater.from(context).inflate(R.layout.judge_item_view_type3, this);
    ButterKnife.bind(this, view);
    attachListeners();
  }

  private void attachListeners() {
    removeBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mJudgeRemoveListener != null) {
          mJudgeRemoveListener.onRemoveJudge(mJudge);
        }
      }
    });
  }

  public JudgeRemovableItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public JudgeRemovableItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setJudge(JudgeModel judge, int index) {
    this.mJudge = judge;
    this.mViewIndex = index;
    ImageHandler.loadCircularImage(context, judgeImage,
      String.format(context.getResources().getString(R.string.steem_user_profile_pic_format),
        judge.getmUsername()));
    judgeName.setText(judge.getmFullName());
  }

  public void setmJudgeRemoveListener(JudgeRemoveListener mJudgeRemoveListener) {
    this.mJudgeRemoveListener = mJudgeRemoveListener;
  }

  public interface JudgeRemoveListener {
    void onRemoveJudge(JudgeModel judgeModel);
  }
}
