package com.hapramp.views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.models.JudgeModel;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;

public class JudgeProfileDialog extends Dialog {
  private final AppCompatActivity mActivity;
  @BindView(R.id.judge_image)
  ImageView judgeImage;
  @BindView(R.id.judge_name)
  TextView judgeName;
  @BindView(R.id.community_stripe_view)
  CommunityStripView communityStripeView;
  @BindView(R.id.bio)
  TextView bio;
  private JudgeModel judge;

  public JudgeProfileDialog(@NonNull AppCompatActivity appCompatActivity) {
    super(appCompatActivity);
    this.mActivity = appCompatActivity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.judge_profile_dialog_view);
    judgeImage = findViewById(R.id.judge_image);
    judgeName = findViewById(R.id.judge_name);
    bio = findViewById(R.id.bio);
    communityStripeView = findViewById(R.id.community_stripe_view);
    setCancelable(true);
    bindData();
  }

  private void bindData() {
    ImageHandler.loadCircularImage(mActivity, judgeImage,
      String.format(mActivity.getResources().getString(R.string.steem_user_profile_pic_format),
        judge.getmUsername()));
    judgeName.setText(judge.getmFullName());
    bio.setText(judge.getmBio());
  }

  public void setJudgeInfo(JudgeModel judgeInfo) {
    this.judge = judgeInfo;
  }
}
