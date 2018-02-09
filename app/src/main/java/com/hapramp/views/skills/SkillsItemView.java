package com.hapramp.views.skills;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.api.URLS;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.SkillsUtils;

import java.net.URL;

import static com.hapramp.utils.SkillsUtils.DRAMATICS;
import static com.hapramp.utils.SkillsUtils.ART;
import static com.hapramp.utils.SkillsUtils.DANCE;
import static com.hapramp.utils.SkillsUtils.LITERATURE;
import static com.hapramp.utils.SkillsUtils.MUSIC;
import static com.hapramp.utils.SkillsUtils.PHOTOGRAPHY;
import static com.hapramp.utils.SkillsUtils.TRAVEL;

/**
 * Created by Ankit on 6/22/2017.
 */

public class SkillsItemView extends FrameLayout {



    ImageView skillsBgImage;
    TextView skillSelectionOverlay;
    TextView skillTitle;
    private Context mContext;

    public SkillsItemView(Context context) {
        super(context);
        init(context);
    }

    public SkillsItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SkillsItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.skills_view, this);
        skillsBgImage = (ImageView) view.findViewById(R.id.skills_bg_image);
        skillSelectionOverlay = (TextView) view.findViewById(R.id.skill_selection_overlay);
        skillTitle = (TextView) view.findViewById(R.id.skill_title);
        skillSelectionOverlay.setTypeface(new FontManager().getTypeFace(FontManager.FONT_MATERIAL));

    }

    private void setSkillsBgImage(int type){

        String resId = URLS.URL_ART;
        String color = "#795548";

        switch (type){
            case PHOTOGRAPHY:
                resId = URLS.URL_PHOTO;
                color = "#f44336";
                break;
            case DANCE:
                resId = URLS.URL_DANCE;
                color = "#e91e63";
                break;
            case DRAMATICS:
                resId = URLS.URL_ACTION;
                color = "#9c27b0";
                break;
            case ART:
                resId = URLS.URL_ART;
                color = "#3949ab";
                break;
            case MUSIC:
                resId = URLS.URL_MUSIC;
                color = "#009688";
                break;
            case TRAVEL:
                resId = URLS.URL_TRAVEL;
                color = "#607d8b";
                break;
            case LITERATURE:
                resId = URLS.URL_LIT;
                color = "#ff5722";
                break;
        }

        ImageHandler.loadCircularImage(mContext,skillsBgImage,resId);
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
        setSkillsBgImage(SkillsUtils.getSkillIdFromName(title));
    }

    public String getSkill(){
        return skillTitle.getText().toString();
    }

}
