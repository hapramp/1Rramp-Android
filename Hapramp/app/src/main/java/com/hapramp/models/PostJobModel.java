package com.hapramp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Ankit on 12/23/2017.
 */

public class PostJobModel {

    public String content;
    public String media_uri;
    public int post_type;
    public List<Integer> skills;
    public int contest_id;
    public int jobId;

    public PostJobModel(int jobId, String content, String media_uri, int post_type, List<Integer> skills, int contest_id) {

        this.jobId = jobId;
        this.content = content;
        this.media_uri = media_uri;
        this.post_type = post_type;
        this.skills = skills;
        this.contest_id = contest_id;

    }



    public PostJobModel(int jobId, String content, String media_uri, int post_type, String skills, int contest_id) {

        this.jobId = jobId;
        this.content = content;
        this.media_uri = media_uri;
        this.post_type = post_type;
        this.skills = tokenizeSkills(skills);
        this.contest_id = contest_id;

    }

    private List<Integer> tokenizeSkills(String s){

        StringTokenizer tokenizer = new StringTokenizer(s,"#");
        List<Integer> skills = new ArrayList<>();

        for (int i=0;i<tokenizer.countTokens();i++){
            skills.add((int) tokenizer.nextElement());
        }
        return skills;

    }

    public String getSkillsAsPaddedString(){

        StringBuilder builder = new StringBuilder();

        for (int i=0;i<skills.size();i++){
            builder.append(skills.get(i))
                    .append("#");
        }

        return builder.toString();

    }


    @Override
    public String toString() {

        return "PostJobModel{" +
                "content='" + content + '\'' +
                ", media_uri='" + media_uri + '\'' +
                ", post_type=" + post_type +
                ", skills=" + skills +
                ", contest_id=" + contest_id +
                '}';

    }

}
