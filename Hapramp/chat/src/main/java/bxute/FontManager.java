package bxute;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {

    public static final String FONT_MATERIAL = "materialFont.ttf";
    public static final String SENT_ICON = "\uF26B";
    public static final String DELIVERED_ICON = "\uF267";
    Context context;

    public FontManager(Context context) {
        this.context = context;
    }

    public Typeface getDefault(){
        return Typeface.createFromAsset(context.getAssets(),FONT_MATERIAL);
    }

}
