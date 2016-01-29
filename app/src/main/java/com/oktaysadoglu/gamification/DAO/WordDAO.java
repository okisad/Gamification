package com.oktaysadoglu.gamification.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TabHost;

import com.oktaysadoglu.gamification.databaseHelper.UserHelper;

import com.oktaysadoglu.gamification.databaseScheme.DictionaryDbScheme;
import com.oktaysadoglu.gamification.databaseScheme.UserDbScheme;
import com.oktaysadoglu.gamification.databaseScheme.UserDbScheme.*;
import com.oktaysadoglu.gamification.model.BaseWord;
import com.oktaysadoglu.gamification.model.Word;
import com.oktaysadoglu.gamification.model.WordNumber;
import com.oktaysadoglu.gamification.preferences.UserStatus;

import java.io.IOException;
import java.nio.channels.IllegalSelectorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by oktaysadoglu on 10/12/15.
 */
public class WordDAO {

    private static final String TAG = "word_dao";

    private Context mContext;

    private UserHelper mDbHelper;

    protected SQLiteDatabase mDatabase;

    public WordDAO(Context context) {

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

    public List<Word> getSpecialNotKnownWords(){

        Cursor cursor = null;

        List<Word> wordList = new ArrayList<>();

        try {

            cursor = mDatabase.query(UserWordTable.NAME,null,UserWordTable.Columns.NUMBER_OF_NOT_KNOW+"=?",new String[]{"-2"},null,null,null);

            if (cursor !=null){

                if(cursor.getCount() > 0){

                    cursor.moveToFirst();

                    while(!cursor.isAfterLast()){

                        Word word = new Word(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2));

                        wordList.add(word);

                        cursor.moveToNext();

                    }

                }

            }

        }finally {

            if (cursor != null) {

                cursor.close();
            } else {

                Log.e(TAG, "cursor is null inside getSpecialNotKnownWords");
            }

        }

        return wordList;

    }

    //parametre olarak girilen level'a kadar olan bütün kelimeleri doğru yapar
    public void applyKnownCorrectUntilSpecificLevel(int level){

        if (level == 1){

            return;

        }

        Cursor cursor = null;

        try {
            cursor = mDatabase.query(UserDbScheme.UserWordTable.NAME,null,"_id BETWEEN ? AND ?",new String[]{"1",String.valueOf((level*100)-100)},null,null,null);

            if(cursor!=null){

                cursor.moveToFirst();

                while (!cursor.isAfterLast()){

                    Word word = new Word(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2));

                    word.setNumberOfNotKnow(0);

                    updateWord(word);

                    cursor.moveToNext();

                }

            }

        }finally {

            if (cursor != null) {

                cursor.close();
            } else {

                Log.e(TAG, "cursor is null inside getAllWords");
            }

        }

    }

    public synchronized long addWord(Word word) {

        ContentValues contentValues = getContentValues(word);

        long id = mDatabase.insert(UserWordTable.NAME, null, contentValues);

        return id;

    }

    public List<Word> getAllWords() {

        List<Word> words = new ArrayList<>();

        Cursor cursor = null;

        try {

            cursor = mDatabase.query(UserWordTable.NAME, null, null, null, null, null, null);

            if (cursor != null) {

                if (cursor.getCount() == 0) {

                    Log.e(TAG, "table is empty inside getAllWords");

                } else {

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {

                        words.add(new Word(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2)));

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

    public Word getSpecificWord(int id) {

        Cursor cursor = null;

        Word word = null;

        try {

            cursor = mDatabase.query(UserWordTable.NAME, null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null) {

                if (cursor.getCount() == 0) {

                    Log.e(TAG, "table is empty inside getSpecificWord");

                } else {

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {

                        word = new Word(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));

                        cursor.moveToNext();

                    }

                }

            } else {

                Log.e(TAG, "cursor is null inside getSpecificWord");

            }

        } finally {

            if (cursor != null) {


                cursor.close();
            } else {

                Log.e(TAG, "cursor is null inside getSpecificWord");

            }

        }


        return word;

    }

    public Word getSpecificWordFromBaseWordId(int id) {

        Cursor cursor = null;

        Word word = null;

        try {

            cursor = mDatabase.query(UserWordTable.NAME, null, UserWordTable.Columns.BASE_WORD_ID+"=?", new String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null) {

                if (cursor.getCount() == 0) {

                    Log.e(TAG, "table is empty inside getSpecificWord");

                } else {

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {

                        word = new Word(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));

                        cursor.moveToNext();

                    }

                }

            } else {

                Log.e(TAG, "cursor is null inside getSpecificWord");

            }

        } finally {

            if (cursor != null) {


                cursor.close();
            } else {

                Log.e(TAG, "cursor is null inside getSpecificWord");

            }

        }


        return word;

    }

    public synchronized void updateWord(Word word) {

        int affectedRow;

        if (word != null ) {

            int id = word.getId();

            ContentValues contentValues = getContentValues(word);

            affectedRow = mDatabase.update(UserWordTable.NAME, contentValues, "_id=?", new String[]{String.valueOf(id)});

            if (affectedRow == 1) {

                Log.e(TAG, "update is success : "+word.toString());

            } else {

                Log.e(TAG, "update is not success");

            }

        } else {

            Log.e(TAG, "parameter Word is null inside updateWord");

        }


    }

    public Word getNotSeenWordForNotification(){

        Word word = null;

        UserStatus userStatus = UserStatus.getInstance(mContext);

        int level = userStatus.getLevel();

        List<Word> wordList = getNotSeenWordUntilSpecificLevel(level);

        if (wordList.size() > 0){

            word = wordList.get(new Random().nextInt(wordList.size()));

        }

        return word;

    }

    public List<Word> getNotKnownWords(){

        Cursor cursor = null;

        List<Word> wordList = new ArrayList<>();

        try {

            cursor = mDatabase.query(UserWordTable.NAME,new String[]{"_id,"+UserWordTable.Columns.BASE_WORD_ID+","+UserWordTable.Columns.NUMBER_OF_NOT_KNOW},UserWordTable.Columns.NUMBER_OF_NOT_KNOW+"!=? AND "+UserWordTable.Columns.NUMBER_OF_NOT_KNOW+"!=?",new String[]{"0","-1"},null,null,null);

            if (cursor != null) {

                if (cursor.getCount() == 0) {

                    Log.e(TAG, "table is empty inside getSpecificWord");

                } else {

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {

                        Word word = new Word(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));

                        wordList.add(word);

                        cursor.moveToNext();

                    }

                }

            } else {

                Log.e(TAG, "cursor is null inside getSpecificWord");

            }
        }finally {

            if (cursor != null) {


                cursor.close();
            } else {

                Log.e(TAG, "cursor is null inside getSpecificWord");

            }

        }

        return wordList;

    }

    public List<Word> getNotSeenWordUntilSpecificLevel(int level){

        Cursor cursor = null;

        List<Word> wordList = new ArrayList<>();

        try {

            int max = level * 100;

            cursor = mDatabase.query(UserWordTable.NAME,null,UserWordTable.Columns.NUMBER_OF_NOT_KNOW+"=? AND "+UserWordTable.Columns.BASE_WORD_ID+" BETWEEN ? AND ?",new String[]{"-1","1",String.valueOf(max)},null,null,null);

            if (cursor!=null){

                if (cursor.getCount() == 0){

                    Log.e(TAG,"there is empty table in getNotSeenWordUntilSpecificLevel");

                }else{

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()){

                        Word word = new Word(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2));

                        wordList.add(word);

                        cursor.moveToNext();

                    }

                }

            }else {

                Log.e(TAG,"Cursor is null in getNotSeenWordUntilSpecificLevel");

            }

        }finally {
            if (cursor != null) {


                cursor.close();
            } else {

                Log.e(TAG, "cursor is null inside getNotSeenWordUntilSpecificLevel");

            }
        }

        return wordList;

    }

    public int getSeenWordNumber() {

        int wordCount = 0;

        Cursor cursor = null;

        try {

            cursor = mDatabase.query(UserWordTable.NAME, null, UserWordTable.Columns.NUMBER_OF_NOT_KNOW+"!=?", new String[]{"-1"}, null, null, null);

            if (cursor != null) {

                wordCount = cursor.getCount();

            } else {

                wordCount = -1;

                Log.e(TAG, "cursor is null inside getSeenWordNumber");

            }

        } finally {

            if (cursor != null) {

                cursor.close();

            } else {

                Log.e(TAG, "cursor is null inside getSeenWordNumber");

            }

        }

        return wordCount;

    }

    public int getKnownWordsNumber(){

        Cursor cursor = null;

        int knownWordsNumber = 0;

        try {

            cursor = mDatabase.query(UserDbScheme.UserWordTable.NAME,new String[]{UserDbScheme.UserWordTable.Columns.NUMBER_OF_NOT_KNOW},UserDbScheme.UserWordTable.Columns.NUMBER_OF_NOT_KNOW+"=?",new String[]{"0"},null,null,null);

            if (cursor != null){

                knownWordsNumber = cursor.getCount();

            }else {

                knownWordsNumber = -1;

            }

        }finally {

            if (cursor != null){

                cursor.close();

            }else {

                Log.e(TAG,"cursor is null inside getKnownWordsNumber");

            }

        }

        return knownWordsNumber;

    }

    public int getKnownWordsPercentage(){

        int seenWords = getSeenWordNumber();

        int knownWords = getKnownWordsNumber();

        int percentageOfKnownWords = (knownWords*100)/seenWords;

        return percentageOfKnownWords;

    }

    public int getKnownWordInSpecificLevel(int level){

        WordNumber wordNumber = getAnsweredWordNumberInSpecificLevel(level);

        return wordNumber.getmCorrectAnsweredWordNumber();

    }

    public WordNumber getAnsweredWordNumberInSpecificLevel(int level){

        Cursor cursor = null;

        WordNumber mWordNumber = new WordNumber();

        String max = String.valueOf((level * 100));

        String min = String.valueOf((level * 100) - 99);

        int numberOfAnsweredWords  = 0;

        int numberOfCorrectAnsweredWords= 0;

        try {

            cursor = mDatabase.query(UserWordTable.NAME, null, UserWordTable.Columns.BASE_WORD_ID+" BETWEEN ? AND ?", new String[]{min,max}, null, null, null);

            if (cursor != null) {

                if (cursor.getCount() == 0) {

                    /*Log.e(TAG, "table is empty inside getSeenWordNumberInSpecificLevel");*/

                } else {

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {

                        if(cursor.getInt(2) == 0){

                            numberOfCorrectAnsweredWords++;

                            numberOfAnsweredWords++;

                        }else if(cursor.getInt(2) != -1){

                            numberOfAnsweredWords++;

                        }

                        cursor.moveToNext();

                    }

                }

            } else {

                Log.e(TAG, "cursor is null inside getSeenWordNumberInSpecificLevel");

            }

        } finally {

            if (cursor != null) {


                cursor.close();
            } else {

                Log.e(TAG, "cursor is null inside getSeenWordNumberInSpecificLevel");

            }

        }


        mWordNumber.setmAnsweredWordNumber(numberOfAnsweredWords);

        mWordNumber.setmCorrectAnsweredWordNumber(numberOfCorrectAnsweredWords);

        /*Log.e("my","firstNotCorrectAnsweredWordNumber : "+firstNotCorrectAnsweredWordNumber);*/

        return mWordNumber;

    }

    private ContentValues getContentValues(Word word) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(UserWordTable.Columns.BASE_WORD_ID, word.getBaseWordId());

        contentValues.put(UserWordTable.Columns.NUMBER_OF_NOT_KNOW, word.getNumberOfNotKnow());

        contentValues.put(UserWordTable.Columns.Date, word.getDate());

        return contentValues;

    }


}
