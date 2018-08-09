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

public class HeadingTwoTypeView extends FrameLayout {

    private TextView content;

    public HeadingTwoTypeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HeadingTwoTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeadingTwoTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.heading_two_type_view, this);
        content = v.findViewById(R.id.content);
       // content.setTypeface(FontFace.getBoldTypeface(context));
    }

    public void setTextSize(int size){
        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
    public void setText(String text) {
        content.setText(Html.fromHtml(text));
    }

}
