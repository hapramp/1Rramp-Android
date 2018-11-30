package com.hapramp.views.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hapramp.R;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.utils.HashTagUtils;
import com.hapramp.utils.ImageHandler;
import com.hapramp.views.CommunityStripView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/1/2018.
 */

public class PostCreateComponent extends FrameLayout implements PostCommunityView.CommunitySelectionChangeListener {

  @BindView(R.id.feed_owner_pic)
  ImageView feedOwnerPic;
  @BindView(R.id.reference_line)
  Space referenceLine;
  @BindView(R.id.feed_owner_title)
  TextView feedOwnerTitle;
  @BindView(R.id.feed_owner_subtitle)
  TextView feedOwnerSubtitle;
  @BindView(R.id.post_header_container)
  RelativeLayout postHeaderContainer;
  @BindView(R.id.image_selector)
  ImageView imageSelector;
  @BindView(R.id.central_divider)
  FrameLayout centralDivider;
  @BindView(R.id.camera_capture)
  ImageView cameraCapture;
  @BindView(R.id.placeholder)
  LinearLayout placeholder;
  @BindView(R.id.postImageView)
  PostImageView postImageView;
  @BindView(R.id.youtube_thumbnailIv)
  ImageView youtubeThumbnailIv;
  @BindView(R.id.youtube_indicator)
  ImageView youtubeIndicator;
  @BindView(R.id.btn_remove)
  TextView btnRemove;
  @BindView(R.id.youtube_item_container)
  RelativeLayout cameraItemContainer;
  @BindView(R.id.content)
  EditText content;
  @BindView(R.id.inline_category_caption)
  TextView inlineCategoryCaption;
  @BindView(R.id.inline_postCategoryView)
  PostCommunityView inlinePostCommunityView;
  @BindView(R.id.inline_community_selector_container)
  RelativeLayout inlineCommunitySelectorContainer;
  @BindView(R.id.inline_hashtags)
  TextView inlineHashtags;
  @BindView(R.id.hashtags_container)
  RelativeLayout hashtagsContainer;
  @BindView(R.id.community_stripe_view)
  CommunityStripView communityStripeView;
  private Context mContext;
  private boolean mediaSelected = false;
  private MediaSelectorListener mediaSelectorListener;
  private String defaultText;

  public PostCreateComponent(@NonNull Context context) {
    super(context);
    init(context);
  }

  private void init(Context context) {
    this.mContext = context;
    View view = LayoutInflater.from(context).inflate(R.layout.post_create_layout, this);
    ButterKnife.bind(this, view);
    inlinePostCommunityView.initCategory();
    inlinePostCommunityView.setCommunitySelectionChangeListener(this);
    String pic_url = String.format(mContext.getResources().getString(R.string.steem_user_profile_pic_format),
      HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    ImageHandler.loadCircularImage(context, feedOwnerPic, pic_url);
    feedOwnerTitle.setText(HaprampPreferenceManager.getInstance().getCurrentSteemUsername());
    attachTextListeners();
    postImageView.setImageActionListener(new PostImageView.ImageActionListener() {
      @Override
      public void onImageRemoved() {
        placeholder.setVisibility(VISIBLE);
        mediaSelected = false;
      }

      @Override
      public void onImageUploaded(String downloadUrl) {

      }
    });

    btnRemove.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mediaSelected = false;
        cameraItemContainer.setVisibility(GONE);
        placeholder.setVisibility(VISIBLE);
      }
    });

    imageSelector.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mediaSelectorListener != null) {
          mediaSelectorListener.onImageInsertOptionSelected();
        }
      }
    });

    cameraCapture.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mediaSelectorListener != null) {
          mediaSelectorListener.onCameraImageSelected();
        }
      }
    });
  }

  private void attachTextListeners() {
    content.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        extractHashTagsAndDisplay(charSequence.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
  }

  public void setDefaultCommunitySelection(List<String> coms){
    inlinePostCommunityView.setDefaultSelection(coms);
    invalidateCommunityStrips(coms);
  }

  private void extractHashTagsAndDisplay(String body) {
    ArrayList<String> tags = HashTagUtils.getHashTags(body);
    ArrayList<String> added = new ArrayList<>();
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < tags.size(); i++) {
      if (!added.contains(tags.get(i))) {
        stringBuilder.append(" #").append(tags.get(i));
        added.add(tags.get(i));
      }
    }
    inlineHashtags.setText(stringBuilder.toString());
  }

  public PostCreateComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PostCreateComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public void setImageResource(String filePath) {
    placeholder.setVisibility(GONE);
    mediaSelected = true;
    postImageView.setImageSource(filePath);
    cameraItemContainer.setVisibility(GONE);
  }

  public void setImageDownloadUrl(String url){
    placeholder.setVisibility(GONE);
    mediaSelected = true;
    postImageView.setDownloadUrl(url);
    cameraItemContainer.setVisibility(GONE);
  }

  public List<String> getSelectedCommunityTags() {
    return inlinePostCommunityView.getSelectedTags();
  }

  public String getBody() {
    StringBuilder stringBuilder = new StringBuilder();
    if (postImageView.getDownloadUrl() != null) {
      stringBuilder
        .append("\\n![Hapramp Image](")
        .append(postImageView.getDownloadUrl())
        .append(")\\n");
    }
    if (getContent().length() > 0) {
      stringBuilder
        .append("\\n")
        .append(getContent())
        .append("\\n");
    }
    return stringBuilder.toString();
  }

  public String getContent() {
    return content.getText().toString().trim();
  }

  public List<String> getImageList() {
    List<String> images = new ArrayList<>();
    if (postImageView.getDownloadUrl() != null) {
      images.add(postImageView.getDownloadUrl());
    }
    return images;
  }

  public boolean isMediaSelected() {
    return mediaSelected;
  }

  public boolean isMediaUploaded() {
    return postImageView.getDownloadUrl() != null;
  }

  public boolean isContentEnough() {
    return content.getText().toString().trim().length() > 0;
  }

  public void setMediaSelectorListener(MediaSelectorListener mediaSelectorListener) {
    this.mediaSelectorListener = mediaSelectorListener;
  }

  @Override
  public void onCommunitySelectionChanged(List<String> communities) {
    invalidateCommunityStrips(communities);
  }

  public void invalidateCommunityStrips(List<String> communities) {
    communityStripeView.setCommunities(communities);
  }

  public void setDefaultText(String defaultText) {
    content.setText(defaultText);
  }

  public interface MediaSelectorListener {
    void onImageInsertOptionSelected();

    void onCameraImageSelected();
  }
}
