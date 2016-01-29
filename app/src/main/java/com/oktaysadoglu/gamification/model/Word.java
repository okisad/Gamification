package com.oktaysadoglu.gamification.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by oktaysadoglu on 10/12/15.
 */

public class Word {

    private int id;

    private int baseWordId;

    private int numberOfNotKnow;

    private String Date;

    public Word(int id, int baseWordId, int numberOfNotKnow) {
        this.id = id;
        this.baseWordId = baseWordId;
        this.numberOfNotKnow = numberOfNotKnow;
        Date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public Word(int baseWordId, int numberOfNotKnow) {
        this.baseWordId = baseWordId;
        this.numberOfNotKnow = numberOfNotKnow;
        Date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public int getBaseWordId() {
        return baseWordId;
    }

    public void setBaseWordId(int baseWordId) {
        this.baseWordId = baseWordId;
    }

    public int getNumberOfNotKnow() {
        return numberOfNotKnow;
    }

    public void setNumberOfNotKnow(int numberOfNotKnow) {
        this.numberOfNotKnow = numberOfNotKnow;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Word{" +
                "baseWordId=" + baseWordId +
                ", numberOfNotKnow=" + numberOfNotKnow +
                '}';
    }
}
