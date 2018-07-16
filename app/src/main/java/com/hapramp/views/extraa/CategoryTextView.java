package com.hapramp.views.extraa;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;

/**
 * Created by Ankit on 12/25/2017.
 */

public class CategoryTextView extends FrameLayout {

  private Context mContext;
  private View view;
  private TextView content;
  private boolean isSelected;

  public CategoryTextView(@NonNull Context context) {
    super(context);
    mContext = context;
    init();
  }

  private void init() {
    view = LayoutInflater.from(mContext).inflate(R.layout.post_category_item_view, this);
    content = view.findViewById(R.id.category_item_text);
  }

  public CategoryTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
    init();
  }

  public CategoryTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mContext = context;
    init();
  }

  public void setText(String text) {
    content.setText(text);
  }

  public String getSkill() {
    return content.getText().toString();
  }  public void setSelected(boolean selected) {

    content.setSelected(selected);
    isSelected = selected;

    if (selected) {
      content.setTextColor(Color.WHITE);
    } else {
      content.setTextColor(Color.parseColor("#8a000000"));
    }

  }

  public boolean isSelected() {
    return isSelected;
  }


}
