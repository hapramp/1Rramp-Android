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
 * Created by Ankit on 1/28/2018.
 */

public class BulletsView extends FrameLayout {

    public static final int ORDERED = 1;
    public static final int UNORDERED = 2;

    @BindView(R.id.bullet)
    TextView bullet;
    @BindView(R.id.container)
    RelativeLayout container;
    private int orderOfList = 2;

    public static final String ORDERED_LIST_ICON_TEXT = "\uF27B";
    public static final String UNORDERED_LIST_ICON_TEXT = "\uF279";


    public BulletsView(@NonNull Context context) {
        super(context);
        ini(context);
    }

    public BulletsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ini(context);
    }

    public BulletsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini(context);
    }

    private void ini(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.bullet_view, this);
        ButterKnife.bind(this, view);
        bullet.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                orderOfList = (orderOfList == ORDERED) ? UNORDERED : ORDERED;
                invalidateButton();
            }
        });

    }

    private void invalidateButton() {

        if (orderOfList == ORDERED) {
            bullet.setText(ORDERED_LIST_ICON_TEXT);
            if (bulletListener != null) {
                bulletListener.onList(true);
            }
        } else {
            bullet.setText(UNORDERED_LIST_ICON_TEXT);
            if (bulletListener != null) {
                bulletListener.onList(false);
            }
        }


    }

    private BulletListener bulletListener;

    public void setBulletListener(BulletListener bulletListener) {
        this.bulletListener = bulletListener;
    }

    public interface BulletListener {
        void onList(boolean isOrdered);
    }
}
