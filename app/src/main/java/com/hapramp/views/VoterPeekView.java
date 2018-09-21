package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hapramp.R;
import com.hapramp.steem.models.Voter;
import com.hapramp.utils.ImageHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoterPeekView extends FrameLayout {
  @BindView(R.id.image1)
  ImageView image1;
  @BindView(R.id.image2)
  ImageView image2;
  @BindView(R.id.image3)
  ImageView image3;
  private Context context;

  public VoterPeekView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    View view = LayoutInflater.from(context).inflate(R.layout.voter_peek_view, this);
    ButterKnife.bind(this, view);
  }

  public VoterPeekView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public VoterPeekView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setVoters(List<Voter> voters) {
    try {
      image1.setVisibility(GONE);
      image2.setVisibility(GONE);
      image3.setVisibility(GONE);
      int size = voters.size();
      for (int i = 0; i < size; i++) {
        if (i == 0) {
          image1.setVisibility(VISIBLE);
          ImageHandler.loadCircularImage(context, image1,
            String.format(context.getResources().getString(R.string.steem_user_profile_pic_format),
              voters.get(i).getVoter()));
        } else if (i == 1) {
          image2.setVisibility(VISIBLE);
          ImageHandler.loadCircularImage(context, image2,
            String.format(context.getResources().getString(R.string.steem_user_profile_pic_format),
              voters.get(i).getVoter()));
        } else if (i == 2) {
          image3.setVisibility(VISIBLE);
          ImageHandler.loadCircularImage(context, image3,
            String.format(context.getResources().getString(R.string.steem_user_profile_pic_format),
              voters.get(i).getVoter()));
        } else {
          break;
        }
      }
    }
    catch (Exception e) {
      Log.e("VoterPeekView", e.toString());
    }
  }
}
