package com.hapramp.views.extraa;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;

import butterknife.BindView;

/**
 * Created by Ankit on 12/19/2017.
 */

public class CreateButtonView extends FrameLayout {


    private static final long REVEAL_DELAY = 1500;
    TextView createArticleBtn;
    TextView createPostBtn;
    TextView plusBtn;
    FrameLayout overlay;
    RelativeLayout root;

    private final int ADD_BUTTON_ROTATION_DELAY = 300;
    private final int FLOATING_BUTTON_DELAY = 100;
    private final float ADD_BUTTON_OVERSHOOT_TENSION = 5f;
    private final float FLOATING_BUTTONOVERSHOOT_TENSION = 5f;
    private final float ADD_BUTTON_ROTATION_ANGLE = 45f;
    private static final int POST_BUTTON_TRANSLATION_Y = 24;
    private static final int ARTICLE_BUTTON_TRANSLATION_Y = 32;


    private Context mContext;
    private boolean isFloating;
    private ItemClickListener itemClickListener;

    public CreateButtonView(@NonNull Context context) {
        super(context);
        init(context);

    }

    public CreateButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public CreateButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {

        this.mContext = context;
        View v = LayoutInflater.from(context).inflate(R.layout.create_new_button_view, this);
        // TODO: 4/4/2018 post creation is pending
//        createArticleBtn = v.findViewById(R.id.createArticleBtn);
        createPostBtn = v.findViewById(R.id.createPostBtn);
        plusBtn = v.findViewById(R.id.plusBtn);
        overlay = v.findViewById(R.id.overlay);
        root = v.findViewById(R.id.root);

        plusBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
        plusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFloating) {
                    // hide
                    hideFloatingButton();
                } else {
                    showFloatingButtons();
                }

            }
        });

        // TODO: 4/4/2018 post creation is pending
//        createArticleBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                hideFloatingButton();
//                if (itemClickListener != null) {
//                    itemClickListener.onCreateArticleButtonClicked();
//                }
//
//            }
//        });

        createPostBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                hideFloatingButton();
                if (itemClickListener != null) {
                    itemClickListener.onCreatePostButtonClicked();
                }
            }
        });

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void hideOverlay() {
       revertOverlayReveal();
    }

    private void showOverlay() {
        revealOverlay();
    }

    private void showFloatingButtons() {

        //todo animate button with spring effect
        // show the overlay
        showOverlay();
        root.setClickable(true);
        // TODO: 4/4/2018 blog creation is pending
        //createArticleBtn.setVisibility(VISIBLE);
        createPostBtn.setVisibility(VISIBLE);

        createPostBtn.setClickable(true);
        // TODO: 4/4/2018 post creation is pending
        // createArticleBtn.setClickable(true);

        // rotate + button
        plusBtn.animate()
                .setInterpolator(new OvershootInterpolator(ADD_BUTTON_OVERSHOOT_TENSION))
                .rotation(ADD_BUTTON_ROTATION_ANGLE)
                .setDuration(ADD_BUTTON_ROTATION_DELAY)
                .start();

        // bump the buttons
        createPostBtn.animate()
                .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
                .translationY(-(getShiftAmount(POST_BUTTON_TRANSLATION_Y)))
                .alpha(1)
                .setDuration(FLOATING_BUTTON_DELAY)
                .start();
// TODO: 4/4/2018 post creation is pending
//        createArticleBtn.animate()
//                .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
//                .alpha(1)
//                .translationY(-(getShiftAmount(ARTICLE_BUTTON_TRANSLATION_Y)))
//                .setDuration(FLOATING_BUTTON_DELAY)
//                .start();

        // set floating
        isFloating = true;

    }

    private void hideFloatingButton() {

        // hide Overlay
        hideOverlay();
        root.setClickable(false);
        // remove buttons
        createPostBtn.setVisibility(GONE);
        // TODO: 4/4/2018 post creation is pending
        //createArticleBtn.setVisibility(GONE);

        // bump the buttons
        createPostBtn.animate()
                .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
                .translationY(0)
                .alpha(0)
                .setDuration(FLOATING_BUTTON_DELAY)
                .start();
// TODO: 4/4/2018 post creation is pending
//        createArticleBtn.animate()
//                .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
//                .alpha(0)
//                .translationY(0)
//                .setDuration(FLOATING_BUTTON_DELAY)
//                .start();

        // re-gain the rotated +
        plusBtn.animate()
                .setInterpolator(new OvershootInterpolator(ADD_BUTTON_OVERSHOOT_TENSION))
                .rotation(0f)
                .setDuration(ADD_BUTTON_ROTATION_DELAY)
                .start();

        //set isFloating
        isFloating = false;

    }

    private void revealOverlay() {
//        // View to reveal -> ratingBarContainer
//        // width of view
//        int w = overlay.getWidth();
//        // height of view
//        int h = overlay.getHeight();
//
//        // radius of reveal
//        int endRadius = (int) Math.hypot(w, h);
//
//        int cx = w/2;
//        int cy = h - getShiftAmount(24);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            overlay.setVisibility(View.VISIBLE);
//            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(overlay, cx, cy, 0, endRadius);
//            revealAnimator.setInterpolator(new DecelerateInterpolator(2f));
//            revealAnimator.setDuration(REVEAL_DELAY);
//            revealAnimator.start();
//
//        } else {
           overlay.setVisibility(View.VISIBLE);
        //}

    }

    private void revertOverlayReveal(){
        // View to reveal -> ratingBarContainer
        // width of view
       /* int w = overlay.getWidth();
        // height of view
        int h = overlay.getHeight();

        // radius of reveal
        int endRadius = (int) Math.hypot(w, h);

        int cx = w/2;
        int cy = h-getShiftAmount(24);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            overlay.setVisibility(View.VISIBLE);
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(overlay, cx, cy,endRadius,0);
            revealAnimator.setInterpolator(new DecelerateInterpolator(5f));
            revealAnimator.setDuration(REVEAL_DELAY);
            revealAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    overlay.setVisibility(GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            revealAnimator.start();

        } else {*/
            overlay.setVisibility(View.GONE);
        //}
    }

    private int getShiftAmount(int dp) {
        return dpToPx(dp);
    }

    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public interface ItemClickListener {
        void onCreateArticleButtonClicked();
        void onCreatePostButtonClicked();
    }
}
