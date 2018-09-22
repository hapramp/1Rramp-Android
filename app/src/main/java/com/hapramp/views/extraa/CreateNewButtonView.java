package com.hapramp.views.extraa;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.analytics.AnalyticsParams;
import com.hapramp.analytics.AnalyticsUtil;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.MomentsUtils;

/**
 * Created by Ankit on 12/19/2017.
 */

public class CreateNewButtonView extends FrameLayout {
  private static final int POST_BUTTON_TRANSLATION_Y = 24;
  private static final int ARTICLE_BUTTON_TRANSLATION_Y = 40;
  private final int ADD_BUTTON_ROTATION_DELAY = 200;
  private final int FLOATING_BUTTON_DELAY = 200;
  private final float ADD_BUTTON_OVERSHOOT_TENSION = 2f;
  private final float FLOATING_BUTTONOVERSHOOT_TENSION = 2f;
  private final float ADD_BUTTON_ROTATION_ANGLE = 45f;
  RelativeLayout addBlogBtn;
  RelativeLayout addPhotoBtn;
  TextView plusBtn;
  FrameLayout overlay;
  RelativeLayout root;
  private Context mContext;
  private boolean isFloating;
  private ItemClickListener itemClickListener;

  public CreateNewButtonView(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View v = LayoutInflater.from(context).inflate(R.layout.create_new_button_view, this);
    addBlogBtn = v.findViewById(R.id.blog_btn);
    addPhotoBtn = v.findViewById(R.id.photo_btn);
    plusBtn = v.findViewById(R.id.plusBtn);
    overlay = v.findViewById(R.id.overlay);
    root = v.findViewById(R.id.root);
    plusBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    plusBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        AnalyticsUtil.logEvent(AnalyticsParams.EVENT_CLICKS_CREATE_BUTTON);
        if (MomentsUtils.isAllowedToCreatePost()) {
          if (isFloating) {
            hideFloatingButton();
          } else {
            showFloatingButtons();
          }
        } else {
          Toast.makeText(mContext, "You can create next post " + MomentsUtils.getTimeLeftInPostCreation(), Toast.LENGTH_LONG).show();
        }
      }
    });

    addBlogBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        hideFloatingButton();
        if (itemClickListener != null) {
          checkConnection();
          itemClickListener.onCreateArticleButtonClicked();
        }
      }
    });

    addPhotoBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        hideFloatingButton();
        if (itemClickListener != null) {
          checkConnection();
          itemClickListener.onCreatePostButtonClicked();
        }
      }
    });

  }

  private void hideFloatingButton() {
    hideOverlay();
    root.setClickable(false);
    addPhotoBtn.setClickable(false);
    addBlogBtn.setClickable(false);
    addPhotoBtn.setVisibility(GONE);
    addBlogBtn.setVisibility(GONE);
    addPhotoBtn.animate()
      .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
      .translationY(0)
      .alpha(0)
      .setDuration(FLOATING_BUTTON_DELAY)
      .start();

    addBlogBtn.animate()
      .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
      .alpha(0)
      .translationY(0)
      .setDuration(FLOATING_BUTTON_DELAY)
      .start();

    plusBtn.animate()
      .setInterpolator(new OvershootInterpolator(ADD_BUTTON_OVERSHOOT_TENSION))
      .rotation(0f)
      .setDuration(ADD_BUTTON_ROTATION_DELAY)
      .start();

    overlay.animate().alpha(0).setDuration(FLOATING_BUTTON_DELAY).start();
    isFloating = false;
  }

  private void showFloatingButtons() {
    showOverlay();
    root.setClickable(true);
    addPhotoBtn.setClickable(true);
    addBlogBtn.setClickable(true);
    addBlogBtn.setVisibility(VISIBLE);
    addPhotoBtn.setVisibility(VISIBLE);
    addPhotoBtn.setClickable(true);
    addBlogBtn.setClickable(true);
    plusBtn.animate()
      .setInterpolator(new OvershootInterpolator(ADD_BUTTON_OVERSHOOT_TENSION))
      .rotation(ADD_BUTTON_ROTATION_ANGLE)
      .setDuration(ADD_BUTTON_ROTATION_DELAY)
      .start();

    addPhotoBtn.animate()
      .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
      .translationY(-(getShiftAmount(POST_BUTTON_TRANSLATION_Y)))
      .alpha(1)
      .setDuration(FLOATING_BUTTON_DELAY)
      .start();

    addBlogBtn.animate()
      .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
      .alpha(1)
      .translationY(-(getShiftAmount(ARTICLE_BUTTON_TRANSLATION_Y)))
      .setDuration(FLOATING_BUTTON_DELAY)
      .start();

    overlay.animate().alpha(1).setDuration(FLOATING_BUTTON_DELAY).start();

    isFloating = true;
  }

  private void checkConnection() {
    if (!ConnectionUtils.isConnected(mContext)) {
      Toast.makeText(mContext, "Internet Connection Required!", Toast.LENGTH_LONG).show();
      return;
    }
  }

  private void hideOverlay() {
    revertOverlayReveal();
  }

  private void showOverlay() {
    revealOverlay();
  }

  private int getShiftAmount(int dp) {
    return dpToPx(dp);
  }

  private void revertOverlayReveal() {
    overlay.setVisibility(View.GONE);
  }

  private void revealOverlay() {
    overlay.setVisibility(View.VISIBLE);
  }

  public int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }

  public CreateNewButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);

  }

  public CreateNewButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setItemClickListener(ItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  public interface ItemClickListener {
    void onCreateArticleButtonClicked();

    void onCreatePostButtonClicked();
  }
}
