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

public class LinkView extends FrameLayout {

    @BindView(R.id.link_btn)
    TextView linkBtn;
    @BindView(R.id.container)
    RelativeLayout container;

    public LinkView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LinkView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinkView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.link_view, this);
        ButterKnife.bind(this, view);
        linkBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linkInsertListener != null) {
                    linkInsertListener.onInsertLink();
                }
            }
        });

    }

    private LinkInsertListener linkInsertListener;

    public void setLinkInsertListener(LinkInsertListener linkInsertListener) {
        this.linkInsertListener = linkInsertListener;
    }

    public interface LinkInsertListener {
        void onInsertLink();
    }

}
