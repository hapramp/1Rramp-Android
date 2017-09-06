package com.hapramp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ankit on 6/22/2017.
 */

public class SkillsItemView extends FrameLayout {

    TextView itemTitle;
    public SkillsItemView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.skills_item_view, this);
        itemTitle = (TextView) view.findViewById(R.id.item_title);
    }

    public void display(String text, boolean isSelected) {
        itemTitle.setText(text);
        display(isSelected);
    }

    public void display(final boolean isSelected) {

        int drawable = isSelected?R.drawable.selected_skills_bg:R.drawable.unselected_skills_bg;
        int textColor = isSelected?Color.parseColor("#ffffff"): Color.parseColor("#000000");
        final int sdk = android.os.Build.VERSION.SDK_INT;
        itemTitle.setTextColor(textColor);
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            itemTitle.setBackgroundDrawable( getResources().getDrawable(drawable));
        } else {
            itemTitle.setBackground( getResources().getDrawable(drawable));
        }
    }
}
