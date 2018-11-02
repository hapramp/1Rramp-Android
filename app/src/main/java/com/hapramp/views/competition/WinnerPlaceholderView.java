package com.hapramp.views.competition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinnerPlaceholderView extends FrameLayout {
  @BindView(R.id.message)
  TextView message;
  private Context context;
  private WinnerPlaceholderCallback winnerPlaceholderCallback;
  private int mRank;

  public WinnerPlaceholderView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.winner_placeholder, this);
    ButterKnife.bind(this, view);
  }

  public WinnerPlaceholderView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public WinnerPlaceholderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setPlaceholderRank(int rank) {
    String msg = "";
    mRank = rank;
    if (rank == 1) {
      msg = "Assign 1st rank";
    } else if (rank == 2) {
      msg = "Assign 2nd rank";
    } else if (rank == 3) {
      msg = "Assign 3rd rank";
    } else {
      msg = "Assign " + rank + "th rank";
    }
    message.setText(msg);
    message.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (winnerPlaceholderCallback != null) {
          winnerPlaceholderCallback.onPlaceholderClicked(mRank);
        }
      }
    });
  }

  public void setWinnerPlaceholderCallback(WinnerPlaceholderCallback winnerPlaceholderCallback) {
    this.winnerPlaceholderCallback = winnerPlaceholderCallback;
  }

  public interface WinnerPlaceholderCallback {
    void onPlaceholderClicked(int rank);
  }
}
