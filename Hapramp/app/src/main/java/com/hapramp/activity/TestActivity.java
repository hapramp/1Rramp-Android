package com.hapramp.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.utils.EditorUtils;
import com.hapramp.views.EditorView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class TestActivity extends AppCompatActivity {


    @BindView(R.id.et)
    EditorView et;
    @BindView(R.id.bold)
    CheckBox bold;
    @BindView(R.id.bigger)
    CheckBox bigger;
    @BindView(R.id.color)
    CheckBox color;
    @BindView(R.id.panel)
    TextView panel;

    private int lastStart;
    private int lastEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                getSpanned(et, start, start + count);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    et.getText().setSpan(new StyleSpan(Typeface.BOLD),
                            lastStart,
                            lastEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        });


    }

    private void getSpanned(EditText editText, int start, int end) {

        lastStart = start;
        lastEnd = end;

        if (color.isChecked()) {
            editText.getText().setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                    start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (bold.isChecked()) {
            editText.getText().setSpan(new StyleSpan(Typeface.BOLD),
                    start,
                    end, SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (bigger.isChecked()) {
            editText.getText().setSpan(new AbsoluteSizeSpan(50), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }


    public void getHtml(View view) {

        panel.setText(EditorUtils.getHtml(et));

    }
}
