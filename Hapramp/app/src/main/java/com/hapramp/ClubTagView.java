package com.hapramp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Ankit on 11/12/2017.
 */

public class ClubTagView extends FrameLayout {

    private final String L = "#c75475";
    private final String T = "#8Bc34a";
    private final String P = "#2196f3";

    TextView club1;
    TextView club2;
    TextView club3;

    public ClubTagView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ClubTagView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClubTagView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.club_tag_view, this);
        club1 = (TextView) view.findViewById(R.id.club1);
        club2 = (TextView) view.findViewById(R.id.club2);
        club3 = (TextView) view.findViewById(R.id.club3);
    }

    public void setClubs(String c1, String c2 , String c3){

        if(c1.length()>0){
            club1.setVisibility(VISIBLE);
            club1.setText(c1);
            club1.getBackground().setColorFilter(getColor(c1), PorterDuff.Mode.SRC_ATOP);
        }

        if(c2.length()>0){
            club2.setVisibility(VISIBLE);
            club2.setText(c2);
            club2.getBackground().setColorFilter(getColor(c2), PorterDuff.Mode.SRC_ATOP);
        }

        if(c3.length()>0){
            club3.setVisibility(VISIBLE);
            club3.setText(c3);
            club3.getBackground().setColorFilter(getColor(c3), PorterDuff.Mode.SRC_ATOP);
        }

    }

    private int getColor(String c) {

        int color = 0;

        switch (c){

            case "L":
                color = Color.parseColor(L);
                break;

            case "T":
                color = Color.parseColor(T);
                break;

            case "P":
                color = Color.parseColor(P);
                break;

            default:
                color = Color.parseColor(P);
                break;

        }

        return color;
    }

}
