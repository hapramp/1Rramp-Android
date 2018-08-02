package com.hapramp.views.editor;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.hapramp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 1/30/2018.
 */

public class LinkInsertDialog extends Dialog {

  private final Context context;
  @BindView(R.id.link_lbl)
  TextView linkLbl;
  @BindView(R.id.link_text)
  EditText linkText;
  @BindView(R.id.url_text)
  EditText urlText;
  @BindView(R.id.cancelBtn)
  TextView cancelBtn;
  @BindView(R.id.addLinkBtn)
  TextView addLinkBtn;
  private OnLinkInsertedListener onLinkInsertedListener;

  public LinkInsertDialog(@NonNull Context context) {
    super(context);
    this.context = context;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.insert_link_dialog);
    ButterKnife.bind(this);

    addLinkBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String url = urlText.getText().toString().trim();
        String text = linkText.getText().toString().trim();
        if (url.length() > 0 && onLinkInsertedListener != null) {
          onLinkInsertedListener.onLinkAdded(text, url);
        }
        dismiss();
      }
    });

    cancelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
  }

  public void setOnLinkInsertedListener(OnLinkInsertedListener onLinkInsertedListener) {
    this.onLinkInsertedListener = onLinkInsertedListener;
  }

  public interface OnLinkInsertedListener {
    void onLinkAdded(String text, String link);
  }

}
