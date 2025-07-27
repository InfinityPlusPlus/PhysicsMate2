package com.example.physicsmate.model;

public class KeyboardBtn {
    public String stringToInsert;
    public String tag;  // work as id
    public String displayText;

    public KeyboardBtn(String stringToInsert, String tag, String displayText) {
        this.stringToInsert = stringToInsert;
        this.tag = tag;
        this.displayText = displayText;

    }
}
