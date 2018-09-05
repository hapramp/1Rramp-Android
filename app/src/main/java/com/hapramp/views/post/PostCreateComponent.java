package com.hapramp.views.post;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.models.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.models.User;
import com.hapramp.utils.Constants;
import com.hapramp.utils.HashTagUtils;
import com.hapramp.utils.ImageHandler;

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
  @BindView(R.id.club3)
  TextView club3;
  @BindView(R.id.club2)
  TextView club2;
  @BindView(R.id.club1)
  TextView club1;
  @BindView(R.id.post_header_container)
  RelativeLayout postHeaderContainer;
  @BindView(R.id.image_selector)
  ImageView imageSelector;
  @BindView(R.id.central_divider)
  FrameLayout centralDivider;
  @BindView(R.id.video_selector)
  ImageView videoSelector;
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
  RelativeLayout youtubeItemContainer;
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
  private Context mContext;
  private String youtubeId = null;
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
    User steemUser = new Gson().fromJson(HaprampPreferenceManager.getInstance().getCurrentUserInfoAsJson(), User.class);
    String pic_url = steemUser.getProfile_image();
    ImageHandler.loadCircularImage(context, feedOwnerPic, pic_url);
    feedOwnerTitle.setText(steemUser.getFullname());
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
        youtubeId = null;
        mediaSelected = false;
        youtubeItemContainer.setVisibility(GONE);
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

    videoSelector.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mediaSelectorListener != null) {
          mediaSelectorListener.onYoutubeVideoOptionSelected();
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

  private void extractHashTagsAndDisplay(String body) {
    ArrayList<String> tags = HashTagUtils.getHashTags(body);
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < tags.size(); i++) {
      stringBuilder.append(" #").append(tags.get(i));
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

  public void setYoutubeThumbnail(String videoId) {
    youtubeItemContainer.setVisibility(VISIBLE);
    placeholder.setVisibility(GONE);
    this.youtubeId = videoId;
    mediaSelected = true;
    postImageView.scaleAndHideMainView();
    ImageHandler.load(mContext, youtubeThumbnailIv, getYoutubeThumbnailUrl(videoId));
  }

  private String getYoutubeThumbnailUrl(String videoKey) {
    return String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", videoKey);
  }

  public void setImageResource(String filePath) {
    placeholder.setVisibility(GONE);
    mediaSelected = true;
    postImageView.setImageSource(filePath);
    youtubeId = null;
    youtubeItemContainer.setVisibility(GONE);
  }

  public List<String> getSelectedCommunityTags() {
    return inlinePostCommunityView.getSelectedTags();
  }

  public String getBody() {
    StringBuilder stringBuilder = new StringBuilder();
    if (postImageView.getDownloadUrl() != null && youtubeId == null) {
      stringBuilder
        .append("\\n![Hapramp Image](")
        .append(postImageView.getDownloadUrl())
        .append(")\\n");
    } else if (youtubeId != null) {
      stringBuilder.append(String.format("\n<iframe src=\"http://www.youtube.com/embed/%s\"" +
        " width=\"560\" height=\"315\" frameborder=\"0\" allowfullscreen></iframe>\n", youtubeId));
    }
    if (getContent().length() > 0) {
      stringBuilder
        .append("\\n")
        .append(getContent())
        .append("\\n");
    }
    return stringBuilder.toString();
  }

  public List<String> getImageList() {
    List<String> images = new ArrayList<>();
    if (postImageView.getDownloadUrl() != null && youtubeId == null) {
      images.add(postImageView.getDownloadUrl());
    } else if (youtubeId != null) {
      images.add(String.format("https://img.youtube.com/vi/%s/0.jpg", youtubeId));
    }
    return images;
  }

  public String getContent() {
    return content.getText().toString().trim() + Constants.FOOTER_TEXT;
  }

  public boolean isMediaSelected() {
    return mediaSelected;
  }

  public boolean isMediaUploaded() {
    if (youtubeId == null) {
      //check for uploaded image
      return postImageView.getDownloadUrl() != null;
    } else {
      //video is already uploaded!
      return true;
    }
  }

  public boolean isContentEnough() {
    return content.getText().toString().trim().length() > 0;
  }

  public void setMediaSelectorListener(MediaSelectorListener mediaSelectorListener) {
    this.mediaSelectorListener = mediaSelectorListener;
  }

  @Override
  public void onCommunitySelectionChanged(List<String> communities) {
    setCommunities(communities);
  }

  public void setCommunities(List<String> communities) {
    List<CommunityModel> cm = new ArrayList<>();
    for (int i = 0; i < communities.size(); i++) {
      if (Communities.doesCommunityExists(communities.get(i))) {
        cm.add(new CommunityModel("", "", communities.get(i),
          HaprampPreferenceManager.getInstance().getCommunityColorFromTag(communities.get(i)),
          HaprampPreferenceManager.getInstance().getCommunityNameFromTag(communities.get(i)),
          0
        ));
      }
    }
    addCommunitiesToLayout(cm);
  }

  private void addCommunitiesToLayout(List<CommunityModel> cms) {
    int size = cms.size();
    resetVisibility();
    if (size > 0) {
      club1.setVisibility(VISIBLE);
      club1.setText(cms.get(0).getmName());
      club1.getBackground().setColorFilter(
        Color.parseColor(cms.get(0).getmColor()),
        PorterDuff.Mode.SRC_ATOP);
      if (size > 1) {
        club2.setVisibility(VISIBLE);
        club2.setText(cms.get(1).getmName());
        club2.getBackground().setColorFilter(
          Color.parseColor(cms.get(1).getmColor()),
          PorterDuff.Mode.SRC_ATOP);
        if (size > 2) {
          club3.setVisibility(VISIBLE);
          club3.setText(cms.get(2).getmName());
          club3.getBackground().setColorFilter(
            Color.parseColor(cms.get(2).getmColor()),
            PorterDuff.Mode.SRC_ATOP);
        }
      }
    }
  }

  private void resetVisibility() {
    club1.setVisibility(GONE);
    club2.setVisibility(GONE);
    club3.setVisibility(GONE);
  }

  public void setDefaultText(String defaultText) {
    content.setText(defaultText);
  }

  public interface MediaSelectorListener {
    void onImageInsertOptionSelected();

    void onYoutubeVideoOptionSelected();
  }
}
