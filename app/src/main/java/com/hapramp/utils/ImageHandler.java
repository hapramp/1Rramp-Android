package com.hapramp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hapramp.R;

/**
 * Created by Ankit on 12/17/2017.
 */

public class ImageHandler {

    public static void load(Context context, ImageView target, String _uri) {

       try {

           target.layout(0,0,0,0);

            Glide.with(context)
                    .load(_uri)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(target);
        }catch (Exception e){

       }

    }

    public static void loadSmaller(Context context,ImageView imageView, String _uri){

       try {

            Glide.with(context)
                    .load(_uri)
                    .override(PixelUtils.dpToPx(72), PixelUtils.dpToPx(72))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

        }catch (IllegalArgumentException e){

       }

    }

    public static void loadCircularImage(final Context context, final ImageView imageView, String url) {

        try{

            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {

                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);

                        }
                    });

        }catch (IllegalArgumentException e){

        }

    }

    private static int getWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

}
