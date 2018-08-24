package com.hapramp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.hapramp.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class TextViewImageGetter implements Html.ImageGetter {
  private Context context;
  private TextView textView;

  public TextViewImageGetter(Context context, TextView textView) {
    this.textView = textView;
    this.context = context;
  }

  @Override
  public Drawable getDrawable(String source) {
    LevelListDrawable d = new LevelListDrawable();
    Drawable empty = context.getResources().getDrawable(R.drawable.hapcoin_icon_bg);
    d.addLevel(0, 0, empty);
    d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
    new LoadImage().execute(source, d);
    return d;
  }

  class LoadImage extends AsyncTask<Object, Void, Bitmap> {
    private LevelListDrawable mDrawable;

    @Override
    protected Bitmap doInBackground(Object... params) {
      String source = (String) params[0];
      mDrawable = (LevelListDrawable) params[1];
      try {
        InputStream is = new URL(source).openStream();
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 540, (height * 1080) / (2*width), false);
        return newBitmap;
      }
      catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      catch (MalformedURLException e) {
        e.printStackTrace();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      if (bitmap != null) {
        BitmapDrawable d = new BitmapDrawable(bitmap);
        mDrawable.addLevel(1, 1, d);
        mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDrawable.setLevel(1);
        CharSequence t = textView.getText();
        textView.setText(t);
      }
    }
  }
}
