package com.hapramp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import xute.clippedslideview.ClippedSlideView;
import xute.progressdot.ProgressDotView;

public class OnBoardingActivity extends AppCompatActivity {
    @BindView(R.id.clippedSlideView)
    ClippedSlideView clippedSlideView;
    @BindView(R.id.dotsView)
    ProgressDotView dotsView;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.next)
    TextView next;
    @BindView(R.id.onboardingTitle)
    TextView onboardingTitle;
    @BindView(R.id.onboardingContent)
    TextView onboardingContent;
    private int[] icons;
    private int mCurrentIndex;
    private int[] titleIds;
    private int[] contentIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        ButterKnife.bind(this);
        init();
        setListeners();
    }

    private void init() {
        icons = new int[]{R.drawable.user_shape, R.drawable.share, R.drawable.dollar};
        titleIds = new int[]{R.string.onboarding_title_1, R.string.onboarding_title_2, R.string.onboarding_title_3};
        contentIds = new int[]{R.string.onboarding_content_1, R.string.onboarding_content_2, R.string.onboarding_content_3};
        clippedSlideView.setImageResource(icons);
        invalidateTitleAndContent(0);
    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!startReached(mCurrentIndex)) {
                    mCurrentIndex--;
                    clippedSlideView.slideRight();
                    dotsView.moveBack();
                    invalidateNavButton(mCurrentIndex);
                    invalidateTitleAndContent(mCurrentIndex);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!endReached(mCurrentIndex)) {
                    mCurrentIndex++;
                    clippedSlideView.slideLeft();
                    dotsView.moveToNext();
                    invalidateNavButton(mCurrentIndex);
                    invalidateTitleAndContent(mCurrentIndex);
                } else {
                    navigateToLoginPage();
                }
            }
        });
    }

    private void invalidateTitleAndContent(int mCurrentIndex) {
        onboardingTitle.setText(getTitleAtIndex(mCurrentIndex));
        onboardingContent.setText(getContentAtIndex(mCurrentIndex));
    }

    private void navigateToLoginPage() {
        HaprampPreferenceManager.getInstance().setOnBoardingVisited();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void invalidateNavButton(int mCurrentIndex) {
        if (endReached(mCurrentIndex)) {
            next.setText("START EARNING!");
        } else {
            next.setText("NEXT");
        }
        if (startReached(mCurrentIndex)) {
            back.setTextColor(getResources().getColor(R.color.Black38));
            back.setEnabled(false);
        } else {
            back.setTextColor(getResources().getColor(R.color.colorPrimary));
            back.setEnabled(true);
        }
    }

    private String getTitleAtIndex(int index) {
        return getResources().getString(titleIds[index]);
    }

    private String getContentAtIndex(int index) {
        return getResources().getString(contentIds[index]);
    }

    private boolean endReached(int index) {
        return index >= icons.length - 1;
    }

    private boolean startReached(int index) {
        return index == 0;
    }

}
