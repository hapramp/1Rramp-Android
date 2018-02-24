package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;

public class PreProcessingModel {

    @SerializedName("full_permlink")
    private String mFullPermlink;
    @SerializedName("content")
    private PostStructureModel mContent;

    public PreProcessingModel(String mFullPermlink, PostStructureModel mContent) {
        this.mFullPermlink = mFullPermlink;
        this.mContent = mContent;
    }

    public String getmFullPermlink() {
        return mFullPermlink;
    }

    public void setmFullPermlink(String mFullPermlink) {
        this.mFullPermlink = mFullPermlink;
    }

    public PostStructureModel getmContent() {
        return mContent;
    }

    public void setmContent(PostStructureModel mContent) {
        this.mContent = mContent;
    }
}
