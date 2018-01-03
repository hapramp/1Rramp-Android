package com.hapramp.views.editor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 1/2/2018.
 */

public class BoldButtonView extends FrameLayout {

    @BindView(R.id.bold)
    TextView bold;
    @BindView(R.id.container)
    RelativeLayout container;

    private boolean isEnabled;
    private Resources resources;
    private int colorInactive;
    private int colorActive;
    private int spanStart;
    private int spanEnd;
    private EditText target;

    public BoldButtonView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BoldButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoldButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.bold_text_view, this);
        ButterKnife.bind(this, view);
        resources = context.getResources();
        colorInactive = resources.getColor(R.color.Black54);
        colorActive = resources.getColor(R.color.colorPrimary);

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isEnabled = !isEnabled;
                invalidateButton();
                invalidateTarget();
            }
        });

    }

    private void invalidateButton() {
        if (isEnabled) {
            bold.setTextColor(colorActive);
        } else {
            bold.setTextColor(colorInactive);
        }
    }

    private void invalidateTarget() {

        try {
            // remove previous span
            StyleSpan[] spans = target.getText().getSpans(spanStart, spanEnd, StyleSpan.class);
            for (StyleSpan sp : spans) {
                target.getText().removeSpan(sp);
            }
            if(isBoldActive()) {
                target.getText().setSpan(getSpan(), spanStart, spanEnd, 0);
            }

        } catch (Exception e) {

        }

    }

    public StyleSpan getBoldSpan() {
        return getSpan();
    }

    private StyleSpan getSpan() {

        if (isEnabled)
            return new StyleSpan(Typeface.BOLD);
        else
            return new StyleSpan(Typeface.NORMAL);

    }

    public void setTarget(EditText et, int start, int end) {
        this.spanStart = start;
        this.spanEnd = end;
        this.target = et;
    }

    public boolean isBoldActive() {
        return isEnabled;
    }


}
