package com.hapramp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;

public class LeaderboardActivity extends AppCompatActivity {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.popupMenuDots)
    TextView popupMenuDots;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.skillsView)
    SkillView skillsView;
    @BindView(R.id.addMoreBtn)
    TextView addMoreBtn;
    @BindView(R.id.hapcoins_icon)
    TextView hapcoinsIcon;
    @BindView(R.id.leaderboard_caption)
    TextView leaderboardCaption;
    @BindView(R.id.category_caption)
    TextView categoryCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        ButterKnife.bind(this);
        setTypefaces();
        addSkills();
    }

    private void setTypefaces(){

        Typeface materialFace = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        closeBtn.setTypeface(materialFace);
        popupMenuDots.setTypeface(materialFace);
        addMoreBtn.setTypeface(materialFace);
    }

    private void addSkills(){
        ArrayList<String> _tags = new ArrayList<>();
        _tags.add("All");
        _tags.add("Art & Craft");
        _tags.add("Photography");
        _tags.add("Dance");
        for (int i=0;i<_tags.size();i++) {
            SkillsItemView skillItem = new SkillsItemView(this);
            skillItem.display(_tags.get(i),false);
            skillsView.addView(skillItem);
        }

        SkillsItemView skillItem = new SkillsItemView(this);
        skillItem.display("Music",true);
        skillsView.addView(skillItem);


    }

}
