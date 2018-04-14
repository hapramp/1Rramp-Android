package com.hapramp.views.types;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.hapramp.R;

/**
 * Created by Ankit on 4/9/2018.
 */

public class ImageTypeView extends FrameLayout {

    public static final String TAG = ImageTypeView.class.getSimpleName();

    private ImageView image;
    private Context mContext;
    private TextView desc;

    public ImageTypeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ImageTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        View v = LayoutInflater.from(context).inflate(R.layout.image_type_view, this);
        image = v.findViewById(R.id.image);
        desc = v.findViewById(R.id.image_desc);
    }

    public void setImageInfo(String info) {

        String[] __info = info.split("\n");
        String image_url = "";
        String des = "";
        // data is in format : <desc>\n<image_url>
        if (__info.length > 1) {
            // desc was included
            //[desc,url]
            des = __info[0];
            image_url = __info[1];
        } else {
            //desc was not included
            //[url]
            image_url = __info[0];
        }


        setImageDesc(des);
        setImageSource(image_url);
    }

    private void setImageDesc(String des) {
        desc.setText(des);
    }

    private void setImageSource(String url) {

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.article_images_placeholder)
                .transform(new Delay(500))
                .animate(R.anim.image_fade_in)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(image);

    }

    class Delay extends UnitTransformation {
        private final int sleepTime;

        public Delay(int sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public Resource transform(Resource resource, int outWidth, int outHeight) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
            }
            return super.transform(resource, outWidth, outHeight);
        }

    }
}
