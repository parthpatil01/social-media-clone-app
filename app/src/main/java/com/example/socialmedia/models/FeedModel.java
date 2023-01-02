package com.example.socialmedia.models;

public class FeedModel {

    String thumbnailUrl, imageUrl, caption, username, id;
    int date, likes, comments;
    boolean liked;

    public FeedModel() {
    }

    public FeedModel(String thumbnailUrl, String imageUrl, String caption, String username, int date, int likes, int comments) {
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.date = date;
        this.likes = likes;
        this.comments = comments;
        this.username = username;
    }

    public FeedModel(String thumbnailUrl, String imageUrl, String username, int date, int likes, int comments) {
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.date = date;
        this.likes = likes;
        this.comments = comments;
        this.username = username;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
