package com.example.socialmedia.models;

import com.google.firebase.firestore.auth.User;

public class UserProfile {
    private String username;
    private int posts,followers,following;

    public UserProfile(){}
    public UserProfile(String username, int posts, int followers, int following) {
        this.username = username;
        this.posts = posts;
        this.followers = followers;
        this.following = following;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
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
}
