package com.hapramp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;


/**
 * Created by Ankit on 12/17/2017.
 */

public class ImageHandler {
  static int imageWidth;

  public static void load(Context context, ImageView target, String _uri) {
    imageWidth = HaprampPreferenceManager
      .getInstance()
      .getDeviceWidth() / HaprampPreferenceManager
      .getInstance()
      .getImageDowngradeFactor();
    String final_url = "https://steemitimages.com/" + imageWidth + "x0/" + _uri;
    NetworkQualityUtils.startNetworkSampling();
    Glide.with(context)
      .load(final_url)
      .diskCacheStrategy(DiskCacheStrategy.RESULT)
      .listener(new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
          return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
          GlideDrawableImageViewTarget glideTarget = (GlideDrawableImageViewTarget) target;
          ImageView iv = glideTarget.getView();
          NetworkQualityUtils.stopstartNetworkSampling();
          int width = iv.getMeasuredWidth();
          int targetHeight = width * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
          if (iv.getLayoutParams().height != targetHeight) {
            iv.getLayoutParams().height = targetHeight;
            iv.requestLayout();
          }
          return false;
        }
      })
      .skipMemoryCache(false)
      .into(target);
  }

  public static void loadFilePath(Context context, ImageView target, String filePath) {
    Glide.with(context)
      .load(filePath)
      .diskCacheStrategy(DiskCacheStrategy.RESULT)
      .listener(new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
          return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
          GlideDrawableImageViewTarget glideTarget = (GlideDrawableImageViewTarget) target;
          ImageView iv = glideTarget.getView();
          int width = iv.getMeasuredWidth();
          int targetHeight = width * resource.getIntrinsicHeight() / resource.getIntrinsicWidth();
          if (iv.getLayoutParams().height != targetHeight) {
            iv.getLayoutParams().height = targetHeight;
            iv.requestLayout();
          }
          return false;
        }
      })
      .skipMemoryCache(false)
      .into(target);
  }

  public static void loadCircularImage(final Context context, final ImageView imageView, String url) {
    try {
      NetworkQualityUtils.startNetworkSampling();
      Glide.with(context)
        .load(url)
        .asBitmap()
        .placeholder(R.drawable.profile)
        .centerCrop()
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .into(new BitmapImageViewTarget(imageView) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            NetworkQualityUtils.stopstartNetworkSampling();
            imageView.setImageDrawable(circularBitmapDrawable);
          }
        });
    }
    catch (IllegalArgumentException e) {
    }
  }

}
