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
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.models.response.UserModel;
import com.hapramp.utils.SkillsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 12/26/2017.
 */

public class SelectableInterestsView extends FrameLayout {

    private Context mContext;
    private String[] skills;
    private ArrayList<Integer> selectedSkills;
    private ViewGroup parentView;

    public SelectableInterestsView(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SelectableInterestsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SelectableInterestsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.category_view_container, this);
        parentView = (ViewGroup) view.findViewById(R.id.viewWrapper);
        selectedSkills = new ArrayList<>();
        skills = SkillsUtils.getSkillsSet();

    }

    private void addViews() {

        for (int i = 0; i < skills.length; i++) {

            final SkillsItemView view = new SkillsItemView(mContext);
            view.setSkillTitle(skills[i]);
            // set selection
            //
            view.setSelection((selectedSkills.indexOf(SkillsUtils.getSkillIdFromName(skills[i])) > -1));


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index = selectedSkills.indexOf(SkillsUtils.getSkillIdFromName(view.getSkill()));
                    if (index == -1) {
                        // select it
                        view.setSelection(true);

                        selectedSkills.add(SkillsUtils.getSkillIdFromName(view.getSkill()));

                    } else {
                        // de-select it
                        if (selectedSkills.size() < 2) {
                            // warn for selecting atleast one
                            Toast.makeText(mContext, "Atleast One Skill Should be There", Toast.LENGTH_LONG).show();
                            return;
                        }
                        view.setSelection(false);
                        selectedSkills.remove(index);

                    }

                }
            });

            parentView.addView(view, i,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

    public List<Integer> getSelectedSkills() {
        return selectedSkills;
    }

    public void setInterests(List<UserModel.Skills> skills) {
        for (UserModel.Skills skill : skills) {
            selectedSkills.add(skill.getId());
        }
        addViews();
    }

}
