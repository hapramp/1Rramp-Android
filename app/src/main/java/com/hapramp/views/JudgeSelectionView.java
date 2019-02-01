package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.models.JudgeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JudgeSelectionView extends FrameLayout {
  public static final int MAX_JUDGES_ALLOWED = 3;
  @BindView(R.id.judge_container)
  LinearLayout judgeContainer;
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
    View view = LayoutInflater.from(context).inflate(R.layout.judges_selection_view, this);
    ButterKnife.bind(this, view);
    attachListener();
  }

  private void attachListener() {
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
    try {
      int len = judges.size() > MAX_JUDGES_ALLOWED ? MAX_JUDGES_ALLOWED : judges.size();
      //show add button
      addBtn.setVisibility(VISIBLE);
      //clear all views
      judgeContainer.removeAllViews();
      //add items
      for (int i = 0; i < len; i++) {
        JudgeItemView itemView = new JudgeItemView(mContext);
        itemView.setJudgeInfo(judges.get(i));
        itemView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            if (judgeSelectionCallback != null) {
              judgeSelectionCallback.onSelectJudge();
            }
          }
        });
        //add to view
        judgeContainer.addView(itemView, i);
        if (i == (MAX_JUDGES_ALLOWED - 1)) {
          addBtn.setVisibility(GONE);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(mContext, "Error while selecting judges!", Toast.LENGTH_LONG).show();
    }
  }

  public void setJudgeSelectionCallback(JudgeSelectionCallback judgeSelectionCallback) {
    this.judgeSelectionCallback = judgeSelectionCallback;
  }

  public interface JudgeSelectionCallback {
    void onSelectJudge();
  }
}
