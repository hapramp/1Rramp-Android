package com.hapramp;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Ankit on 6/2/2017.
 */

public class SkillView extends ViewGroup {

    int deviceWidth;

    public SkillView(Context context) {
        this(context,null,0);
    }

    public SkillView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SkillView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int childCount = getChildCount();
        // accumulative variables for all views
        int curWidth,curHeight,curLeft,curTop,maxHeight;

        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        int childWidth = childRight - childLeft;
        int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for(int i=0;i<childCount;i++){

            View childView = getChildAt(i);
            curWidth = childView.getMeasuredWidth();
            curHeight = childView.getMeasuredHeight();

            if((curLeft + curWidth)>childRight){
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }

            childView.layout(curLeft,curTop , curLeft+curWidth,curTop+curHeight);
            maxHeight = Math.max(maxHeight,curHeight);
            curLeft += curWidth;


        }

    }

    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        deviceWidth = point.x;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;

        for (int i = 0; i < childCount; i++) {

            final View child = getChildAt(i);
            if(child.getVisibility()==GONE)
                continue;
            //measure the child
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
            mLeftWidth += child.getMeasuredWidth();

            if ((mLeftWidth / deviceWidth) > rowCount) {
                maxHeight += child.getMeasuredHeight();
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }

            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

}
