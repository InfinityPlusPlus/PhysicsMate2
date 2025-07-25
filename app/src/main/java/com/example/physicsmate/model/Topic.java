package com.example.physicsmate.model;

public class Topic {
    public String title;
    public int navId;  // ID from nav_graph for navigation

    public Topic(String title, int navId) {
        this.title = title;
        this.navId = navId;
    }
}
