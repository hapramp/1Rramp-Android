package com.hapramp.views.types;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hapramp.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Ankit on 4/12/2018.
 */

public class BulletTypeView extends FrameLayout {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_UL, TYPE_OL})
    public @interface BulletOrderType {
    }

    public static final int TYPE_OL = 0;
    public static final int TYPE_UL = 1;

    private Context mContext;
    private TextView content;
    private int bulletType = TYPE_OL;

    public BulletTypeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BulletTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BulletTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.bullet_type_view, this);
        content = v.findViewById(R.id.content);
    }

    public void setBulletType(@BulletOrderType int type) {
        this.bulletType = type;
    }

    public void setText(String text) {
        //take out the strings
        String[] lines = text.split("\n");
        StringBuilder stringBuilder = new StringBuilder();

        if (bulletType == TYPE_OL) {
            for (int i = 0; i < lines.length; i++) {
                stringBuilder.append(i + 1)
                        .append(" ")
                        .append(lines[1])
                        .append("\n");
            }
        } else {
            for (int i = 0; i < lines.length; i++) {
                stringBuilder.append("•")
                        .append(" ")
                        .append(lines[1])
                        .append("\n");
            }
        }

        content.setText(stringBuilder.toString());

    }

}
