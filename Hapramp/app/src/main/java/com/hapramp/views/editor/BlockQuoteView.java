package com.hapramp.views.editor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.QuoteSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.PixelUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 1/2/2018.
 */

public class BlockQuoteView extends FrameLayout {

    private static float MY_GAP_WIDTH = 14;
    private static float MY_STRIPE_WIDTH = 16;
    private static int MY_STRIPE_COLOR = -1;
    @BindView(R.id.bloque)
    TextView bloque;
    @BindView(R.id.container)
    RelativeLayout container;
    private Resources resources;
    private int colorInactive;
    private int colorActive;
    private boolean isEnabled;
    private int spanStart;
    private int spanEnd;
    private EditText target;
    private int bgcolor;

    public BlockQuoteView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BlockQuoteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BlockQuoteView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.bloquote_view, this);
        ButterKnife.bind(this, view);
        resources = context.getResources();
        colorInactive = resources.getColor(R.color.Black54);
        colorActive = resources.getColor(R.color.colorPrimary);

        bgcolor = resources.getColor(R.color.Black12);
        MY_STRIPE_COLOR = resources.getColor(R.color.colorPrimary);
        MY_STRIPE_WIDTH = PixelUtils.dpToPx(4);
        MY_GAP_WIDTH = PixelUtils.dpToPx(2);

        bloque.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

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
            bloque.setTextColor(colorActive);
        } else {
            bloque.setTextColor(colorInactive);
        }
    }

    private void invalidateTarget() {

        try {
            CustomQuoteSpan[] spanToRemove = target.getText().getSpans(spanStart, spanEnd, CustomQuoteSpan.class);

            for (CustomQuoteSpan aSpanToRemove : spanToRemove)
                target.getText().removeSpan(aSpanToRemove);

            if (isQuoteActive()) {
                target.getText().setSpan(getQuoteSpan(), spanStart, spanEnd, 0);
            }

        } catch (Exception e) {

        }
    }

    public CustomQuoteSpan getQuoteSpan() throws Exception {

        CustomQuoteSpan qs = getSpan();

        if (qs != null) {
            target.getText().setSpan(qs, spanStart, spanEnd, 0);
            return qs;
        } else {
            throw new Exception("Quote Span should not be Null");
        }

    }

    private CustomQuoteSpan getSpan() {
        if (isEnabled)
            return new CustomQuoteSpan(
                bgcolor,
                MY_STRIPE_COLOR,
                MY_STRIPE_WIDTH,
                MY_GAP_WIDTH);
        else return null;
    }


    public void setTarget(EditText et, int start, int end) {
        this.spanStart = start;
        this.spanEnd = end;
        this.target = et;
    }

    public boolean isQuoteActive() {
        return isEnabled;
    }

    public class CustomQuoteSpan implements LeadingMarginSpan, LineBackgroundSpan {
        private final int backgroundColor;
        private final int stripeColor;
        private final float stripeWidth;
        private final float gap;

        public CustomQuoteSpan(int backgroundColor, int stripeColor, float stripeWidth, float gap) {
            this.backgroundColor = backgroundColor;
            this.stripeColor = stripeColor;
            this.stripeWidth = stripeWidth;
            this.gap = gap;
        }

        @Override
        public int getLeadingMargin(boolean first) {
            return (int) (stripeWidth + gap);
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom,
                                      CharSequence text, int start, int end, boolean first, Layout layout) {
            Paint.Style style = p.getStyle();
            int paintColor = p.getColor();

            p.setStyle(Paint.Style.FILL);
            p.setColor(stripeColor);

            c.drawRect(x, top, x + dir * stripeWidth, bottom, p);

            p.setStyle(style);
            p.setColor(paintColor);
        }

        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
            int paintColor = p.getColor();
            p.setColor(backgroundColor);
            c.drawRect(left, top, right, bottom, p);
            p.setColor(paintColor);
        }
    }

}
