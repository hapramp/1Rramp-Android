package com.hapramp.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.PixelUtils;

import butterknife.BindView;

/**
 * Created by Ankit on 1/1/2018.
 */

public class TextSizeView extends FrameLayout {

    @BindView(R.id.biggerT)
    TextView biggerT;
    @BindView(R.id.smallerT)
    TextView smallerT;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_LARGE = 1;
    private static final int STATE_EXTRAA_LARGE = 2;

    private static int tapsCount = 0;
    private int textSize;

    private Resources resources;
    @BindView(R.id.container)
    RelativeLayout container;

    public TextSizeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TextSizeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextSizeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        LayoutInflater.from(context).inflate(R.layout.text_size_button_view, this);
        resources = context.getResources();

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tapsCount++;
                setState(tapsCount % 3);
            }
        });

    }

    private void setState(int tapsCount) {

        switch (tapsCount) {
            case STATE_NORMAL:

                biggerT.setTextColor(resources.getColor(R.color.Black54));
                smallerT.setTextColor(resources.getColor(R.color.Black54));
                textSize = PixelUtils.dpToPx(14);

                break;

            case STATE_LARGE:

                biggerT.setTextColor(resources.getColor(R.color.Black54));
                smallerT.setTextColor(resources.getColor(R.color.Black87));
                textSize = PixelUtils.dpToPx(16);

                break;

            case STATE_EXTRAA_LARGE:

                biggerT.setTextColor(resources.getColor(R.color.Black87));
                smallerT.setTextColor(resources.getColor(R.color.Black54));
                textSize = PixelUtils.dpToPx(18);
                break;

            default:
        }
    }

    public AbsoluteSizeSpan getSizeSpan() {
        // Normal size 14 sp  large 16 sp ex-large 18
        return new AbsoluteSizeSpan(textSize);
    }


}
