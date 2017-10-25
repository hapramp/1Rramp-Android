package com.hapramp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 6/22/2017.
 */

public class SkillsItemView extends FrameLayout {

    public static final int ART = 1;
    public static final int DANCE = 2;
    public static final int TRAVEL = 3;
    public static final int LITERATURE = 4;
    public static final int ACTION = 5;
    public static final int PHOTOGRAPHY = 6;
    public static final int SOCIAL = 7;
    public static final int MUSIC = 8;

    SimpleDraweeView skillsBgImage;
    TextView skillSelectionOverlay;
    TextView skillTitle;

    public SkillsItemView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.skills_view, this);
        skillsBgImage = (SimpleDraweeView) view.findViewById(R.id.skills_bg_image);
        skillSelectionOverlay = (TextView) view.findViewById(R.id.skill_selection_overlay);
        skillTitle = (TextView) view.findViewById(R.id.skill_title);
        skillSelectionOverlay.setTypeface(new FontManager().getTypeFace(FontManager.FONT_MATERIAL));
    }

    public void setSkillsBgImage(int type){
        int resId = -1;
        String color = "#795548";
        switch (type){
            case PHOTOGRAPHY:
                resId = R.drawable.photography_icon;
                color = "#f44336";
                break;
            case DANCE:
                resId = R.drawable.dance_icon;
                color = "#e91e63";
                break;
            case ACTION:
                resId = R.drawable.act_icon;
                color = "#9c27b0";
                break;
            case ART:
                resId = R.drawable.art_icon;
                color = "#3949ab";
                break;
            case MUSIC:
                resId = R.drawable.music_icon;
                color = "#009688";
                break;
            case TRAVEL:
                resId = R.drawable.travel_icon;
                color = "#607d8b";
                break;
            case LITERATURE:
                resId = R.drawable.literature_icon;
                color = "#ff5722";
                break;
        }
        skillsBgImage.setImageResource(resId);
        setOverlayColor(color);
    }

    private void setOverlayColor(String color){
        GradientDrawable background = (GradientDrawable) skillSelectionOverlay.getBackground();
        background.setColor(Color.parseColor(color));
    }

    public void setSelection(boolean selected){
        if(selected){
            skillSelectionOverlay.setVisibility(VISIBLE);
        }else{
            skillSelectionOverlay.setVisibility(GONE);
        }
    }

    public void setSkillTitle(String title){
        skillTitle.setText(title);
    }

}
