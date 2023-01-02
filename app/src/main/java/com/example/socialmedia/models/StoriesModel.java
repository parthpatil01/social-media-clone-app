package com.example.socialmedia.models;

import java.util.List;

public class StoriesModel {


    String username,profileUrl;
    List<String> stories;

    public StoriesModel(){}

    public StoriesModel(String username, String profileUrl, List<String> stories) {
        this.username = username;
        this.profileUrl = profileUrl;
        this.stories = stories;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public List<String> getStories() {
        return stories;
    }

    public void setStories(List<String> stories) {
        this.stories = stories;
    }
}
