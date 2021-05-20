package com.example.socialnetwork.model;

public class Topics {
    private String name;

    public Topics() {
    }

    public Topics(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name ;
    }
}
