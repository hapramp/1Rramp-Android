package com.hapramp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.hapramp.R;
import com.hapramp.services.PostService;
import com.hapramp.views.editor.EditorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.et)
    EditorView et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

//        et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                Log.d("TWW","Text:"+charSequence.toString()+" Start:"+start+" Before:"+before+" Count:"+count);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

//        et.setOnSelectionChangeLister(new EditorView.OnSelectionChangeLister() {
//            @Override
//            public void onSelectionChanged(int start, int end) {
//                handleSelectionChange(start,end);
//            }
//        });


//    private void handleSelectionChange(int start, int end) {
//
//        // get all spans in this range
//        AbsoluteSizeSpan[] sizeSpan = et.getText().getSpans(start,end,AbsoluteSizeSpan.class);
//        // consider first span
//        int startOfFirstSpan = et.getText().getSpanStart(sizeSpan[0]);
//        int endOfFirstSpan = et.getText().getSpanEnd(sizeSpan[0]);
//        textSizeView.setSpan(sizeSpan[0],et,startOfFirstSpan,endOfFirstSpan);
//
//    }

    }
}
