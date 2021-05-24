package com.example.socialnetwork.model;

public class Notification {
    private String uid;
    private String text;
    private String postid;
    private Boolean ispost;

    public Notification() {
    }

    public Notification(String uid, String text, String postid, Boolean ispost) {
        this.uid = uid;
        this.text = text;
        this.postid = postid;
        this.ispost = ispost;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public Boolean getIspost() {
        return ispost;
    }

    public void setIspost(Boolean ispost) {
        this.ispost = ispost;
    }
}
