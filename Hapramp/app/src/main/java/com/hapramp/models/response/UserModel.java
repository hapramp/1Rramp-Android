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
    @SerializedName("bio")
    public String bio;
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
    @SerializedName("posts")
    public List<Posts> posts;
    @SerializedName("clubs")
    public List<Clubs> clubs;
    @SerializedName("followers")
    public int followers;
    @SerializedName("followings")
    public int followings;
    @SerializedName("hapcoins")
    public int hapcoins;


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

        public Organization(int id, String name, String username, String image_uri, String description) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.image_uri = image_uri;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }

        public String getImage_uri() {
            return image_uri;
        }

        public String getDescription() {
            return description;
        }
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

        public String getName() {
            return name;
        }

        public String getImage_uri() {
            return image_uri;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class User {
        @SerializedName("id")
        public int id;
        @SerializedName("username")
        public String username;
        @SerializedName("full_name")
        public String full_name;
        @SerializedName("karma")
        public int karma;
        @SerializedName("image_uri")
        public String image_uri;

        public User(int id, String username, String full_name, int karma, String image_uri) {
            this.id = id;
            this.username = username;
            this.full_name = full_name;
            this.karma = karma;
            this.image_uri = image_uri;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public int getKarma() {
            return karma;
        }

        public void setKarma(int karma) {
            this.karma = karma;
        }

        public String getImage_uri() {
            return image_uri;
        }

        public void setImage_uri(String image_uri) {
            this.image_uri = image_uri;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", full_name='" + full_name + '\'' +
                    ", karma=" + karma +
                    ", image_uri='" + image_uri + '\'' +
                    '}';
        }
    }

    public static class Skill {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_uri")
        public String image_uri;
        @SerializedName("description")
        public String description;

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

        public Skill(int id, String name, String image_uri, String description) {
            this.id = id;
            this.name = name;
            this.image_uri = image_uri;
            this.description = description;

        }

        @Override
        public String toString() {
            return "Skill{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", image_uri='" + image_uri + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    public static class Club {
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
        @SerializedName("skill")
        public Skill skill;
        @SerializedName("organization")
        public Organization organization;

        public Club(int id, String name, String handle, String description, String created_at, String logo_uri, Skill skill, Organization organization) {
            this.id = id;
            this.name = name;
            this.handle = handle;
            this.description = description;
            this.created_at = created_at;
            this.logo_uri = logo_uri;
            this.skill = skill;
            this.organization = organization;
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

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getLogo_uri() {
            return logo_uri;
        }

        public void setLogo_uri(String logo_uri) {
            this.logo_uri = logo_uri;
        }

        public Skill getSkill() {
            return skill;
        }

        public void setSkill(Skill skill) {
            this.skill = skill;
        }

        public Organization getOrganization() {
            return organization;
        }

        public void setOrganization(Organization organization) {
            this.organization = organization;
        }

        @Override
        public String toString() {
            return "Club{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", handle='" + handle + '\'' +
                    ", description='" + description + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", logo_uri='" + logo_uri + '\'' +
                    ", skill=" + skill +
                    ", organization=" + organization +
                    '}';
        }
    }

    public static class Contest_post {
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("handle")
        public String handle;
        @SerializedName("description")
        public String description;
        @SerializedName("logo_uri")
        public String logo_uri;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("start_time")
        public String start_time;
        @SerializedName("end_time")
        public String end_time;
        @SerializedName("entry_fee")
        public int entry_fee;
        @SerializedName("give_out_ratio")
        public int give_out_ratio;
        @SerializedName("club")
        public Club club;
        @SerializedName("participant_count")
        public int participant_count;

        public Contest_post(int id, String name, String handle, String description, String logo_uri, String created_at, String start_time, String end_time, int entry_fee, int give_out_ratio, Club club, int participant_count) {
            this.id = id;
            this.name = name;
            this.handle = handle;
            this.description = description;
            this.logo_uri = logo_uri;
            this.created_at = created_at;
            this.start_time = start_time;
            this.end_time = end_time;
            this.entry_fee = entry_fee;
            this.give_out_ratio = give_out_ratio;
            this.club = club;
            this.participant_count = participant_count;
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

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLogo_uri() {
            return logo_uri;
        }

        public void setLogo_uri(String logo_uri) {
            this.logo_uri = logo_uri;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getEntry_fee() {
            return entry_fee;
        }

        public void setEntry_fee(int entry_fee) {
            this.entry_fee = entry_fee;
        }

        public int getGive_out_ratio() {
            return give_out_ratio;
        }

        public void setGive_out_ratio(int give_out_ratio) {
            this.give_out_ratio = give_out_ratio;
        }

        public Club getClub() {
            return club;
        }

        public void setClub(Club club) {
            this.club = club;
        }

        public int getParticipant_count() {
            return participant_count;
        }

        public void setParticipant_count(int participant_count) {
            this.participant_count = participant_count;
        }

        @Override
        public String toString() {
            return "Contest_post{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", handle='" + handle + '\'' +
                    ", description='" + description + '\'' +
                    ", logo_uri='" + logo_uri + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", entry_fee=" + entry_fee +
                    ", give_out_ratio=" + give_out_ratio +
                    ", club=" + club +
                    ", participant_count=" + participant_count +
                    '}';
        }
    }

    public static class Posts {
        @SerializedName("id")
        public int id;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("content")
        public String content;
        @SerializedName("media_uri")
        public String media_uri;
        @SerializedName("post_type")
        public int post_type;
        @SerializedName("user")
        public User user;
        @SerializedName("skills")
        public List<Skills> skills;
        @SerializedName("vote_count")
        public int vote_count;
        @SerializedName("vote_sum")
        public int vote_sum;
        @SerializedName("comment_count")
        public int comment_count;
        @SerializedName("contest_post")
        public Contest_post contest_post;

        public Posts(int id, String created_at, String content, String media_uri, int post_type, User user, List<Skills> skills, int vote_count, int vote_sum, int comment_count, Contest_post contest_post) {
            this.id = id;
            this.created_at = created_at;
            this.content = content;
            this.media_uri = media_uri;
            this.post_type = post_type;
            this.user = user;
            this.skills = skills;
            this.vote_count = vote_count;
            this.vote_sum = vote_sum;
            this.comment_count = comment_count;
            this.contest_post = contest_post;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<Skills> getSkills() {
            return skills;
        }

        public void setSkills(List<Skills> skills) {
            this.skills = skills;
        }

        public int getVote_count() {
            return vote_count;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

        public int getVote_sum() {
            return vote_sum;
        }

        public void setVote_sum(int vote_sum) {
            this.vote_sum = vote_sum;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public Contest_post getContest_post() {
            return contest_post;
        }

        public void setContest_post(Contest_post contest_post) {
            this.contest_post = contest_post;
        }

        @Override
        public String toString() {
            return "Posts{" +
                    "id=" + id +
                    ", created_at='" + created_at + '\'' +
                    ", content='" + content + '\'' +
                    ", media_uri='" + media_uri + '\'' +
                    ", post_type=" + post_type +
                    ", user=" + user +
                    ", skills=" + skills +
                    ", vote_count=" + vote_count +
                    ", vote_sum=" + vote_sum +
                    ", comment_count=" + comment_count +
                    ", contest_post=" + contest_post +
                    '}';
        }
    }

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
        @SerializedName("skill")
        public Skill skill;
        @SerializedName("organization")
        public Organization organization;

        public Clubs(int id, String name, String handle, String description, String created_at, String logo_uri, Skill skill, Organization organization) {
            this.id = id;
            this.name = name;
            this.handle = handle;
            this.description = description;
            this.created_at = created_at;
            this.logo_uri = logo_uri;
            this.skill = skill;
            this.organization = organization;
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

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getLogo_uri() {
            return logo_uri;
        }

        public void setLogo_uri(String logo_uri) {
            this.logo_uri = logo_uri;
        }

        public Skill getSkill() {
            return skill;
        }

        public void setSkill(Skill skill) {
            this.skill = skill;
        }

        public Organization getOrganization() {
            return organization;
        }

        public void setOrganization(Organization organization) {
            this.organization = organization;
        }

        @Override
        public String toString() {
            return "Clubs{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", handle='" + handle + '\'' +
                    ", description='" + description + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", logo_uri='" + logo_uri + '\'' +
                    ", skill=" + skill +
                    ", organization=" + organization +
                    '}';
        }
    }

    public UserModel(int id, String email, String username, Organization organization, String full_name, List<Skills> skills, int karma, String image_uri, List<Posts> posts, List<Clubs> clubs, int followers, int followings) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.organization = organization;
        this.full_name = full_name;
        this.skills = skills;
        this.karma = karma;
        this.image_uri = image_uri;
        this.posts = posts;
        this.clubs = clubs;
        this.followers = followers;
        this.followings = followings;
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

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }

    public List<Clubs> getClubs() {
        return clubs;
    }

    public void setClubs(List<Clubs> clubs) {
        this.clubs = clubs;
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
                ", posts=" + posts +
                ", clubs=" + clubs +
                '}';
    }
}
