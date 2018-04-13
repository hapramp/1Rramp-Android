package com.hapramp.editor.Utilities;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by IRSHU on 11/5/2017.
 */

public class FontCache {

    public static final String BOLD_ROBOTO = "RobotoCondensed-Bold.ttf";
    public static final String REGULAR_ROBOTO = "RobotoCondensed-Regular.ttf";
    public static final String LIGHT_ROBOTO = "RobotoCondensed-Light.ttf";

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}
