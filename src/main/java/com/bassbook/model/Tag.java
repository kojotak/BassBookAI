package com.bassbook.model;

public enum Tag {
    PLAY("Play"),
    PRACTICE("Practice"),
    SLOWDOWN("Slowdown"),
    TODO("Todo"),
    FORGET("Forget");
    
    private final String displayName;
    
    Tag(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}