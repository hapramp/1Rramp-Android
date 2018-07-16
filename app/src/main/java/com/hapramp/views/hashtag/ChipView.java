package com.hapramp.views.hashtag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/23/2018.
 */

public class ChipView extends RelativeLayout {

  @BindView(R.id.tag_text)
  TextView tagText;
  @BindView(R.id.remove_btn)
  TextView removeBtn;
  private Context context;
  private int mIndex;
  private View view;
  private RemoveTagListener removeTagListener;

  public ChipView(Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {

    this.context = context;
    view = LayoutInflater.from(context).inflate(R.layout.chip_layout, this);
    ButterKnife.bind(this, view);
    removeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

    removeBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (removeTagListener != null) {
          removeTagListener.onRemove(view, tagText.getText().toString());
        }
      }
    });
  }

  public ChipView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setTagText(String text, int index) {
    tagText.setText(text);
    this.mIndex = index;
  }

  public void setRemoveTagListener(RemoveTagListener removeTagListener) {
    this.removeTagListener = removeTagListener;
  }

  public interface RemoveTagListener {
    void onRemove(View view, String tag);
  }

}
