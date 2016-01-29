package com.oktaysadoglu.gamification.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by oktaysadoglu on 23/12/15.
 */


public class Log {

    private int id;
    private String logName;
    private String logDescription;
    private String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

    public Log(String logName, String logDescription) {
        this.logName = logName;
        this.logDescription = logDescription;
    }

    public Log(int id, String logName, String logDescription) {
        this.id = id;
        this.logName = logName;
        this.logDescription = logDescription;
    }

    public Log() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogDescription() {
        return logDescription;
    }

    public void setLogDescription(String logDescription) {
        this.logDescription = logDescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Log{" +
                "logName='" + logName + '\'' +
                ", logDescription='" + logDescription + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
