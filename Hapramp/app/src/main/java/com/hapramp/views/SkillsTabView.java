package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;

/**
 * Created by Ankit on 10/25/2017.
 */

public class SkillsTabView extends FrameLayout {

    ImageView skillsBgImage;
    TextView skillTitle;
    FrameLayout selectorOverlay;
    FrameLayout selectionTabIndicator;

    private boolean isSelected = false;

    public static final int ART = 1;
    public static final int DANCE = 2;
    public static final int TRAVEL = 3;
    public static final int LITERATURE = 4;
    public static final int ACTION = 5;
    public static final int PHOTOGRAPHY = 6;
    public static final int MUSIC = 8;

    private int id = -1;

    public SkillsTabView(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.category_selector_item, this);
        skillsBgImage = (ImageView) view.findViewById(R.id.skills_bg_image);
        skillTitle = (TextView) view.findViewById(R.id.skill_title);
        selectorOverlay = (FrameLayout) view.findViewById(R.id.selector_overlay);
        selectionTabIndicator = (FrameLayout) view.findViewById(R.id.selection_tab_indicator);
    }

    public boolean getIsSelected(){
        return isSelected;
    }

    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;
        setSelection(isSelected);
    }

//    public void setOnClickListener(final CategoryRecyclerAdapter.OnCategoryItemClickListener clickListener){
//        selectorOverlay.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v){
//                setSelected(!isSelected);
//                clickListener.onCategoryClicked(id);
//            }
//        });
//    }
//
    public void setSkillsBgImage(int type){

        int resId = -1;
        this.id = type;

        switch (type){

            case PHOTOGRAPHY:

                resId = R.drawable.photography_icon;
                break;

            case DANCE:

                resId = R.drawable.dance_icon;
                break;

            case ACTION:

                resId = R.drawable.act_icon;
                break;

            case ART:

                resId = R.drawable.art_icon;
                break;

            case MUSIC:

                resId = R.drawable.music_icon;
                break;

            case TRAVEL:

                resId = R.drawable.travel_icon;
                break;

            case LITERATURE:

                resId = R.drawable.literature_icon;
                break;

            default:

                resId = R.drawable.profile_user_dp_circle;
                break;

        }

        skillsBgImage.setImageResource(resId);

    }

    private void setSelection(boolean selected){
        if(selected){
            selectionTabIndicator.setVisibility(VISIBLE);
            selectorOverlay.setVisibility(GONE);
        }else{
            selectionTabIndicator.setVisibility(GONE);
            selectorOverlay.setVisibility(VISIBLE);
        }
    }

    public void setSkillTitle(String title){
        skillTitle.setText(title);
    }



}
