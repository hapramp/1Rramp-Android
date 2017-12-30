package com.hapramp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hapramp.R;
import com.hapramp.models.response.UserModel;
import com.hapramp.utils.SkillsUtils;

import java.util.List;

/**
 * Created by Ankit on 12/26/2017.
 */

public class InterestsView extends FrameLayout{

    private boolean interestsSet = false;
    private Context mContext;
    private ViewGroup parentView;
    private List<UserModel.Skills> interests;

    public InterestsView(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public InterestsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public InterestsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.category_view_container, this);
        parentView = (ViewGroup) view.findViewById(R.id.viewWrapper);

    }

    private void addViews() {

        for (int i = 0; i < interests.size(); i++) {

            final SkillsItemView view = new SkillsItemView(mContext);
            Log.d("InterestView",interests.get(i).toString());
            view.setSkillTitle(SkillsUtils.getSkillTitleFromId(interests.get(i).id));
            view.setSelection(false);

            parentView.addView(view, i,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        this.interestsSet = true;

    }

    public void setInterests(List<UserModel.Skills> skills) {

        this.interests = skills;

        if(!interestsSet)
            addViews();
    }

}
