package com.hapramp.views.types;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.steem.ContentTypes;

/**
 * Created by Ankit on 4/9/2018.
 */

public class HeadingThreeTypeView extends FrameLayout {

    private TextView content;
    private float h1Size = 24f;

    public HeadingThreeTypeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HeadingThreeTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeadingThreeTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.heading_three_type_view, this);
        content = v.findViewById(R.id.content);
    }

    public void setText(String text) {
        content.setText(text);
    }

}
