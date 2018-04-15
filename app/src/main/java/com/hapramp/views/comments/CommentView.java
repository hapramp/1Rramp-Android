package com.hapramp.views.comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.steem.SteemCommentModel;
import com.hapramp.utils.FontManager;
import com.hapramp.utils.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 2/8/2018.
 */

public class CommentView extends FrameLayout {

    @BindView(R.id.commentAvatar)
    ImageView commentAvatar;
    @BindView(R.id.commentMetaHolder)
    LinearLayout commentMetaHolder;
    @BindView(R.id.commentTv)
    TextView commentTv;
    @BindView(R.id.popupMenuDots)
    TextView popupMenuDots;
    @BindView(R.id.commentOwnerName)
    TextView commentOwnerName;
    private Context mContext;

    public CommentView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CommentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.comment_view_band, this);
        ButterKnife.bind(this, view);
        popupMenuDots.setTypeface(FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL));

    }

    public void setComment(SteemCommentModel result) {

        ImageHandler.loadCircularImage(mContext, commentAvatar, result.getCommentAuthorImageUri());
        commentOwnerName.setText(result.getCommentAuthor());
        commentTv.setText(result.getComment());

    }
}
