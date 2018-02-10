package com.hapramp.models;

import com.google.gson.annotations.SerializedName;
import com.hapramp.models.response.UserModel;

import java.util.List;

/**
 * Created by Ankit on 11/16/2017.
 */

public class UserResponse {

    @SerializedName("id")
    public int id;
    @SerializedName("email")
    public String email;
    @SerializedName("username")
    public String username;
    @SerializedName("organization")
    public Organization organization;
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("skills")
    public List<UserModel.Skills> skills;
    @SerializedName("karma")
    public int karma;
    @SerializedName("image_uri")
    public String image_uri;
    @SerializedName("bio")
    public String bio;
    @SerializedName("hapcoins")
    public double hapcoins;

    public static class Organization {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("username")
        public String username;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;
    }


    public UserResponse(int id, String email, String username, Organization organization, String full_name,
                        List<UserModel.Skills> skills, int karma, String image_uri, String bio, double hapcoins) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.organization = organization;
        this.full_name = full_name;
        this.skills = skills;
        this.karma = karma;
        this.image_uri = image_uri;
        this.bio = bio;
        this.hapcoins = hapcoins;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", organization=" + organization +
                ", full_name='" + full_name + '\'' +
                ", skills=" + skills +
                ", karma=" + karma +
                ", image_uri='" + image_uri + '\'' +
                ", bio='" + bio + '\'' +
                ", hapcoins=" + hapcoins +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public List<UserModel.Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<UserModel.Skills> skills) {
        this.skills = skills;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public String getImage_uri() {
        return image_uri!=null?image_uri:"";
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public double getHapcoins() {
        return hapcoins;
    }

    public void setHapcoins(double hapcoins) {
        this.hapcoins = hapcoins;
    }
}
