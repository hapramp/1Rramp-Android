package com.hapramp.views.types;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;

/**
 * Created by Ankit on 4/9/2018.
 */

public class HeadingFourTypeView extends FrameLayout {

    private TextView content;

    public HeadingFourTypeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HeadingFourTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeadingFourTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.heading_four_type_view, this);
        content = v.findViewById(R.id.content);
    }

    public void setText(String text) {
        content.setText(text);
    }

}
