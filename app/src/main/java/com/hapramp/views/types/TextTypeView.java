package com.hapramp.views.types;

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

import com.hapramp.R;
import com.hapramp.editor.FontFace;

import static com.hapramp.editor.FontSize.NORMALTEXTSIZE;


/**
 * Created by Ankit on 4/9/2018.
 */

public class TextTypeView extends FrameLayout{

    private TextView content;

    public TextTypeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TextTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View v = LayoutInflater.from(context).inflate(R.layout.text_type_view,this);
        content = v.findViewById(R.id.content);
        content.setTypeface(FontFace.getNormalTypeface(context));
    }

    public void setTextSize(int size){
        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setText(String text){
        content.setText(Html.fromHtml(text));
    }

}
