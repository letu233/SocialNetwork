package com.example.socialnetwork.model;

public class Posts {
    public String uid, time, date, postimage, fullname, email, topic, title, content;
    public Posts(){

    }

    public Posts(String uid, String time, String date, String postimage, String fullname, String email, String topic, String title, String content) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.fullname = fullname;
        this.email = email;
        this.topic = topic;
        this.title = title;
        this.content = content;
    }

    public Posts(String uid, String time, String date, String fullname, String email, String topic, String title, String content) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.fullname = fullname;
        this.email = email;
        this.topic = topic;
        this.title = title;
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPostimage() {
        return postimage;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }



}
