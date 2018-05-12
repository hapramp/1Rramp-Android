package com.hapramp.views.post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hapramp.R;
import com.hapramp.datamodels.CommunityModel;
import com.hapramp.preferences.HaprampPreferenceManager;
import com.hapramp.steem.Communities;
import com.hapramp.steem.FeedDataConstants;
import com.hapramp.steem.models.data.FeedDataItemModel;
import com.hapramp.steem.models.user.SteemUser;
import com.hapramp.utils.ImageHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/1/2018.
 */

public class PostCreateComponent extends FrameLayout implements PostCategoryView.CommunitySelectionChangeListener {


    @BindView(R.id.feed_owner_pic)
    ImageView postCreatorPic;
    @BindView(R.id.reference_line)
    Space referenceLine;
    @BindView(R.id.feed_owner_title)
    TextView postCreatorTitle;
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
    @BindView(R.id.postImageView)
    PostImageView postImageView;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.category_caption)
    TextView categoryCaption;
    @BindView(R.id.postCategoryView)
    PostCategoryView postCategoryView;
    @BindView(R.id.youtube_thumbnailIv)
    ImageView youtubeThumbnailIv;
    @BindView(R.id.youtube_indicator)
    ImageView youtubeIndicator;
    @BindView(R.id.youtube_item_container)
    RelativeLayout youtubeItemContainer;
    @BindView(R.id.btn_remove)
    TextView btnRemove;
    private Context mContext;
    private String youtubeId = null;

    public PostCreateComponent(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PostCreateComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostCreateComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.post_create_layout, this);
        ButterKnife.bind(this, view);
        postCategoryView.initCategory();
        postCategoryView.setCommunitySelectionChangeListener(this);
        SteemUser steemUser = new Gson().fromJson(HaprampPreferenceManager.getInstance().getCurrentUserInfoAsJson(), SteemUser.class);
        String pic_url = steemUser
                .getUser()
                .getJsonMetadata()
                .getProfile()
                .getProfileImage();
        ImageHandler.loadCircularImage(context, postCreatorPic, pic_url);
        postCreatorTitle.setText(steemUser.getUser().getJsonMetadata().getProfile().getName());
        btnRemove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                youtubeId = null;
                youtubeItemContainer.setVisibility(GONE);
            }
        });
    }

    public void setYoutubeThumbnail(String videoId) {
        youtubeItemContainer.setVisibility(VISIBLE);
        this.youtubeId = videoId;
        postImageView.scaleAndHideMainView();
        ImageHandler.load(mContext, youtubeThumbnailIv, getYoutubeThumbnailUrl(videoId));
    }

    private String getYoutubeThumbnailUrl(String videoKey) {
        return String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", videoKey);
    }

    public void setImageResource(final Bitmap bitmap) {
        postImageView.setImageSource(bitmap);
        youtubeId = null;
        youtubeItemContainer.setVisibility(GONE);
    }

    public List<String> getSelectedCommunityTags() {
        return postCategoryView.getSelectedTags();
    }

    public String getContent() {
        return content.getText().toString().trim();
    }

    @Override
    public void onCommunitySelectionChanged(List<String> communities) {
        setCommunities(communities);
    }

    private void setCommunities(List<String> communities) {
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

    public List<FeedDataItemModel> getDataList() {
        List<FeedDataItemModel> datas = new ArrayList<>();
        if (postImageView.getDownloadUrl() != null && youtubeId == null) {
            datas.add(new FeedDataItemModel(postImageView.getDownloadUrl(), FeedDataConstants.ContentType.IMAGE));
        } else if (youtubeId != null) {
            datas.add(new FeedDataItemModel(youtubeId, FeedDataConstants.ContentType.YOUTUBE));
        }
        if (getContent().length() > 0) {
            datas.add(new FeedDataItemModel(getContent(), FeedDataConstants.ContentType.TEXT));
        }
        return datas;
    }

    public boolean isValidContent() {
        return getContent().length() > 0 && (postImageView.getDownloadUrl() != null || youtubeId != null);
    }
}
