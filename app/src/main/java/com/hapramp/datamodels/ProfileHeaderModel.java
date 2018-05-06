package com.hapramp.datamodels;

import java.util.List;

/**
 * Created by Ankit on 12/30/2017.
 */

public class ProfileHeaderModel {

    private int userId;
    private String dpUrl;
    private String userName;
    private String hapname;
    private String bio;
    private int posts;
    private int followers;
    private int following;
    private boolean editable;
    private List<CommunityModel> interests;

    public ProfileHeaderModel(int userId,
                              String dpUrl, String userName,
                              String hapname,
                              boolean editable,
                              String bio,
                              int posts,
                              int followers,
                              int following,
                              List<CommunityModel> interests) {

        this.userId = userId;
        this.dpUrl = dpUrl;
        this.userName = userName;
        this.hapname = hapname;
        this.editable = editable;
        this.bio = bio;
        this.posts = posts;
        this.followers = followers;
        this.following = following;
        this.interests = interests;

    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHapname() {
        return hapname;
    }

    public void setHapname(String hapname) {
        this.hapname = hapname;
    }

    public String getBio() {
        return bio;
    }


    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "ProfileHeaderModel{" +
                "dpUrl='" + dpUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", hapname='" + hapname + '\'' +
                ", bio='" + bio + '\'' +
                ", posts='" + posts + '\'' +
                ", followers='" + followers + '\'' +
                ", following='" + following + '\'' +
                ", interests=" + interests +
                '}';
    }
}
