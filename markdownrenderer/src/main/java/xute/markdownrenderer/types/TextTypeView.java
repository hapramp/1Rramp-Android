package xute.markdownrenderer.types;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import xute.markdownrenderer.R;


/**
 * Created by Ankit on 4/9/2018.
 */

public class TextTypeView extends FrameLayout {

  private TextView content;

  public TextTypeView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    View v = LayoutInflater.from(context).inflate(R.layout.text_type_view, this);
    content = v.findViewById(R.id.content);
    content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    content.setLineSpacing(0f,1.2f);
  }

  public TextTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public void setText(String text) {
    content.setText(Html.fromHtml(text));
  }

}
