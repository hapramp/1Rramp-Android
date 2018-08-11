package xute.markdownrenderer.types;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import xute.markdownrenderer.R;

public class HeadingFiveTypeView extends FrameLayout {

  private TextView content;

  public HeadingFiveTypeView(@NonNull Context context) {
    super(context);
    init(context);
  }

  public HeadingFiveTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public HeadingFiveTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    View v = LayoutInflater.from(context).inflate(R.layout.heading_five_type_view, this);
    content = v.findViewById(R.id.content);
  }

  public void setText(String text) {
    content.setText(text);
  }

}
