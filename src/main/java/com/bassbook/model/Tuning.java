package com.bassbook.model;

public enum Tuning {
    EADG("EADG"),
    DADG("DADG"),
    CGCF("CGCF"),
    BEADG("BEADG");
    
    private final String displayName;
    
    Tuning(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}