package com.hapramp.steem;

import com.google.gson.annotations.SerializedName;

public class ProcessedBodyResponse {

    @SerializedName("body")
    private String mBody;

    public ProcessedBodyResponse(String mBody) {
        this.mBody = mBody;
    }

    public String getmBody() {
        return mBody;
    }

    public void setmBody(String mBody) {
        this.mBody = mBody;
    }
}
