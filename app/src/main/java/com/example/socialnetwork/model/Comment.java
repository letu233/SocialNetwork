package com.example.socialnetwork.model;

public class Comment {
    private String comment;
    private String publisher;
    private String time;
    private String date;

    public Comment(String comment, String publisher, String time, String date) {
        this.comment = comment;
        this.publisher = publisher;
        this.time = time;
        this.date = date;
    }

    public Comment() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
