package com.hapramp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GoogleImageFilePathReader {
  public static String getImageFilePath(Context context, Intent data) {
    Uri uri = data.getData();
    if (uri != null)
      if (!uri.toString().contains("content://com.google.android.apps.photos")) {
        return FilePathUtils.getPath(context, uri);
      }
    try {
      InputStream inputStream = null;
      OutputStream output = null;
      try {
        inputStream = context.getContentResolver().openInputStream(uri);
        String filename = System.currentTimeMillis() + "_image.png";
        File file = new File(context.getCacheDir(), filename);
        output = new FileOutputStream(file);
        if (inputStream != null) {
          byte[] buffer = new byte[4 * 1024]; // or other buffer size
          int read;
          while ((read = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, read);
          }
          output.flush();
          return file.getPath();
        } else {
          Log.e("GooglePhotosError", "Error while reading selected image");
        }
      }
      finally {
        if (output != null) output.close();
        if (inputStream != null) inputStream.close();
      }
    }
    catch (FileNotFoundException e) {
      Log.e("GooglePhotosError", "FileNotFoundException - Error while reading selected image");
    }
    catch (IOException e) {
      Log.e("GooglePhotosError", "IOException - Error while reading selected image");
    }
    return "";
  }
}
