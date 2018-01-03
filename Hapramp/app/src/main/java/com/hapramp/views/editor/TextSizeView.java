package com.hapramp.views.editor;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.PixelUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private static final int TEXT_SIZE_NORMAL = 16;
    private static final int TEXT_SIZE_LARGE = 24;
    private static final int TEXT_SIZE_EXTRA_LARGE = 36;
    private static int state = STATE_NORMAL;

    private Resources resources;
    @BindView(R.id.container)
    RelativeLayout container;
    private EditText target;
    private int spanEnd;
    private int spanStart;

    private int colorInactive;
    private int colorActive;



    private static final String TAG = TextSizeView.class.getSimpleName();

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

        View view = LayoutInflater.from(context).inflate(R.layout.text_size_button_view, this);
        ButterKnife.bind(this,view);

        resources = context.getResources();
        colorInactive = resources.getColor(R.color.Black54);
        colorActive = resources.getColor(R.color.colorPrimary);

        invalidateButtonStates(STATE_NORMAL);

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                state++;
                state%=3;
                invalidateButtonStates(state);
                invalidateTarget();
            }
        });

    }

    public void setSpan(AbsoluteSizeSpan sizeSpan, EditText target, int start, int end) {

        this.target = target;
        this.spanStart = start;
        this.spanEnd = end;
        int size = sizeSpan.getSize();

        //detemine state of Button
        switch (PixelUtils.pxToDp(size)) {
            case TEXT_SIZE_LARGE:
                state = STATE_LARGE;
                invalidateButtonStates(STATE_LARGE);
                break;

            case TEXT_SIZE_EXTRA_LARGE:
                state = STATE_EXTRAA_LARGE;
                invalidateButtonStates(STATE_EXTRAA_LARGE);
                break;

            default:
                state = STATE_NORMAL;
                invalidateButtonStates(STATE_NORMAL);
                break;

        }

    }

    private void invalidateButtonStates(int state) {

        switch (state) {

            case STATE_LARGE:

                biggerT.setTextColor(colorInactive);
                smallerT.setTextColor(colorActive);

                break;

            case STATE_EXTRAA_LARGE:

                biggerT.setTextColor(colorActive);
                smallerT.setTextColor(colorInactive);

                break;

            default:

                biggerT.setTextColor(colorInactive);
                smallerT.setTextColor(colorInactive);

                break;
        }


    }

    private void invalidateTarget() {
        try{
        target.getText().setSpan(new AbsoluteSizeSpan(getTextSize(state)), spanStart, spanEnd, 0);
        }catch (Exception e){

        }

    }

    public AbsoluteSizeSpan getSizeSpan() {
        // Normal size 14 sp  large 16 sp ex-large 18
        return new AbsoluteSizeSpan(getTextSize(state));
    }

    private int getTextSize(int state) {
        switch (state) {
            case STATE_LARGE:
                return PixelUtils.dpToPx(TEXT_SIZE_LARGE);
            case STATE_EXTRAA_LARGE:
                return PixelUtils.dpToPx(TEXT_SIZE_EXTRA_LARGE);
            default:
                return PixelUtils.dpToPx(TEXT_SIZE_NORMAL);
        }

    }

    public boolean isActive(){
        return state!=STATE_NORMAL;
    }

    private void l(String msg){
       // Log.d(TAG,msg);
    }

    public void setTarget(EditText et,int start,int end) {
        this.spanStart = start;
        this.spanEnd = end;
        this.target = et;
    }

}
