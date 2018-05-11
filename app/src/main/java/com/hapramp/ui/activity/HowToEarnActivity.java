package com.hapramp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HowToEarnActivity extends AppCompatActivity {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.how_to_redeem_caption)
    TextView howToRedeemCaption;
    @BindView(R.id.table_header)
    LinearLayout tableHeader;
    @BindView(R.id.table_header_divider)
    FrameLayout tableHeaderDivider;
    @BindView(R.id.row1)
    LinearLayout row1;
    @BindView(R.id.row2)
    LinearLayout row2;
    @BindView(R.id.row3)
    LinearLayout row3;
    @BindView(R.id.table)
    LinearLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_earn);
        ButterKnife.bind(this);

        closeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
