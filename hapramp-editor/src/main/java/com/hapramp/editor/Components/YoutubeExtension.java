package com.hapramp.editor.Components;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hapramp.editor.EditorCore;
import com.hapramp.editor.R;
import com.hapramp.editor.models.EditorControl;
import com.hapramp.editor.models.EditorType;

/**
 * Created by Ankit on 4/17/2018.
 */

public class YoutubeExtension {

  private EditorCore editorCore;
  private int editorYoutubeLayout = R.layout.tmpl_youtube_view;

  public YoutubeExtension(EditorCore editorCore) {
    this.editorCore = editorCore;
  }

  public void insertYoutubeVideo(String videoId) {

    insertYoutubeVideoThumbnail(videoId, -1);

  }

  private void insertYoutubeVideoThumbnail(String videoKey, int index) {

    String imageUrl = getYoutubeThumbnailUrl(videoKey);
    final View childLayout = ((Activity) editorCore.getContext()).getLayoutInflater().inflate(this.editorYoutubeLayout, null);
    ImageView imageView = childLayout.findViewById(R.id.youtube_thumbnailIv);
    loadImage(imageView, imageUrl);
    if (index == -1) {
      index = editorCore.determineIndex(EditorType.ytb);
    }

    showNextInputHint(index);
    editorCore.getParentView().addView(childLayout, index);

    //      _Views.add(childLayout);
    if (editorCore.isLastRow(childLayout)) {
      editorCore.getInputExtensions().insertEditText(index + 1, null, null);
    }

    //set video key
    if (!TextUtils.isEmpty(videoKey)) {
      EditorControl control = editorCore.createTag(EditorType.ytb);
      control.path = videoKey;
      childLayout.setTag(control);
    }

    BindEvents(childLayout);

  }

  private String getYoutubeThumbnailUrl(String videoKey) {
    return String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", videoKey);
  }

  /*
    /used by the renderer to render the image from the Node
  */
  public void loadImage(ImageView target, String imageUrl) {

    Glide.with(this.editorCore.getContext())
      .load(imageUrl)
      .diskCacheStrategy(DiskCacheStrategy.RESULT)
      .into(target);


  }

  private void showNextInputHint(int index) {
    View view = editorCore.getParentView().getChildAt(index);
    EditorType type = editorCore.getControlType(view);
    if (type != EditorType.INPUT)
      return;
    TextView tv = (TextView) view;
    tv.setHint(editorCore.placeHolder);
  }

  private void BindEvents(final View layout) {

    final ImageView imageView = layout.findViewById(R.id.youtube_thumbnailIv);
    final View btn_remove = layout.findViewById(R.id.btn_remove);

    btn_remove.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int index = editorCore.getParentView().indexOfChild(layout);
        editorCore.getParentView().removeView(layout);
        hideInputHint(index);
        editorCore.getInputExtensions().setFocusToPrevious(index);
      }
    });

    imageView.setOnTouchListener(new View.OnTouchListener() {
      private Rect rect;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          imageView.setColorFilter(Color.argb(50, 0, 0, 0));
          rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
          imageView.setColorFilter(Color.argb(0, 0, 0, 0));
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
          if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
            imageView.setColorFilter(Color.argb(0, 0, 0, 0));
          }
        }
        return false;
      }
    });

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        btn_remove.setVisibility(View.VISIBLE);
      }
    });
    imageView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        btn_remove.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
      }
    });
  }

  private void hideInputHint(int index) {
    View view = editorCore.getParentView().getChildAt(index);
    EditorType type = editorCore.getControlType(view);
    if (type != EditorType.INPUT)
      return;

    String hint = editorCore.placeHolder;
    if (index > 0) {
      View prevView = editorCore.getParentView().getChildAt(index - 1);
      EditorType prevType = editorCore.getControlType(prevView);
      if (prevType == EditorType.INPUT)
        hint = null;
    }
    TextView tv = (TextView) view;
    tv.setHint(hint);
  }

}
