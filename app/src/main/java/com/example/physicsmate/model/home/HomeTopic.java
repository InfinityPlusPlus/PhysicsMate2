package com.example.physicsmate.model.home;

public class HomeTopic {
    public String title;
    public int navId;  // ID from nav_graph for navigation

    public HomeTopic(String title, int navId) {
        this.title = title;
        this.navId = navId;
    }
}
