package com.oktaysadoglu.gamification.model;

/**
 * Created by oktaysadoglu on 02/01/16.
 */

public class LastSeenWord {

    private int id;

    private int level;

    private int baseWordId;

    public LastSeenWord(int id, int level, int baseWordId) {
        this.id = id;
        this.level = level;
        this.baseWordId = baseWordId;
    }

    public LastSeenWord(int level, int baseWordId) {
        this.level = level;
        this.baseWordId = baseWordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBaseWordId() {
        return baseWordId;
    }

    public void setBaseWordId(int baseWordId) {
        this.baseWordId = baseWordId;
    }

    @Override
    public String toString() {
        return "LastSeenWord{" +
                "baseWordId=" + baseWordId +
                ", level=" + level +
                '}';
    }
}
