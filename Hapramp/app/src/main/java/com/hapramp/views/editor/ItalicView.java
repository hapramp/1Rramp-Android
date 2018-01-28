package com.hapramp.views.editor;

import android.content.Context;
import android.content.res.Resources;
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
 * Created by Ankit on 1/2/2018.
 */

public class ItalicView extends FrameLayout {

    @BindView(R.id.italic_button)
    TextView italic;
    @BindView(R.id.container)
    RelativeLayout container;
    private boolean isEnabled;

    public ItalicView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ItalicView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItalicView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.italic_view, this);
        ButterKnife.bind(this, view);
        italic.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                isEnabled = !isEnabled;
                invalidateButton();
            }
        });


    }

    private void invalidateButton() {
//        if (isEnabled) {
//            italic.setTextColor(colorActive);
//        } else {
//            italic.setTextColor(colorInactive);
//        }

        if(italicTextListener!=null){
            italicTextListener.onItalicText(isEnabled);
        }
    }

    private ItalicTextListener italicTextListener;

    public void setItalicTextListener(ItalicTextListener italicTextListener) {
        this.italicTextListener = italicTextListener;
    }

    public interface ItalicTextListener{
        void onItalicText(boolean isActive);
    }

}
