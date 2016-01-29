package com.oktaysadoglu.gamification.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.oktaysadoglu.gamification.databaseHelper.DictionaryHelper;
import com.oktaysadoglu.gamification.model.BaseWord;
import com.oktaysadoglu.gamification.databaseScheme.DictionaryDbScheme.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by oktaysadoglu on 10/12/15.
 */
public class BaseWordDAO {

    private static final String TAG = "base_word_dao";

    private Context mContext;

    private SQLiteDatabase mDatabase;

    private DictionaryHelper mDbHelper;

    public BaseWordDAO(Context context) {
        this.mContext = context.getApplicationContext();

        mDbHelper = DictionaryHelper.getInstance(mContext);

        try {
            mDbHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mDbHelper.openDataBase();

        /*mDbHelper.close();*/

        open();

    }

    private void open(){

        if(mDbHelper==null){

            mDbHelper = DictionaryHelper.getInstance(mContext);

        }

        mDatabase = mDbHelper.getReadableDatabase();

    }

    private synchronized void addWord(BaseWord word){

        ContentValues contentValues = getContentValues(word);

        mDatabase.insert(BASE_WORD_LIST_TABLE.NAME, null, contentValues);

    }

    public int getWordNumber(){

        Cursor cursor = null;

        int databaseWordCount = 0;

        try {

            cursor = mDatabase.query(BASE_WORD_LIST_TABLE.NAME, null, null, null, null, null, null);

            if(cursor!=null){

                databaseWordCount = cursor.getCount();

            }else{

                databaseWordCount = -1;

            }

        }finally {
            if (cursor != null){

                cursor.close();

            }else {

                Log.e(TAG, "cursor is null");

            }


        }

        return databaseWordCount;

    }

    public List<BaseWord> getAllWordList(){

        Cursor cursor = null;

        List<BaseWord> words;

        try {

            cursor = mDatabase.query(BASE_WORD_LIST_TABLE.NAME, null, null, null, null, null, null);

            if(cursor != null){

                words = new ArrayList<>();

                cursor.moveToFirst();

                while (!cursor.isAfterLast()){

                    words.add(new BaseWord(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));

                    cursor.moveToNext();

                }


            }else {

                words = null;

            }

        }finally {

            if(cursor != null){

                cursor.close();

            }else {

                Log.e(TAG,"cursor is null inside getAllWordList()");

            }

        }

        return words;

    }

    public BaseWord getSpecificBaseWord(int id){

        BaseWord mBaseWord = null;

        Cursor cursor = null;

        try {

            cursor = mDatabase.query(BASE_WORD_LIST_TABLE.NAME,null,"_id=?",new String[]{String.valueOf(id)},null,null,null);

            if (cursor != null){

                if (cursor.getCount() > 0){

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()){

                        mBaseWord = new BaseWord(cursor.getInt(0),cursor.getString(1),cursor.getString(2));

                        cursor.moveToNext();

                    }

                }else {

                    Log.e(TAG,"id is not found in table BaseWord. Inside getSpecificBaseWord");

                }

            }else {

                mBaseWord = null;

            }

        }finally {

            if(cursor!= null){

                cursor.close();

            }else {

                Log.e(TAG,"cursor is null inside getSpecificBaseWord");

            }

        }


        return mBaseWord;

    }

    public BaseWord getSpecificBaseWord(String wordName){

        BaseWord mBaseWord = null;

        Cursor cursor = null;

        try {

            cursor = mDatabase.query(BASE_WORD_LIST_TABLE.NAME,null, BASE_WORD_LIST_TABLE.Columns.WORD+"=?",new String[]{wordName},null,null,null);

            if (cursor != null){

                if (cursor.getCount() > 0){

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()){

                        mBaseWord = new BaseWord(cursor.getInt(0),cursor.getString(1),cursor.getString(2));

                        cursor.moveToNext();

                    }

                }else {

                    Log.e(TAG, "wordName is not found in table BaseWord. Inside getSpecificBaseWord");

                }

            }else {

                mBaseWord = null;

            }

        }finally {

            if(cursor!= null){

                cursor.close();

            }else {

                Log.e(TAG, "cursor is null inside getSpecificBaseWord");

            }

        }


        return mBaseWord;

    }

    public List<BaseWord> getBaseWordListAccordingToLevel(int level){

        Cursor cursor = null;

        List<BaseWord> baseWords = null;

        String max = String.valueOf((level * 100));

        String min = String.valueOf((level * 100) - 99);

        try {

            cursor = mDatabase.query(BASE_WORD_LIST_TABLE.NAME,null,"_id BETWEEN ? AND ?",new String[]{min,max},null,null,null,null);

            if(cursor != null){

                if(cursor.getCount()<1){

                    Log.e("my", "Word is not came from table BaseWord inside getBaseWordListAccordingToLevel");

                    return null;

                }else {

                    baseWords = new ArrayList<>();

                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()){

                        BaseWord baseWord = new BaseWord(cursor.getInt(0),cursor.getString(1),cursor.getString(2));

                        baseWords.add(baseWord);

                        cursor.moveToNext();
                    }

                }


            }else {

                Log.e(TAG,"query is null inside getBaseWordListAccordingToLevel");

            }

        }finally {

            if (cursor != null){

                cursor.close();

            }else {

                Log.e(TAG,"cursor is null inside getBaseWordListAccordingToLevel");

            }

        }

        return baseWords;
    }

    public List<BaseWord> getWordOptionsForTest(BaseWord baseWord){

        List<Integer> baseWordsIdForOptions = new ArrayList<>();

        List<BaseWord> baseWordsForOptions = new ArrayList<>();

        int correctWordId = baseWord.getId();

        int level;

        if(correctWordId%100==0){

            level = correctWordId/100;

        }else{

            level = (correctWordId/100)+1;

        }

        int max = level*100;

        int min = max-99;

        Random random = new Random();

        while (baseWordsIdForOptions.size()<3){

            int i = random.nextInt(100)+min;

            if(correctWordId != i){

                if(baseWordsIdForOptions.size()==0){

                    if(getSpecificBaseWord(i) != null){

                        baseWordsIdForOptions.add(i);

                    }

                }else{

                    int size = baseWordsIdForOptions.size();

                    boolean canAdd = true;

                    for(int m = 0 ; m < size ; m++){

                        if(i == baseWordsIdForOptions.get(m)){

                            canAdd = false;

                        }

                        if(getSpecificBaseWord(i) == null){

                            canAdd = false;

                            /*Log.e(TAG,"id does not exist in baseWord table -- "+i);*/

                        }

                    }

                    if(canAdd){

                        baseWordsIdForOptions.add(i);

                    }
                }

            }

        }

        for(Integer l : baseWordsIdForOptions){

            baseWordsForOptions.add(getSpecificBaseWord(l));

        }

        baseWordsForOptions.add(baseWord);

        /*Log.e("my",baseWordsForOptions.toString());*/

        return baseWordsForOptions;

    }

    public List<BaseWord> getBaseWordsForTest(){

        int i = 50;
        List<BaseWord> words = new ArrayList<>();

        while (i < 5000){

            BaseWord baseWord = getSpecificBaseWord(i);

            words.add(baseWord);

            i= i + 100;

        }

        return words;

    }

    private static ContentValues getContentValues(BaseWord word){

        ContentValues contentValues = new ContentValues();

        contentValues.put(BASE_WORD_LIST_TABLE.Columns.WORD,word.getWord());

        contentValues.put(BASE_WORD_LIST_TABLE.Columns.MEAN,word.getMean());

        return contentValues;

    }
}
