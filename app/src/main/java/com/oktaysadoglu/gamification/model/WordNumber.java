package com.oktaysadoglu.gamification.model;

/**
 * Created by oktaysadoglu on 17/12/15.
 */
public class WordNumber {

    private int mAnsweredWordNumber;

    private int mCorrectAnsweredWordNumber;


    public WordNumber() {
        super();
    }

    public int getmAnsweredWordNumber() {
        return mAnsweredWordNumber;
    }

    public void setmAnsweredWordNumber(int mAnsweredWordNumber) {
        this.mAnsweredWordNumber = mAnsweredWordNumber;
    }

    public int getmCorrectAnsweredWordNumber() {
        return mCorrectAnsweredWordNumber;
    }

    public void setmCorrectAnsweredWordNumber(int mCorrectAnsweredWordNumber) {
        this.mCorrectAnsweredWordNumber = mCorrectAnsweredWordNumber;
    }

    @Override
    public String toString() {
        return "WordNumber{" +
                "mAnsweredWordNumber=" + mAnsweredWordNumber +
                ", mCorrectAnsweredWordNumber=" + mCorrectAnsweredWordNumber +
                '}';
    }
}
