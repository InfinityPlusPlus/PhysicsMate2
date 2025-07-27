package com.example.physicsmate.model;

public class KeyboardBtn {
    public String stringToInsert;
    public String label;  // ID from nav_graph for navigation

    public KeyboardBtn(String stringToInsert, String label) {
        this.stringToInsert = stringToInsert;
        this.label = label;
    }
}
