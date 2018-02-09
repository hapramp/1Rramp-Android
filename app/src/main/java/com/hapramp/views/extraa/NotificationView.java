package com.hapramp.views.extraa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;

/**
 * Created by Ankit on 10/23/2017.
 */

public class NotificationView extends FrameLayout {

    TextView countTv;
    TextView iconTv;

    public NotificationView(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.action_bar_notification_view,this);
        iconTv = (TextView) view.findViewById(R.id.notification_icon);
        countTv = (TextView) view.findViewById(R.id.notification_count);
        iconTv.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    }

    public void setCount(int count){

        if(count>0) {
            countTv.setVisibility(VISIBLE);
            countTv.setText(String.valueOf(count));
        }else{
            countTv.setVisibility(GONE);
        }
    }
}
