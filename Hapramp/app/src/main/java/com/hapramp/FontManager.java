package com.hapramp;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

    public static final String FONT_MATERIAL = "materialFont.ttf";

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
