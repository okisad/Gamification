package com.oktaysadoglu.gamification.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.oktaysadoglu.gamification.databaseHelper.UserHelper;
import com.oktaysadoglu.gamification.databaseScheme.UserDbScheme;
import com.oktaysadoglu.gamification.model.LastSeenWord;
import com.oktaysadoglu.gamification.model.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oktaysadoglu on 02/01/16.
 */
public class LastSeenWordDAO {

    private static final String TAG = "word_dao";

    private Context mContext;

    private UserHelper mDbHelper;

    protected SQLiteDatabase mDatabase;

    public LastSeenWordDAO(Context context) {

        this.mContext = context;

        mDbHelper = UserHelper.getInstance(mContext);

        try {
            mDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mDbHelper.openDataBase();

        open();

    }

    private void open() {

        if (mDbHelper == null) {

            mDbHelper = UserHelper.getInstance(mContext);

        }
        mDatabase = mDbHelper.getWritableDatabase();

    }

    public LastSeenWord getLastSeenWord(int level) {

        Cursor cursor = null;

        LastSeenWord lastSeenWord = null;

        try {
            cursor = mDatabase.query(UserDbScheme.LastSeenWordTable.NAME, null, UserDbScheme.LastSeenWordTable.Columns.LEVEL + "=?", new String[]{String.valueOf(level)}, null, null, null);

            if (cursor == null) {

                Log.e(TAG, "cursor is null inside getBaseWordId from LastSeenWordDAO class");

            } else {

                if (cursor.getCount() == 0) {

                    Log.e(TAG, "table is empty inside getBaseWordId from LastSeenWordDAO class");

                } else {

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {

                        lastSeenWord = new LastSeenWord(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));

                        cursor.moveToNext();

                    }
                }
            }
        }finally {
            if (cursor != null){

                cursor.close();

            }
        }

        return lastSeenWord;

    }

    public void updateBaseWordId(LastSeenWord lastSeenWord){

        int affectedRow;

        if (lastSeenWord != null ) {

            int id = lastSeenWord.getId();

            ContentValues contentValues = getContentValues(lastSeenWord);

            affectedRow = mDatabase.update(UserDbScheme.LastSeenWordTable.NAME, contentValues, "_id=?", new String[]{String.valueOf(id)});

            if (affectedRow == 1) {

                Log.e(TAG, "update is success");

            } else {

                Log.e(TAG, "update is not success");

            }

        } else {

            Log.e(TAG, "parameter Word is null inside updateWord");

        }

    }

    public List<LastSeenWord> getAll() {

        List<LastSeenWord> words = new ArrayList<>();

        Cursor cursor = null;

        try {

            cursor = mDatabase.query(UserDbScheme.LastSeenWordTable.NAME, null, null, null, null, null, null);

            if (cursor != null) {

                if (cursor.getCount() == 0) {

                    Log.e(TAG, "table is empty inside getAllWords");

                } else {

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {

                        words.add(new LastSeenWord(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));

                        cursor.moveToNext();

                    }

                }


            } else {

                Log.e(TAG, "cursor is null inside getAllWords");

            }

        } finally {

            if (cursor != null) {

                cursor.close();

            } else {

                Log.e(TAG, "cursor is null inside getAllWords");
            }

        }

        return words;
    }

    private ContentValues getContentValues(LastSeenWord lastSeenWord) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(UserDbScheme.LastSeenWordTable.Columns.LEVEL, lastSeenWord.getLevel());

        contentValues.put(UserDbScheme.LastSeenWordTable.Columns.LAST_BASE_WORD_ID, lastSeenWord.getBaseWordId());

        return contentValues;

    }

}
