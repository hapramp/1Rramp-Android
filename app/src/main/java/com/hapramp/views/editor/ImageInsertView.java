package com.hapramp.views.editor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 1/28/2018.
 */

public class ImageInsertView extends FrameLayout {


    @BindView(R.id.image_btn)
    TextView imageBtn;
    @BindView(R.id.container)
    RelativeLayout container;

    public ImageInsertView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ImageInsertView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageInsertView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.article_image_insert_view, this);
        ButterKnife.bind(this, view);
        imageBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (insertListener != null) {
                    insertListener.onInsertImage();
                }
            }
        });
    }

    private ImageInsertListener insertListener;

    public void setInsertListener(ImageInsertListener insertListener) {
        this.insertListener = insertListener;
    }

    public interface ImageInsertListener {
        void onInsertImage();
    }

}
