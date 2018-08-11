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

public class BlockQuoteView extends FrameLayout{

  private TextView content;

  public BlockQuoteView(@NonNull Context context) {
    super(context);
    init(context);
  }

  public BlockQuoteView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    View v = LayoutInflater.from(context).inflate(R.layout.blockquote_type_view, this);
    content = v.findViewById(R.id.content);
  }

  public void setText(String text) {
    content.setText(text);
  }
}
