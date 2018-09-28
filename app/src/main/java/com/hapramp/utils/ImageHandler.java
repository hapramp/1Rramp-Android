package com.hapramp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.Crashlytics;
import com.hapramp.R;

/**
 * Created by Ankit on 12/17/2017.
 */
public class ImageHandler {

  public static void load(Context context, final ImageView imageView, String _uri) {
    String final_url = "https://steemitimages.com/0x0/" + _uri;
    //NetworkQualityUtils.startNetworkSampling();
    try {
      RequestOptions options = new RequestOptions()
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH);
      Glide.with(context)
        .load(final_url)
        .apply(options)
        .listener(new RequestListener<Drawable>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e,
                                      Object model,
                                      Target<Drawable> target,
                                      boolean isFirstResource) {
            Crashlytics.logException(e);
            return false;
          }

          @Override
          public boolean onResourceReady(Drawable resource,
                                         Object model,
                                         Target<Drawable> target,
                                         DataSource dataSource,
                                         boolean isFirstResource) {
            //NetworkQualityUtils.stopstartNetworkSampling();
            int width = imageView.getMeasuredWidth();
            int targetHeight = width * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
            if (imageView.getLayoutParams().height != targetHeight) {
              imageView.getLayoutParams().height = targetHeight;
              imageView.requestLayout();
            }
            imageView.setImageDrawable(resource);
            return true;
          }
        })
        .into(imageView);
    }
    catch (Exception e) {
      Crashlytics.logException(e);
    }
  }

  public static void loadUnOverridden(Context context, final ImageView imageView, String _uri) {
    String final_url = "https://steemitimages.com/0x0/" + _uri;
    try {
      RequestOptions options = new RequestOptions()
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH);
      Glide.with(context)
        .load(final_url)
        .apply(options)
        .listener(new RequestListener<Drawable>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e,
                                      Object model,
                                      Target<Drawable> target,
                                      boolean isFirstResource) {
            Crashlytics.logException(e);
            return false;
          }

          @Override
          public boolean onResourceReady(Drawable resource,
                                         Object model,
                                         Target<Drawable> target,
                                         DataSource dataSource,
                                         boolean isFirstResource) {
            imageView.setImageDrawable(resource);
            return true;
          }
        })
        .into(imageView);
    }
    catch (Exception e) {
      Crashlytics.logException(e);
    }
  }

  public static void loadFilePath(Context context, final ImageView imageView, String filePath) {
    try {
      RequestOptions options = new RequestOptions()
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .priority(Priority.HIGH);
      Glide.with(context)
        .load(filePath)
        .apply(options)
        .listener(new RequestListener<Drawable>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e,
                                      Object model,
                                      Target<Drawable> target,
                                      boolean isFirstResource) {
            Crashlytics.logException(e);
            return false;
          }

          @Override
          public boolean onResourceReady(Drawable resource,
                                         Object model,
                                         Target<Drawable> target,
                                         DataSource dataSource,
                                         boolean isFirstResource) {
            int width = imageView.getMeasuredWidth();
            int targetHeight = width * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
            if (imageView.getLayoutParams().height != targetHeight) {
              imageView.getLayoutParams().height = targetHeight;
              imageView.requestLayout();
            }
            imageView.setImageDrawable(resource);
            return true;
          }
        })
        .into(imageView);
    }
    catch (Exception e) {
      Crashlytics.logException(e);
    }
  }

  public static void loadCircularImage(final Context context, final ImageView imageView, String url) {
    try {
      RequestOptions options = new RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.circular_image_placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH);
      //NetworkQualityUtils.startNetworkSampling();
      Glide.with(context)
        .asBitmap()
        .load(url)
        .apply(options)
        .listener(new RequestListener<Bitmap>() {
          @Override
          public boolean onLoadFailed(@Nullable GlideException e,
                                      Object model,
                                      Target<Bitmap> target,
                                      boolean isFirstResource) {
            Crashlytics.logException(e);
            return false;
          }

          @Override
          public boolean onResourceReady(Bitmap resource,
                                         Object model,
                                         Target<Bitmap> target,
                                         DataSource dataSource,
                                         boolean isFirstResource) {
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
              .create(context.getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            //NetworkQualityUtils.stopstartNetworkSampling();
            imageView.setImageDrawable(circularBitmapDrawable);
            return true;
          }
        })
        .into(imageView);
    }
    catch (IllegalArgumentException e) {
      Crashlytics.logException(e);
    }
  }

}
