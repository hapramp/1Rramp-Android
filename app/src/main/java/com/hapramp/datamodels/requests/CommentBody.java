package com.hapramp.datamodels.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 11/13/2017.
 */

public class CommentBody {

    @SerializedName("content")
    public String content;

    public CommentBody(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
