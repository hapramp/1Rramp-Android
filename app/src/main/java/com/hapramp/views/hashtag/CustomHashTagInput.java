package com.hapramp.views.hashtag;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hapramp.R;
import com.hapramp.utils.HashTagUtils;
import com.hapramp.views.post.WrapViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/22/2018.
 */

public class CustomHashTagInput extends FrameLayout implements ChipView.RemoveTagListener {

  @BindView(R.id.tag_input)
  EditText tagInput;
  @BindView(R.id.chips_container)
  WrapViewGroup chipsContainer;
  private Context context;
  private ArrayList<String> hashTags = new ArrayList<>();

  public CustomHashTagInput(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    View view = LayoutInflater.from(context).inflate(R.layout.custom_hashtag_layout, this);
    ButterKnife.bind(this, view);
    (chipsContainer).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
    attachListener();
  }

  private void attachListener() {
    tagInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (before < count) {
          int len = s.length();
          if (len > 0) {
            if (s.charAt(len - 1) == ' ' || s.charAt(len - 1) == '\n') {
              //reset text
              String tagToAdd = s.toString().trim();
              if (hashTags.indexOf(tagToAdd) == -1) {
                if (tagToAdd.length() > 0) {
                  tagInput.setText("");
                  addChip(tagToAdd);
                }
              } else {
                tagInput.setText("");
                Toast.makeText(context, "Already Added", Toast.LENGTH_LONG).show();
              }
            }
          }
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  private void addChip(String text) {
    text = "#" + text;
    if (!HashTagUtils.isValidHashTag(text)) {
      Toast.makeText(context, "Invalid HashTag", Toast.LENGTH_SHORT).show();
      return;
    }
    int size = hashTags.size();
    text = HashTagUtils.reformatHashTag(text);
    hashTags.add(text);
    ChipView chipView = new ChipView(context);
    chipView.setRemoveTagListener(this);
    chipView.setTagText(text, size);
    chipsContainer.addView(chipView, size,
      new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT));
  }

  public CustomHashTagInput(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CustomHashTagInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  @Override
  public void onRemove(View view, String tag) {
    //remove from list
    hashTags.remove(tag);
    //remove from view
    chipsContainer.removeView(view);
  }

  public ArrayList<String> getHashTags() {
    return hashTags;
  }
}
