package com.hapramp.views.competition;

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
import com.hapramp.models.CompetitionWinnerModel;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinnerItemView extends FrameLayout {
  @BindView(R.id.competition_rank_text)
  TextView competitionRankText;
  @BindView(R.id.competition_image)
  ImageView competitionImage;
  @BindView(R.id.competition_title)
  TextView competitionTitle;
  @BindView(R.id.byLabel)
  TextView byLabel;
  @BindView(R.id.winner_username)
  TextView winnerUsername;
  @BindView(R.id.competition_winner_remove)
  ImageView competitionWinnerRemove;
  private Context context;
  private CompetitionWinnerModel data;
  private WinnerItemViewCallback winnerItemViewCallback;

  public WinnerItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    View view = LayoutInflater.from(context).inflate(R.layout.winner_item_view, this);
    ButterKnife.bind(this, view);
  }

  public WinnerItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public WinnerItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setWinnerData(CompetitionWinnerModel winnerData) {
    this.data = winnerData;
    competitionRankText.setText(String.valueOf(data.getRank()));
    ImageHandler.loadUnOverridden(context, competitionImage, data.getImageUrl());
    competitionTitle.setText(data.getTitle());
    winnerUsername.setText(data.getUsername());
    competitionWinnerRemove.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (winnerItemViewCallback != null) {
          winnerItemViewCallback.onItemRemoveClicked(data.getRank());
        }
      }
    });
  }

  public void setWinnerItemViewCallback(WinnerItemViewCallback winnerItemViewCallback) {
    this.winnerItemViewCallback = winnerItemViewCallback;
  }

  public interface WinnerItemViewCallback {
    void onItemRemoveClicked(int rank);
  }
}
