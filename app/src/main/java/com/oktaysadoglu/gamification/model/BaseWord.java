package com.oktaysadoglu.gamification.model;

/**
 * Created by oktaysadoglu on 10/12/15.
 */
public class BaseWord {

    private int id;

    private String word;

    private String mean;

    public BaseWord() {

    }

    public BaseWord(String word, String mean) {
        this.mean = mean;
        this.word = word;
    }

    public BaseWord(int id, String word, String mean) {
        this.id = id;
        this.word = word;
        this.mean = mean;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + "-"+word +" : "+mean;
    }
}
