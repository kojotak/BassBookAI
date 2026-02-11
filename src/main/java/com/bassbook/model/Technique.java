package com.bassbook.model;

public enum Technique {
    SLAP("Slap"),
    HAMMER_ON("Hammer On"),
    PULL_OFF("Pull Off"),
    PICK("Pick"),
    SLIDE("Slide"),
    DOUBLE_STOP("Double Stop"),
    TAP("Tap"),
    PALM_MUTE("Palm Mute"),
    HARMONICS("Harmonics"),
    VIBRATO("Vibrato");
    
    private final String displayName;
    
    Technique(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}