package com.hapramp.views.editor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 1/1/2018.
 */

public class TextHeaderView extends FrameLayout {

    @BindView(R.id.biggerT)
    TextView biggerT;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_LARGE = 1;
    private static final int STATE_EXTRAA_LARGE = 2;
    private static int state = STATE_NORMAL;
    @BindView(R.id.container)
    RelativeLayout container;

    private static final String TAG = TextHeaderView.class.getSimpleName();

    public TextHeaderView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TextHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.text_size_button_view, this);
        ButterKnife.bind(this, view);
        biggerT.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        invalidateButtonStates(STATE_NORMAL);

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                state++;
                state %= 3;
                invalidateButtonStates(state);
            }
        });

    }

    private void invalidateButtonStates(int state) {

        switch (state) {

            case STATE_LARGE:

//                biggerT.setTextColor(colorInactive);
//                smallerT.setTextColor(colorActive);
                if (headingChangeListener != null) {
                    headingChangeListener.onHeading2Active();
                }

                break;

            case STATE_EXTRAA_LARGE:

//                biggerT.setTextColor(colorActive);
//                smallerT.setTextColor(colorInactive);
                if (headingChangeListener != null) {
                    headingChangeListener.onHeading1Active();
                }

                break;

            default:

//                biggerT.setTextColor(colorInactive);
//                smallerT.setTextColor(colorInactive);
                if (headingChangeListener != null) {
                    headingChangeListener.onHeadingClear();
                }

                break;
        }
    }

    private HeadingChangeListener headingChangeListener;

    public void setHeadingChangeListener(HeadingChangeListener headingChangeListener) {
        this.headingChangeListener = headingChangeListener;
    }

    public interface HeadingChangeListener {
        void onHeading1Active();

        void onHeading2Active();

        void onHeadingClear();
    }

}
