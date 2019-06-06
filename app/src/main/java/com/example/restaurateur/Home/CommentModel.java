package com.example.restaurateur.Home;

public class CommentModel {
    private String commentsId;
    private String restId;
    private String userName;
    private Float voteForRestaurant;
    private String notes;

    public CommentModel(String commentsId, String restId, String userId, Float voteForRestaurant, String notes) {
        this.commentsId = commentsId;
        this.restId = restId;
        this.userName = userId;
        this.voteForRestaurant = voteForRestaurant;
        this.notes = notes;
    }

    public String getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getUserId() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userName = userId;
    }

    public Float getVoteForRestaurant() {
        return voteForRestaurant;
    }

    public void setVoteForRestaurant(Float voteForRestaurant) {
        this.voteForRestaurant = voteForRestaurant;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
