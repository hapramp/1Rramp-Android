package com.hapramp.datamodels.requests;

/**
 * Created by Ankit on 2/21/2018.
 */

public class PostForProcessingModel {

    String content;
    String full_permalink;

    public PostForProcessingModel(String content, String full_permalink) {
        this.content = content;
        this.full_permalink = full_permalink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFull_permalink() {
        return full_permalink;
    }

    public void setFull_permalink(String full_permalink) {
        this.full_permalink = full_permalink;
    }
}
