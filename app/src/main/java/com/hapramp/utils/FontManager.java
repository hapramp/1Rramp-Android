package com.hapramp.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.hapramp.main.HapRampMain;

public class FontManager {

  public static final String FONT_MATERIAL = "materialFont.ttf";
  public static final String FONT_LOBSTER = "lobster.ttf";
  public static final String FONT_RALEWAY = "raleway.ttf";
  public static final String FONT_ROBOTO = "roboto.ttf";
  public static final String FONT_SHRINKLAND = "shrinkland.ttf";

  private static Context context;
  private static FontManager mInstance;

  public FontManager() {
    FontManager.context = HapRampMain.getContext();
  }

  public static FontManager getInstance() {
    if (mInstance == null) {
      mInstance = new FontManager();
    }
    return mInstance;
  }

  public Typeface getTypeFace(String type) {
    return Typeface.createFromAsset(context.getAssets(), type);
  }

}
