package com.oktaysadoglu.gamification.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oktaysadoglu.gamification.databaseHelper.UserHelper;
import com.oktaysadoglu.gamification.databaseScheme.UserDbScheme;
import com.oktaysadoglu.gamification.model.Log;

import java.io.IOException;

/**
 * Created by oktaysadoglu on 23/12/15.
 */
public class LogDAO {

    private Context mContext;

    private UserHelper mDbHelper;

    private SQLiteDatabase mDatabase;

    public LogDAO(Context mContext) {
        this.mContext = mContext;

        mDbHelper = UserHelper.getInstance(mContext);

        try {
            mDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mDbHelper.openDataBase();

        open();
    }

    private void open(){

        if(mDbHelper == null){

            mDbHelper = UserHelper.getInstance(mContext);

        }

        mDatabase = mDbHelper.getWritableDatabase();

    }

    public synchronized long addLog(Log log){

        ContentValues contentValues = getContentValues(log);

        long id = mDatabase.insert(UserDbScheme.LogTable.NAME, null, contentValues);

        return id;

    }

    private ContentValues getContentValues(Log log){

        ContentValues contentValues = new ContentValues();

        contentValues.put(UserDbScheme.LogTable.Columns.LOG_NAME,log.getLogName());

        contentValues.put(UserDbScheme.LogTable.Columns.LOG_DESCRIPTION,log.getLogDescription());

        contentValues.put(UserDbScheme.LogTable.Columns.DATE,log.getDate());

        return contentValues;
    }
}
