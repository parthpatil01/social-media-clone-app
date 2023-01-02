package com.example.socialmedia.models;

public class LikesFollowersFollowing {
    String profileUrl, name, username, comment;

    public LikesFollowersFollowing() {
    }

    public LikesFollowersFollowing(String profileUrl, String name, String username) {
        this.profileUrl = profileUrl;
        this.name = name;
        this.username = username;
    }

    public LikesFollowersFollowing(String comment, String profileUrl, String name, String username) {
        this.profileUrl = profileUrl;
        this.comment = comment;
        this.username = username;
        this.name=name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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
}
