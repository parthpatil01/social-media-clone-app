package com.example.socialmedia.models;

import java.util.List;

public class UsersData {
    String username, totalPosts, followers, following ,name,bio,profilePic;
    //    List<String> posts, thumbnails, followersList, followingList, caption;
    List<String> followersList, followingList, caption;
    List<PostData> postData;

    public UsersData(){}
    public UsersData(String username, String totalPosts, String followers, String following, List<String> followersList, List<String> followingList, List<String> caption, List<PostData> postData) {
        this.username = username;
        this.totalPosts = totalPosts;
        this.followers = followers;
        this.following = following;
        this.followersList = followersList;
        this.followingList = followingList;
        this.caption = caption;
        this.postData = postData;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(String totalPosts) {
        this.totalPosts = totalPosts;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public List<String> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(List<String> followersList) {
        this.followersList = followersList;
    }

    public List<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<String> followingList) {
        this.followingList = followingList;
    }

    public List<String> getCaption() {
        return caption;
    }

    public void setCaption(List<String> caption) {
        this.caption = caption;
    }

    public List<PostData> getPostData() {
        return postData;
    }

    public void setPostData(List<PostData> postData) {
        this.postData = postData;
    }

    public static class PostData {

        String Id, ImgUrl, ThumbnailUrl, caption;


        public PostData(String id, String imgUrl, String thumbnailUrl) {
            Id = id;
            ImgUrl = imgUrl;
            ThumbnailUrl = thumbnailUrl;
        }

        public PostData(String id, String imgUrl, String thumbnailUrl, String caption) {
            Id = id;
            ImgUrl = imgUrl;
            ThumbnailUrl = thumbnailUrl;
            this.caption = caption;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }

        public String getThumbnailUrl() {
            return ThumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            ThumbnailUrl = thumbnailUrl;
        }
    }
}
