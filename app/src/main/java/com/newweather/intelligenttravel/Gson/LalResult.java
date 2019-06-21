package com.newweather.intelligenttravel.Gson;

public class LalResult {
    private LalLocation location;
    private String level;
    public void setLocation(LalLocation location) {
        this.location = location;
    }
    public LalLocation getLocation() {
        return location;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getLevel() {
        return level;
    }
}
