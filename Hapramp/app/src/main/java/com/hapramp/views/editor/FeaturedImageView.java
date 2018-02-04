package com.hapramp.views.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 2/3/2018.
 */

public class FeaturedImageView extends FrameLayout {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.selection_tick)
    TextView selectionTick;
    private Context mContext;
    private boolean isSelected;

    public FeaturedImageView(@NonNull Context context) {

        super(context);
        init(context);

    }

    public FeaturedImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public FeaturedImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {

        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.featured_image_selector_item_view, this);
        ButterKnife.bind(this, view);
        selectionTick.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));
    }

    public void setSelection(boolean isSelected) {

        this.isSelected = isSelected;

        if (isSelected) {
            selectionTick.setVisibility(VISIBLE);
        } else {
            selectionTick.setVisibility(GONE);
        }

    }

    public void setImageSource(String url) {
        ImageHandler.loadSmaller(mContext,image,url);
    }

}
