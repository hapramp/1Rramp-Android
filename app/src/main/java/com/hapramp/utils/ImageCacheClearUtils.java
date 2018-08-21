package com.hapramp.utils;

import android.util.Log;

import java.io.File;

public class ImageCacheClearUtils {
  public static void deleteImage(final String path) {
    new Thread() {
      @Override
      public void run() {
        File file = new File(path);
        boolean deleted = file.delete();
        Log.d("ImageOrientation", "deleted !" + path);
      }
    }.start();
  }
}
