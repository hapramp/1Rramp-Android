package com.hapramp.views.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.utils.SkillsUtils;
import com.hapramp.views.extraa.CategoryTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 12/25/2017.
 */

public class PostCategoryView extends FrameLayout {

    private ViewGroup rootView;
    private Context mContext;
    private String[] skills;
    private ArrayList<Integer> selectedSkills;

    public PostCategoryView(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PostCategoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public PostCategoryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.category_view_container, this);
        rootView = (ViewGroup) view.findViewById(R.id.viewWrapper);
        selectedSkills = new ArrayList<>();

    }

    private void addViews() {

        for (int i = 0; i < skills.length; i++) {

            final CategoryTextView view = new CategoryTextView(mContext);
            view.setText(skills[i]);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index = selectedSkills.indexOf(SkillsUtils.getSkillIdFromName(view.getSkill()));
                    if(index==-1){
                        // select it
                        if (selectedSkills.size() > 2) {
                            Toast.makeText(mContext,"Maximum 3 Skills",Toast.LENGTH_LONG).show();
                        } else {
                            view.setSelected(true);
                            selectedSkills.add(SkillsUtils.getSkillIdFromName(view.getSkill()));
                        }
                    }else{
                        // de-select it
                        view.setSelected(false);
                        selectedSkills.remove(index);
                    }

                }
            });

            rootView.addView(view, i,
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

    public List<Integer> getSelectedSkills(){
        return selectedSkills;
    }

    public List<String> getSelectedSkillsTitle(){

        List<String> selectedTitles = new ArrayList<>();
        for (Integer id :selectedSkills) {
            selectedTitles.add(SkillsUtils.getSkillTitleFromId(id));
        }
        return selectedTitles;

    }

    public void setCategoryItems(String[] skills) {
        this.skills = skills;
        addViews();
    }

    private void click(String skill){

    }

}
