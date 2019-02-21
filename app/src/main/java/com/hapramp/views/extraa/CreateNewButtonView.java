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
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.ConnectionUtils;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.MomentsUtils;

/**
 * Created by Ankit on 12/19/2017.
 */

public class CreateNewButtonView extends FrameLayout {
  private static int competitionButtonTranslationY = 24;
  private static int postButtonTranslationY = 36;// + 12
  private static int blogButtonTranslationY = 48;// + 12
  private final int ADD_BUTTON_ROTATION_DELAY = 200;
  private final int FLOATING_BUTTON_DELAY = 200;
  private final float ADD_BUTTON_OVERSHOOT_TENSION = 2f;
  private final float FLOATING_BUTTONOVERSHOOT_TENSION = 2f;
  private final float ADD_BUTTON_ROTATION_ANGLE = 45f;
  RelativeLayout addBlogBtn;
  RelativeLayout addPhotoBtn;
  RelativeLayout competitionBtn;
  TextView plusBtn;
  FrameLayout overlay;
  RelativeLayout root;
  private Context mContext;
  private boolean isFloating;
  private boolean eligibleForCompetitionCreation = true;
  private ItemClickListener itemClickListener;
  private OnClickListener photoClickListener = new OnClickListener() {
    @Override
    public void onClick(View view) {
      hideFloatingButton();
      if (itemClickListener != null) {
        checkConnection();
        itemClickListener.onCreatePostButtonClicked();
      }
    }
  };
  private OnClickListener articleClickListener = new OnClickListener() {
    @Override
    public void onClick(View view) {
      hideFloatingButton();
      if (itemClickListener != null) {
        checkConnection();
        itemClickListener.onCreateArticleButtonClicked();
      }
    }
  };

  private OnClickListener competitionClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      hideFloatingButton();
      if (itemClickListener != null) {
        checkConnection();
        itemClickListener.onCompetitionButtonClicked();
      }
    }
  };

  public CreateNewButtonView(@NonNull Context context) {
    super(context);
    init(context);
  }

  public CreateNewButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);

  }

  public CreateNewButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View v = LayoutInflater.from(context).inflate(R.layout.create_new_button_view, this);
    addBlogBtn = v.findViewById(R.id.blog_btn);
    addPhotoBtn = v.findViewById(R.id.photo_btn);
    competitionBtn = v.findViewById(R.id.competition_btn);
    setCompetitionCreationEligibility(HaprampPreferenceManager.getInstance().isEligibleForCompetitionCreation());
    addPhotoBtn.setClickable(false);
    addBlogBtn.setClickable(false);
    competitionBtn.setClickable(false);
    plusBtn = v.findViewById(R.id.plusBtn);
    overlay = v.findViewById(R.id.overlay);
    root = v.findViewById(R.id.root);
    plusBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    plusBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        AnalyticsUtil.logEvent(AnalyticsParams.EVENT_CLICKS_CREATE_BUTTON);
        setCompetitionCreationEligibility(HaprampPreferenceManager.getInstance().isEligibleForCompetitionCreation());
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
  }



  private void setCompetitionCreationEligibility(boolean eligiblity){
    eligiblity = true;
    eligibleForCompetitionCreation = eligiblity;
    if(eligiblity) {
      competitionBtn.setVisibility(VISIBLE);
      competitionButtonTranslationY = 24;//base
      postButtonTranslationY = 36;// + 12
      blogButtonTranslationY = 48;// + 12
    }else{
      competitionBtn.setVisibility(GONE);
      postButtonTranslationY = 24;//base
      blogButtonTranslationY = 36;// + 12
    }
  }

  private void hideCompetitionButton() {
    competitionBtn.setClickable(false);
    competitionBtn.setVisibility(GONE);
    competitionBtn.setOnClickListener(null);
    competitionBtn.animate()
      .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
      .alpha(0)
      .translationY(0)
      .setDuration(FLOATING_BUTTON_DELAY)
      .start();
  }

  private void hideFloatingButton() {
    hideOverlay();
    hideCompetitionButton();
    root.setClickable(false);
    addPhotoBtn.setClickable(false);
    addBlogBtn.setClickable(false);
    addPhotoBtn.setVisibility(GONE);
    addBlogBtn.setVisibility(GONE);
    addBlogBtn.setOnClickListener(null);
    addPhotoBtn.setOnClickListener(null);
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

  private int getShiftAmount(int dp) {
    return dpToPx(dp);
  }

  public int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }

  private void showCompetitionButton() {
    //todo: enabled all users to create contest
    if (eligibleForCompetitionCreation) {
      competitionBtn.setClickable(true);
      competitionBtn.setVisibility(VISIBLE);
      competitionBtn.setOnClickListener(competitionClickListener);
      competitionBtn.animate()
        .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
        .alpha(1)
        .translationY(-(getShiftAmount(competitionButtonTranslationY)))
        .setDuration(FLOATING_BUTTON_DELAY)
        .start();
    }
  }

  private void showFloatingButtons() {
    showOverlay();
    showCompetitionButton();
    root.setClickable(true);
    addPhotoBtn.setClickable(true);
    addBlogBtn.setClickable(true);
    addBlogBtn.setVisibility(VISIBLE);
    addPhotoBtn.setVisibility(VISIBLE);
    addBlogBtn.setOnClickListener(articleClickListener);
    addPhotoBtn.setOnClickListener(photoClickListener);

    plusBtn.animate()
      .setInterpolator(new OvershootInterpolator(ADD_BUTTON_OVERSHOOT_TENSION))
      .rotation(ADD_BUTTON_ROTATION_ANGLE)
      .setDuration(ADD_BUTTON_ROTATION_DELAY)
      .start();

    addPhotoBtn.animate()
      .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
      .translationY(-(getShiftAmount(postButtonTranslationY)))
      .alpha(1)
      .setDuration(FLOATING_BUTTON_DELAY)
      .start();

    addBlogBtn.animate()
      .setInterpolator(new OvershootInterpolator(FLOATING_BUTTONOVERSHOOT_TENSION))
      .alpha(1)
      .translationY(-(getShiftAmount(blogButtonTranslationY)))
      .setDuration(FLOATING_BUTTON_DELAY)
      .start();

    overlay.animate().alpha(1).setDuration(FLOATING_BUTTON_DELAY).start();
    overlay.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        hideFloatingButton();
      }
    });
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

  private void revertOverlayReveal() {
    overlay.setVisibility(View.GONE);
  }

  private void revealOverlay() {
    overlay.setVisibility(View.VISIBLE);
  }

  public void setItemClickListener(ItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  public interface ItemClickListener {
    void onCreateArticleButtonClicked();

    void onCreatePostButtonClicked();

    void onCompetitionButtonClicked();
  }
}
