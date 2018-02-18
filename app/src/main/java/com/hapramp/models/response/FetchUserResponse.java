package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 10/22/2017.
 */

import com.google.gson.annotations.Expose;

public class FetchUserResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("organization")
    @Expose
    private Organization organization;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("skills")
    @Expose
    private List<Skill> skills = null;
    @SerializedName("karma")
    @Expose
    private Integer karma;
    @SerializedName("image_uri")
    @Expose
    private String imageUri;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("hapcoins")
    @Expose
    private Double hapcoins;
    @SerializedName("clubs")
    @Expose
    private List<Object> clubs = null;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("followings")
    @Expose
    private Integer followings;
    @SerializedName("posts")
    @Expose
    private Integer posts;
    @SerializedName("rated")
    @Expose
    private Integer rated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Integer getKarma() {
        return karma;
    }

    public void setKarma(Integer karma) {
        this.karma = karma;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Double getHapcoins() {
        return hapcoins;
    }

    public void setHapcoins(Double hapcoins) {
        this.hapcoins = hapcoins;
    }

    public List<Object> getClubs() {
        return clubs;
    }

    public void setClubs(List<Object> clubs) {
        this.clubs = clubs;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowings() {
        return followings;
    }

    public void setFollowings(Integer followings) {
        this.followings = followings;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public Integer getRated() {
        return rated;
    }

    public void setRated(Integer rated) {
        this.rated = rated;
    }

    public class Organization {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("image_uri")
        @Expose
        private String imageUri;
        @SerializedName("description")
        @Expose
        private String description;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImageUri() {
            return imageUri;
        }

        public void setImageUri(String imageUri) {
            this.imageUri = imageUri;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public class Skill {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image_uri")
        @Expose
        private Object imageUri;
        @SerializedName("description")
        @Expose
        private Object description;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getImageUri() {
            return imageUri;
        }

        public void setImageUri(Object imageUri) {
            this.imageUri = imageUri;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

    }

    @Override
    public String toString() {
        return "FetchUserResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", organization=" + organization +
                ", fullName='" + fullName + '\'' +
                ", skills=" + skills +
                ", karma=" + karma +
                ", imageUri='" + imageUri + '\'' +
                ", bio='" + bio + '\'' +
                ", hapcoins=" + hapcoins +
                ", clubs=" + clubs +
                ", followers=" + followers +
                ", followings=" + followings +
                ", posts=" + posts +
                ", rated=" + rated +
                '}';
    }
}