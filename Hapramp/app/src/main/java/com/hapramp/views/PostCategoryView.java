package com.hapramp.views;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.models.response.PostResponse;
import com.hapramp.utils.SkillsConverter;

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

        View view = LayoutInflater.from(mContext).inflate(R.layout.post_category_view_container, this);
        rootView = (ViewGroup) view.findViewById(R.id.postCategoryContainer);
        selectedSkills = new ArrayList<>();

    }

    private void addViews() {

        for (int i = 0; i < skills.length; i++) {

            final CategoryTextView view = new CategoryTextView(mContext);
            view.setText(skills[i]);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index = selectedSkills.indexOf(SkillsConverter.getSkillIdFromName(view.getSkill()));
                    if(index==-1){
                        // select it
                        if (selectedSkills.size() > 2) {
                            Toast.makeText(mContext,"Maximum 3 Skills",Toast.LENGTH_LONG).show();
                        } else {
                            view.setSelected(true);
                            selectedSkills.add(SkillsConverter.getSkillIdFromName(view.getSkill()));
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
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
        }

    }

    public List<Integer> getSelectedSkills(){
        return selectedSkills;
    }

    public void setCategoryItems(String[] skills) {
        this.skills = skills;
        addViews();
    }

    private void click(String skill){

    }

}
