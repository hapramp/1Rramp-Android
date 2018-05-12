package com.hapramp.views.editor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.FontManager;
import com.hapramp.youtube.YoutubeVideoSelectorActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 1/28/2018.
 */
public class YoutubeInsertButtonView extends FrameLayout {

    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.divider_btn)
    TextView youtubeBtn;
    Context mContext;
    public static int YOUTUBE_RESULT_REQUEST = 102;


    public YoutubeInsertButtonView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public YoutubeInsertButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YoutubeInsertButtonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.paragraph_divider_view, this);
        ButterKnife.bind(this, view);
        youtubeBtn.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               startYoutubeSelectionActivity();
            }
        });

    }

    private void startYoutubeSelectionActivity() {
        Intent youtubeIntent = new Intent(mContext, YoutubeVideoSelectorActivity.class);
        ((AppCompatActivity) mContext).startActivityForResult(youtubeIntent, YOUTUBE_RESULT_REQUEST);
    }

}
