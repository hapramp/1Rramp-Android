package com.hapramp.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ankit on 10/25/2017.
 */

public class PostResponse {


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
    @SerializedName("is_voted")
    public boolean is_voted;
    @SerializedName("current_vote")
    public int current_vote;
    @SerializedName("comment_count")
    public int comment_count;
    @SerializedName("contest_post")
    public Contest_post contest_post;

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

    public boolean isIs_voted() {
        return is_voted;
    }

    public void setIs_voted(boolean is_voted) {
        this.is_voted = is_voted;
    }

    public int getCurrent_vote() {
        return current_vote;
    }

    public void setCurrent_vote(int current_vote) {
        this.current_vote = current_vote;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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
    }
}
