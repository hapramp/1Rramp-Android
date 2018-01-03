package com.hapramp.views.skills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.api.URLS;
import com.hapramp.utils.ImageHandler;
import com.hapramp.utils.SkillsUtils;

import static com.hapramp.utils.SkillsUtils.ART;
import static com.hapramp.utils.SkillsUtils.DANCE;
import static com.hapramp.utils.SkillsUtils.DRAMATICS;
import static com.hapramp.utils.SkillsUtils.LITERATURE;
import static com.hapramp.utils.SkillsUtils.MUSIC;
import static com.hapramp.utils.SkillsUtils.PHOTOGRAPHY;
import static com.hapramp.utils.SkillsUtils.TRAVEL;

/**
 * Created by Ankit on 10/25/2017.
 */

public class SkillsTabView extends FrameLayout {

    private final Context mContext;
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
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.category_selector_item, this);
        skillsBgImage = (ImageView) view.findViewById(R.id.skills_bg_image);
        skillTitle = (TextView) view.findViewById(R.id.skill_title);
        selectorOverlay = (FrameLayout) view.findViewById(R.id.selector_overlay);
        selectionTabIndicator = (FrameLayout) view.findViewById(R.id.selection_tab_indicator);

    }

    public void setSkillId(int id){
        //set image
        setSkillsBgImage(id);
        // set title
        setSkillTitle(SkillsUtils.getSkillTitleFromId(id));
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        setSelection(isSelected);
    }

    public void enableTabIndicator(boolean enable) {
        if (enable) {
            selectionTabIndicator.setVisibility(VISIBLE);
        }else{
            selectionTabIndicator.setVisibility(GONE);
        }
    }

    private void setSkillsBgImage(int type) {
        this.id = type;

        String resId = URLS.URL_ART;

        switch (type){
            case PHOTOGRAPHY:
                resId = URLS.URL_PHOTO;
                break;
            case DANCE:
                resId = URLS.URL_DANCE;

                break;
            case DRAMATICS:
                resId = URLS.URL_ACTION;

                break;
            case ART:
                resId = URLS.URL_ART;

                break;
            case MUSIC:
                resId = URLS.URL_MUSIC;

                break;
            case TRAVEL:
                resId = URLS.URL_TRAVEL;

                break;
            case LITERATURE:
                resId = URLS.URL_LIT;

                break;
        }

        ImageHandler.loadCircularImage(mContext,skillsBgImage,resId);
    }

    private void setSelection(boolean selected) {
        if (selected) {
            selectionTabIndicator.setVisibility(VISIBLE);
            selectorOverlay.setVisibility(GONE);
        } else {
            selectionTabIndicator.setVisibility(GONE);
            selectorOverlay.setVisibility(VISIBLE);
        }
    }

    private void setSkillTitle(String title) {
        skillTitle.setText(title);
    }


}
