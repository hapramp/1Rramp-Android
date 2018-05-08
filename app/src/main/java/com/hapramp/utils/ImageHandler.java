package com.hapramp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
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

import java.io.ByteArrayOutputStream;


/**
 * Created by Ankit on 12/17/2017.
 */

public class ImageHandler {

    public static void load(Context context, ImageView target, String _uri) {

       Glide.with(context)
               .load(_uri)
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
                        if(iv.getLayoutParams().height != targetHeight) {
                            iv.getLayoutParams().height = targetHeight;
                            iv.requestLayout();
                        }
                        return false;
                   }
               })
               .diskCacheStrategy(DiskCacheStrategy.RESULT)
               .into(target);

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

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize + 1;
    }

    public static Bitmap decodeSampledBitmap(Bitmap bitmap) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapdata,0,bitmapdata.length,options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        Log.d("DIMENSION","height "+options.outHeight);
        Log.d("DIMENSION","width "+options.outWidth);

        int deviceWidth = PixelUtils.getWidth();
        int reqWidth = deviceWidth;
        int reqHeight = (imageHeight*deviceWidth)/imageWidth;
        // Calculate inSampleSize
        Log.d("DIMENSION","req height "+options.outHeight);
        Log.d("DIMENSION","req width "+options.outWidth);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap sampledBitmap = BitmapFactory.decodeByteArray(bitmapdata,0,bitmapdata.length,options);
        Log.d("DIMENSION","[d] height "+options.outHeight);
        Log.d("DIMENSION","[d] width "+options.outWidth);

        return sampledBitmap;

    }

}
