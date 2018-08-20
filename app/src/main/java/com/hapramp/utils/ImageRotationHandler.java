package com.hapramp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageRotationHandler {
  private final Context mContext;
  private ImageRotationOperationListner imageRotationOperationListner;

  public ImageRotationHandler(Context context) {
    this.mContext = context;
  }

  public void checkOrientationAndFixImage(final String imagePath, final int uid) {
    new Thread() {
      @Override
      public void run() {
        ExifInterface ei = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        try {
          ei = new ExifInterface(imagePath);
          int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL);
          Bitmap rotatedBitmap = null;
          switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
              rotatedBitmap = rotateImage(bitmap, 90);
              saveRotatedBitampAndSendBackResults(rotatedBitmap, imagePath, uid);
              break;
            case ExifInterface.ORIENTATION_ROTATE_180:
              rotatedBitmap = rotateImage(bitmap, 180);
              saveRotatedBitampAndSendBackResults(rotatedBitmap, imagePath, uid);
              break;
            case ExifInterface.ORIENTATION_ROTATE_270:
              rotatedBitmap = rotateImage(bitmap, 270);
              saveRotatedBitampAndSendBackResults(rotatedBitmap, imagePath, uid);
              break;
            default:
              performCallback(imagePath, false, uid);
          }
        }
        catch (IOException e) {
          performCallback(imagePath, false, uid);
        }
      }
    }.start();
  }

  private Bitmap rotateImage(Bitmap source, float angle) {
    Bitmap bitmap = null;
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    try {
      bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
        matrix, true);
    }
    catch (OutOfMemoryError err) {
      err.printStackTrace();
      return null;
    }
    return bitmap;
  }

  private void saveRotatedBitampAndSendBackResults(Bitmap bitmap, String originalPath, int uid) {
    if (bitmap == null) {
      performCallback(originalPath, false, uid);
    } else {
      FileOutputStream out = null;
      try {
        File filename = new File(mContext.getCacheDir(), System.currentTimeMillis() + "_rotated_cache.png");
        out = new FileOutputStream(filename);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        performCallback(filename.getAbsolutePath(), true, uid);
      }
      catch (Exception e) {
        performCallback(originalPath, false, uid);
        e.printStackTrace();
      }
      finally {
        try {
          if (out != null) {
            out.close();
          }
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void performCallback(String filePath, boolean needsToBeCleaned, int uid) {
    if (imageRotationOperationListner != null) {
      imageRotationOperationListner.onImageRotationFixed(filePath, needsToBeCleaned, uid);
    }
  }

  public void setImageRotationOperationListner(ImageRotationOperationListner imageRotationOperationListner) {
    this.imageRotationOperationListner = imageRotationOperationListner;
  }

  public interface ImageRotationOperationListner {
    void onImageRotationFixed(String filePath, boolean fileShouldBeDeleted, int uid);
  }
}
