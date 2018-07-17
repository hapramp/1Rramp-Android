package com.hapramp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.hapramp.main.HapRampMain;

/**
 * Created by Ankit on 1/1/2018.
 */

public class PixelUtils {

  private static String height;

  public static int pxToDp(int px) {
    return (int) (px / Resources.getSystem().getDisplayMetrics().density);
  }

  public static int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }

  public static int getWidth() {

    WindowManager wm = (WindowManager) HapRampMain.getContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.x / 2;

  }

  public static DisplayMetrics getDimension(Context context) {

    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return metrics;
  }

  public static int getHeight() {

    WindowManager wm = (WindowManager) HapRampMain.getContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.y / 2;
  }
}
