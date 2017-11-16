package com.hapramp.views;

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

import com.hapramp.models.response.CompetitionsPostReponse;
import com.hapramp.R;
import com.hapramp.utils.SkillsConverter;
import com.hapramp.models.response.PostResponse;

import java.util.List;

/**
 * Created by Ankit on 11/12/2017.
 */

public class ClubTagView extends FrameLayout {

    private final String L = "#c75475";
    private final String T = "#8Bc34a";
    private final String P = "#2196f3";
    private final String D = "#607d8b";
    private final String A = "#9c27b0";
    private final String M = "#FFEB3B";

    TextView club1;
    TextView club2;
    TextView club3;
    private List<CompetitionsPostReponse.Skills> skills;

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

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.club_tag_view, this);
        club1 = (TextView) view.findViewById(R.id.club1);
        club2 = (TextView) view.findViewById(R.id.club2);
        club3 = (TextView) view.findViewById(R.id.club3);
    }

    public void setClubs(String c1, String c2, String c3) {

        if (c1.length() > 0) {
            club1.setVisibility(VISIBLE);
            club1.setText(c1);
            club1.getBackground().setColorFilter(getColor(c1), PorterDuff.Mode.SRC_ATOP);
        }

        if (c2.length() > 0) {
            club2.setVisibility(VISIBLE);
            club2.setText(c2);
            club2.getBackground().setColorFilter(getColor(c2), PorterDuff.Mode.SRC_ATOP);
        }

        if (c3.length() > 0) {
            club3.setVisibility(VISIBLE);
            club3.setText(c3);
            club3.getBackground().setColorFilter(getColor(c3), PorterDuff.Mode.SRC_ATOP);
        }

    }

    private int getColor(String c) {

        int color = 0;

        switch (c) {

            case "L":
                color = Color.parseColor(L);
                break;

            case "T":
                color = Color.parseColor(T);
                break;

            case "P":
                color = Color.parseColor(P);
                break;
            case "D":
                color = Color.parseColor(D);
                break;
            case "A":
                color = Color.parseColor(A);
                break;
            case "M":
                color = Color.parseColor(M);
                break;
            default:
                color = Color.parseColor(P);
                break;

        }
        return color;

    }

    public void setCompetitionSkills(List<CompetitionsPostReponse.Skills> skills) {

        int s = skills.size();
        if (s > 0) {
            //1
            if (s > 1) {
                //2
                if (s > 2) {
                    //3
                    setClubs(SkillsConverter.getSkillCharacter(skills.get(0).id),
                            SkillsConverter.getSkillCharacter(skills.get(1).id),
                            SkillsConverter.getSkillCharacter(skills.get(2).id)
                    );
                    return;
                }
                setClubs(SkillsConverter.getSkillCharacter(skills.get(0).id),
                        SkillsConverter.getSkillCharacter(skills.get(1).id),
                        ""
                );
                return;
            }
            setClubs(SkillsConverter.getSkillCharacter(skills.get(0).id),
                    "",
                    ""
            );
            return;
        }
    }

    public void setPostSkills(List<PostResponse.Skills> skills) {

        int s = skills.size();
        if (s > 0) {
            //1
            if (s > 1) {
                //2
                if (s > 2) {
                    //3
                    setClubs(SkillsConverter.getSkillCharacter(skills.get(0).id),
                            SkillsConverter.getSkillCharacter(skills.get(1).id),
                            SkillsConverter.getSkillCharacter(skills.get(2).id)
                    );
                    return;
                }
                setClubs(SkillsConverter.getSkillCharacter(skills.get(0).id),
                        SkillsConverter.getSkillCharacter(skills.get(1).id),
                        ""
                );
                return;
            }
            setClubs(SkillsConverter.getSkillCharacter(skills.get(0).id),
                    "",
                    ""
            );
            return;
        }
    }

}
