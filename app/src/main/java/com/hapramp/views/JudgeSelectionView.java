package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.JudgeModel;
import com.hapramp.utils.ImageHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JudgeSelectionView extends FrameLayout {
  @BindView(R.id.judge1_image)
  ImageView judge1Image;
  @BindView(R.id.judge1_name)
  TextView judge1Name;
  @BindView(R.id.judge1)
  RelativeLayout judge1;
  @BindView(R.id.judge2_image)
  ImageView judge2Image;
  @BindView(R.id.judge2_name)
  TextView judge2Name;
  @BindView(R.id.judge2)
  RelativeLayout judge2;
  ArrayList<Integer> judeges;
  @BindView(R.id.addBtn)
  ImageView addBtn;
  private Context mContext;
  private JudgeSelectionCallback judgeSelectionCallback;

  public JudgeSelectionView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    judeges = new ArrayList<>();
    View view = LayoutInflater.from(context).inflate(R.layout.judges_selection_view, this);
    ButterKnife.bind(this, view);
    attachListener();
  }

  private void attachListener() {
    judge1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (judgeSelectionCallback != null) {
          judgeSelectionCallback.onSelectJudge();
        }
      }
    });
    judge2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (judgeSelectionCallback != null) {
          judgeSelectionCallback.onSelectJudge();
        }
      }
    });
    addBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (judgeSelectionCallback != null) {
          judgeSelectionCallback.onSelectJudge();
        }
      }
    });
  }

  public JudgeSelectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public void setJudgesList(List<JudgeModel> judges) {
    int len = judges.size() > 2 ? 2 : judges.size();
    judge1.setVisibility(GONE);
    judge2.setVisibility(GONE);
    for (int i = 0; i < len; i++) {
      if (i == 0) {
        judge1.setVisibility(VISIBLE);
        ImageHandler.loadCircularImage(mContext, judge1Image,
          String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
            judges.get(i).getmUsername()));
        judge1Name.setText(judges.get(i).getmUsername());
        addBtn.setVisibility(VISIBLE);
      } else if (i == 1) {
        judge2.setVisibility(VISIBLE);
        ImageHandler.loadCircularImage(mContext, judge2Image,
          String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
            judges.get(i).getmUsername()));
        judge2Name.setText(judges.get(i).getmUsername());
        //remove plus button
        addBtn.setVisibility(GONE);
      }
    }
  }

  public void setJudgeSelectionCallback(JudgeSelectionCallback judgeSelectionCallback) {
    this.judgeSelectionCallback = judgeSelectionCallback;
  }

  public interface JudgeSelectionCallback {
    void onSelectJudge();
  }
}
