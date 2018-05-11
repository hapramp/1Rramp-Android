package com.hapramp.editor;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Ankit on 4/13/2018.
 */

public class FontFace {

    private static int fontFace = R.string.fontFamily__sans_serif_medium;

    public static Typeface getNormalTypeface(Context context){
        return Typeface.create(getFontFace(context), Typeface.NORMAL);
    }

    public static Typeface getBoldTypeface(Context context){
        return Typeface.create(getFontFace(context), Typeface.BOLD);
    }

    private static String getFontFace(Context context) {
        return context.getResources().getString(fontFace);
    }

}
