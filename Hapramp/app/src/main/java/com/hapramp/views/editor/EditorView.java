package com.hapramp.views.editor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.github.irshulx.Editor;
import com.github.irshulx.models.EditorTextStyle;
import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Ankit on 12/31/2017.
 */

public class EditorView extends FrameLayout implements TextHeaderView.HeadingChangeListener, BoldButtonView.BoldTextListener, ItalicView.ItalicTextListener, BulletsView.BulletListener, LinkView.LinkInsertListener, DividerView.DividerListener, ImageInsertView.ImageInsertListener {


    @BindView(R.id.editor)
    Editor editor;
    @BindView(R.id.editorControlHolder)
    RelativeLayout editorControlHolder;
    @BindView(R.id.textHeaderView)
    TextHeaderView textHeaderView;
    @BindView(R.id.bold_text_control)
    BoldButtonView boldTextControl;
    @BindView(R.id.italic_text_control)
    ItalicView italicTextControl;
    @BindView(R.id.bullets_control)
    BulletsView bulletsControl;
    @BindView(R.id.link_view)
    LinkView linkView;
    @BindView(R.id.paragraph_divider_view)
    DividerView paragraphDividerView;
    @BindView(R.id.image_insertBtn)
    ImageInsertView imageInsertBtn;
    private Context mContext;
    private View view;

    public EditorView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public EditorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.editor_view, this);
        ButterKnife.bind(this, view);
        attachListeners();
        editor.render();

    }

    private void attachListeners() {
        textHeaderView.setHeadingChangeListener(this);
        boldTextControl.setBoldTextListener(this);
        italicTextControl.setItalicTextListener(this);
        bulletsControl.setBulletListener(this);
        linkView.setLinkInsertListener(this);
        paragraphDividerView.setDividerListener(this);
        imageInsertBtn.setInsertListener(this);
    }

    @Override
    public void onHeading1Active() {
        editor.updateTextStyle(EditorTextStyle.H1);
    }

    @Override
    public void onHeading2Active() {
        editor.updateTextStyle(EditorTextStyle.H2);
    }

    @Override
    public void onHeadingClear() {

        editor.updateTextStyle(EditorTextStyle.H1);
    }

    @Override
    public void onBoldText(boolean isBoldActive) {
        editor.updateTextStyle(EditorTextStyle.BOLD);
    }

    @Override
    public void onItalicText(boolean isActive) {
        editor.updateTextStyle(EditorTextStyle.ITALIC);
    }

    @Override
    public void onList(boolean isOrdered) {
        editor.insertList(isOrdered);
    }

    @Override
    public void onInsertLink() {
        editor.insertLink();
    }

    @Override
    public void onInsertDivider() {
        editor.insertDivider();
    }


    @Override
    public void onInsertImage() {
        editor.openImagePicker();
    }

    public Editor getEditor() {
        return editor;
    }
}
