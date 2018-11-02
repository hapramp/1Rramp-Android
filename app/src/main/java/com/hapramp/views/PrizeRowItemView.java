package com.hapramp.views;

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

public class PrizeRowItemView extends FrameLayout {
  @BindView(R.id.prize_header)
  TextView prizeHeader;
  @BindView(R.id.prize_value)
  TextView prizeValue;
  private Context mContext;

  public PrizeRowItemView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.prize_item_row, this);
    ButterKnife.bind(this, view);
  }

  public PrizeRowItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PrizeRowItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setPrizeData(String header, String value) {
    prizeHeader.setText(header);
    prizeValue.setText(value);
  }

}
