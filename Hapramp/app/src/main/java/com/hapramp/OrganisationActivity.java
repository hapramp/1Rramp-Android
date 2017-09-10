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
import butterknife.BindView;

public class OrganisationActivity extends AppCompatActivity {

    @BindView(R.id.organisation_people_icon)
    TextView organisationPeopleIcon;
    @BindView(R.id.organisation_name)
    EditText organisationName;
    @BindView(R.id.orCaption)
    TextView orCaption;
    @BindView(R.id.seeCaption)
    TextView seeCaption;
    @BindView(R.id.suggestionCaptionContainer)
    LinearLayout suggestionCaptionContainer;
    @BindView(R.id.concentric_bottom_circle)
    FrameLayout concentricBottomCircle;
    @BindView(R.id.concentric_mid_circle)
    FrameLayout concentricMidCircle;
    @BindView(R.id.concentric_top_circle)
    FrameLayout concentricTopCircle;
    @BindView(R.id.organisation_continueBtn)
    TextView organisationContinueBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_up);
        setContentView(R.layout.activity_organisation);
        ButterKnife.bind(this);
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
        Animator bottomCircleAnimation;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bottomCircleAnimation = ViewAnimationUtils.createCircularReveal(concentricBottomCircle, centerX, centerY, startRadius, concentricBottomCircle.getHeight());
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

}