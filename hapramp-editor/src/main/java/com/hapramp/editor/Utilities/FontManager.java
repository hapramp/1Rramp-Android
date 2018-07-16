package com.hapramp.editor.Utilities;

import android.content.Context;
import android.graphics.Typeface;


public class FontManager {

  public static final String FONT_MATERIAL = "materialFont.ttf";

  private static FontManager mInstance;

  public FontManager() {
  }

  public static FontManager getInstance() {
    if (mInstance == null) {
      mInstance = new FontManager();
    }
    return mInstance;
  }

  public Typeface getTypeFace(String type, Context context) {
    return Typeface.createFromAsset(context.getAssets(), type);
  }

}
