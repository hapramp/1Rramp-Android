package com.hapramp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hapramp.R;
import com.hapramp.editor.EditorCore;
import com.hapramp.editor.models.EditorContent;
import com.hapramp.editor.models.Node;
import com.hapramp.steem.models.data.FeedDataItemModel;
import com.hapramp.views.editor.EditorView;
import com.hapramp.views.editor.FeaturedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.getContentBtn)
    Button getContentBtn;
    @BindView(R.id.editorView)
    EditorView editorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        testContent();
    }

    private void testContent() {

        getContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               ArrayList<FeedDataItemModel> feedDataItemModels = editorView.getDataItemList();
               Intent i = new Intent(TestActivity.this,PreviewActivity.class);
               i.putParcelableArrayListExtra("data",feedDataItemModels);
               startActivity(i);
//                for (int i = 0; i < feedDataItemModels.size(); i++) {
//                    Log.d("TestActivity", feedDataItemModels.get(i).toString());
//                }
            }
        });

    }

}
