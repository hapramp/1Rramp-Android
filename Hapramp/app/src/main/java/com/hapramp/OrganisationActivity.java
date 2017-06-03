package com.hapramp;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OrganisationActivity extends AppCompatActivity {

    @InjectView(R.id.organisation_people_icon)
    TextView organisationPeopleIcon;
    @InjectView(R.id.organisation_name)
    EditText organisationName;
    @InjectView(R.id.orCaption)
    TextView orCaption;
    @InjectView(R.id.seeCaption)
    TextView seeCaption;
    @InjectView(R.id.suggestionCaptionContainer)
    LinearLayout suggestionCaptionContainer;
    @InjectView(R.id.concentric_bottom_circle)
    FrameLayout concentricBottomCircle;
    @InjectView(R.id.concentric_mid_circle)
    FrameLayout concentricMidCircle;
    @InjectView(R.id.concentric_top_circle)
    FrameLayout concentricTopCircle;
    @InjectView(R.id.organisation_continueBtn)
    TextView organisationContinueBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_up);
        setContentView(R.layout.activity_organisation);
        ButterKnife.inject(this);
        setTypeFace();
        attachListeners();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                revealCircle();
            }
        }, 500);
    }

    private void attachListeners() {
            organisationContinueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    revealCircle();
                }
            });
    }

    private void setTypeFace() {
        organisationPeopleIcon.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    }

    private void revealCircle() {

        int startRadius = 0;
        int centerX = concentricTopCircle.getWidth() / 2;
        int centerY = 0;

        Animator bottomCircleAnimation = ViewAnimationUtils.createCircularReveal(concentricBottomCircle, centerX, centerY, startRadius, concentricBottomCircle.getHeight());
        Animator midCircleAnimation = ViewAnimationUtils.createCircularReveal(concentricMidCircle, centerX, centerY, startRadius, concentricMidCircle.getHeight());
        Animator topCircleAnimation = ViewAnimationUtils.createCircularReveal(concentricTopCircle, centerX, centerY, startRadius, concentricTopCircle.getHeight());

        bottomCircleAnimation.setDuration(800);
        bottomCircleAnimation.setInterpolator(new DecelerateInterpolator(1.5f));

        midCircleAnimation.setDuration(1000);
        midCircleAnimation.setInterpolator(new DecelerateInterpolator(1.5f));

        topCircleAnimation.setDuration(1200);
        topCircleAnimation.setInterpolator(new DecelerateInterpolator(1.5f));

        bottomCircleAnimation.start();
        concentricBottomCircle.setVisibility(View.VISIBLE);
        midCircleAnimation.start();
        concentricMidCircle.setVisibility(View.VISIBLE);
        topCircleAnimation.start();
        concentricTopCircle.setVisibility(View.VISIBLE);

    }

}
