package com.hapramp.v;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hapramp.editormodule.R;
import com.hapramp.editormodule.utils.ImageHandler;

/**
 * Created by Ankit on 4/9/2018.
 */

public class ImageTypeView extends FrameLayout {

    private ImageView image;
    private Context mContext;

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
    }


    private void init(Context context) {
        this.mContext = context;
        View v = LayoutInflater.from(context).inflate(R.layout.image_type_view, this);
        image = v.findViewById(R.id.content);
        init(context);
    }

    public void setImageSource(String url) {
        ImageHandler.load(mContext, image, url);
    }

}
