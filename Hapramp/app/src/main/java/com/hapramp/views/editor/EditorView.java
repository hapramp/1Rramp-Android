package com.hapramp.views.editor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.EditorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 12/31/2017.
 */

public class EditorView extends FrameLayout {


    @BindView(R.id.editor)
    EditText editor;
    @BindView(R.id.textSizeBtn)
    TextSizeView textSizeBtn;
    @BindView(R.id.bottomeControlsContainer)
    LinearLayout bottomeControlsContainer;
    @BindView(R.id.boldButton)
    BoldButtonView boldButton;
    @BindView(R.id.characterLimit)
    TextView characterLimit;

    private String actualContent;
    private String formattedContent;

    private int startIndex;
    private int endIndex;

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

        View view = LayoutInflater.from(context).inflate(R.layout.editor_view, this);
        ButterKnife.bind(this, view);

        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (before < count) {
                    if (charSequence.charAt(start + count - 1) == '\n') {
                        startIndex = start + count - 1;
                    }
                    endIndex = charSequence.length();

                    if (startIndex < endIndex)
                        getSpanned(editor, startIndex, endIndex);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TWW", getFormattedContent());
            }
        });
    }

    private void getSpanned(EditText et, int start, int end) {

        textSizeBtn.setTarget(et, start, end);
        boldButton.setTarget(et, start, end);

        if (textSizeBtn.isActive()) {
            // remove previous span
            AbsoluteSizeSpan[] spanToRemove = et.getText().getSpans(start, end, AbsoluteSizeSpan.class);

            for (AbsoluteSizeSpan aSpanToRemove : spanToRemove)
                et.getText().removeSpan(aSpanToRemove);

            et.getText().setSpan(textSizeBtn.getSizeSpan(), start, end, 0);

        }

        if (boldButton.isBoldActive()) {
            // remove previous span
            StyleSpan[] spanToRemove = et.getText().getSpans(start, end, StyleSpan.class);
//
//            for (StyleSpan aSpanToRemove : spanToRemove)
//                et.getText().removeSpan(aSpanToRemove);

            et.getText().setSpan(boldButton.getBoldSpan(), start, end, 0);
        }
//
//        if (blockquote.isQuoteActive()) {
//            try {
//                // remove previous span
//                BlockQuoteView.CustomQuoteSpan[] spanToRemove = et.getText().getSpans(start, end, BlockQuoteView.CustomQuoteSpan.class);
//
//                for (BlockQuoteView.CustomQuoteSpan aSpanToRemove : spanToRemove)
//                    et.getText().removeSpan(aSpanToRemove);
//
//                et.getText().setSpan(blockquote.getQuoteSpan(), start, end,0);
//
//            } catch (Exception e) {
//
//            }
//        }


    }

    public String getFormattedContent() {
        return EditorUtils.getHtml(editor);
    }

    public String getActualContent() {
        return actualContent;
    }


}
