package com.hapramp.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hapramp.utils.FontManager;
import com.hapramp.R;
import com.hapramp.api.DataServer;
import com.hapramp.interfaces.CommentCreateCallback;
import com.hapramp.models.requests.CommentBody;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentEditorActivity extends AppCompatActivity implements CommentCreateCallback {

    @BindView(R.id.closeBtn)
    TextView closeBtn;
    @BindView(R.id.toolbar_container)
    RelativeLayout toolbarContainer;
    @BindView(R.id.inContextOf)
    TextView inContextOf;
    @BindView(R.id.commentEt)
    EditText commentEt;
    @BindView(R.id.commentPublishBtn)
    TextView commentPublishBtn;
    private Typeface typeface;
    private String postId = "";
    private ProgressDialog progressDialog;
    private String contextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_editor);
        ButterKnife.bind(this);
        init();

    }

    private void init() {

        progressDialog = new ProgressDialog(this);
        typeface = FontManager.getInstance().getTypeFace(FontManager.FONT_MATERIAL);
        closeBtn.setTypeface(typeface);
        contextText = getIntent().getExtras().getString("context");
        postId = getIntent().getExtras().getString("postId");
        inContextOf.setText(contextText.substring(0,10));
        commentPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void postComment(){

        String cmnt = commentEt.getText().toString().trim();
        if(cmnt.length()>2) {
            showProgress("Posting Your Comment..."+postId);
            DataServer.createComment(postId, new CommentBody(cmnt),this);
        }

    }

    @Override
    public void onCommentCreated() {

        hideProgress();
        finish();
        Toast.makeText(this,"Create Comment",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCommentCreateError() {

        hideProgress();
        finish();
        Toast.makeText(this,"Cannot Create Comment!",Toast.LENGTH_SHORT).show();

    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    private void hideProgress() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }


}
