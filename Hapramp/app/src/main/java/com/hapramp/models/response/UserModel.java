package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 10/29/2017.
 */

public class UserModel {


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
    public List<Skills> skills;
    @SerializedName("karma")
    public int karma;
    @SerializedName("image_uri")
    public String image_uri;
    @SerializedName("bio")
    public String bio;
    @SerializedName("hapcoins")
    public int hapcoins;
    @SerializedName("clubs")
    public List<Clubs> clubs;
    @SerializedName("followers")
    public int followers;
    @SerializedName("followings")
    public int followings;

    public static class Clubs {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("handle")
        public String handle;
        @SerializedName("description")
        public String description;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("logo_uri")
        public String logo_uri;
        @SerializedName("organization")
        public Organization organization;


    }

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

    public static class Skills {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;

        public Skills(int id, String name, String image_uri, String description) {
            this.id = id;
            this.name = name;
            this.image_uri = image_uri;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage_uri() {
            return image_uri;
        }

        public void setImage_uri(String image_uri) {
            this.image_uri = image_uri;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Skills{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", image_uri='" + image_uri + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public UserModel(int id, String email, String username, Organization organization, String full_name, List<Skills> skills, int karma, String image_uri, String bio, int hapcoins, List<Clubs> clubs, int followers, int followings) {
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
        this.clubs = clubs;
        this.followers = followers;
        this.followings = followings;
    }

    @Override
    public String toString() {
        return "UserModel{" +
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
                ", clubs=" + clubs +
                ", followers=" + followers +
                ", followings=" + followings +
                '}';
    }
}


