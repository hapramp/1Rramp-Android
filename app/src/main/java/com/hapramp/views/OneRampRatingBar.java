package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneRampRatingBar extends FrameLayout {
  @BindView(R.id.rate1)
  ImageView rate1;
  @BindView(R.id.rate2)
  ImageView rate2;
  @BindView(R.id.rate3)
  ImageView rate3;
  @BindView(R.id.rate4)
  ImageView rate4;
  @BindView(R.id.rate5)
  ImageView rate5;
  private OnRatingChangeListener ratingChangeListener;
  private Context mContext;

  public OneRampRatingBar(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.oneramp_rating_bar, this);
    ButterKnife.bind(this, view);
    attachListener();
  }

  private void attachListener() {
    rate1.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        setRate(1, true);
      }
    });
    rate2.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        setRate(2, true);
      }
    });
    rate3.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        setRate(3, true);
      }
    });
    rate4.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        setRate(4, true);
      }
    });
    rate5.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        setRate(5, true);
      }
    });
  }

  private void setRate(int rate, boolean fromUser) {
    if (fromUser) {
      if (ratingChangeListener != null) {
        ratingChangeListener.onRatingChanged(rate);
      }
    }
    updateStars(rate);
  }

  private void updateStars(int rate) {
    //clear all stars
    rate1.setImageResource(R.drawable.star);
    rate2.setImageResource(R.drawable.star);
    rate3.setImageResource(R.drawable.star);
    rate4.setImageResource(R.drawable.star);
    rate5.setImageResource(R.drawable.star);

    //fill stars
    if (rate >= 1) {
      rate1.setImageResource(R.drawable.star_filled);
      if (rate > 1) {
        rate2.setImageResource(R.drawable.star_filled);
        if (rate > 2) {
          rate3.setImageResource(R.drawable.star_filled);
          if (rate > 3) {
            rate4.setImageResource(R.drawable.star_filled);
            if (rate > 4) {
              rate5.setImageResource(R.drawable.star_filled);
            }
          }
        }
      }
    }
  }

  public OneRampRatingBar(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public OneRampRatingBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setRatingChangeListener(OnRatingChangeListener ratingChangeListener) {
    this.ratingChangeListener = ratingChangeListener;
  }

  public void setRating(int rating) {
    setRate(rating, false);
  }

  public interface OnRatingChangeListener {
    void onRatingChanged(int rating);
  }
}
