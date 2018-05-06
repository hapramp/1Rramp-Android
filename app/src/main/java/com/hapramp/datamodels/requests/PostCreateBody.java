package com.hapramp.datamodels.requests;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 11/2/2017.
 */

public class PostCreateBody {

    @SerializedName("content")
    public String content;
    @SerializedName("media_uri")
    public String media_uri;
    @SerializedName("post_type")
    public int post_type;
    @SerializedName("skills")
    public List<Integer> skills;
    @Nullable
    @SerializedName("contest_id")
    public String contest_id;

    public PostCreateBody(String content, String media_uri, int post_type, List<Integer> skills, String contest_id) {
        this.content = content;
        this.media_uri = media_uri;
        this.post_type = post_type;
        this.skills = skills;
        this.contest_id = contest_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia_uri() {
        return media_uri;
    }

    public void setMedia_uri(String media_uri) {
        this.media_uri = media_uri;
    }

    public int getPost_type() {
        return post_type;
    }

    public void setPost_type(int post_type) {
        this.post_type = post_type;
    }

    public List<Integer> getSkills() {
        return skills;
    }

    public void setSkills(List<Integer> skills) {
        this.skills = skills;
    }

    public String getContest_id() {
        return contest_id;
    }

    public void setContest_id(String contest_id) {
        this.contest_id = contest_id;
    }

    @Override
    public String toString() {
        return "PostCreateBody{" +
                "content='" + content + '\'' +
                ", media_uri='" + media_uri + '\'' +
                ", post_type=" + post_type +
                ", skills=" + skills +
                ", contest_id=" + contest_id +
                '}';
    }
}
