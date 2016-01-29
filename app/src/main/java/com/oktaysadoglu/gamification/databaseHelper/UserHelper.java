package com.oktaysadoglu.gamification.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oktaysadoglu.gamification.BuildConfig;
import com.oktaysadoglu.gamification.databaseScheme.UserDbScheme;
import com.oktaysadoglu.gamification.databaseScheme.UserDbScheme.*;
import com.oktaysadoglu.gamification.model.LastSeenWord;
import com.oktaysadoglu.gamification.model.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by oktaysadoglu on 10/12/15.
 */
public class UserHelper extends SQLiteOpenHelper {

    private static volatile UserHelper mUserHelper;

    private static String DB_PATH = "";

    private Context mContext;

    private static final int VERSION = 1;

    private SQLiteDatabase mDatabase;

    private static final String DATABASE_NAME = "user.db";

    public static UserHelper getInstance(Context context){

        if(mUserHelper == null){

            synchronized (UserHelper.class){

                if (mUserHelper == null){

                    mUserHelper = new UserHelper(context.getApplicationContext());

                }

            }

        }

        return mUserHelper;

    }

    private UserHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*db.execSQL("create table "+UserWordTable.NAME+"(" +
                "_id integer primary key autoincrement," +
                UserWordTable.Columns.BASE_WORD_ID+" integer,"+
                UserWordTable.Columns.NUMBER_OF_NOT_KNOW+" integer," +
                UserWordTable.Columns.Date+" text"+
                ");");




        db.execSQL("create table "+LogTable.NAME+"(" +
                "_id integer primary key autoincrement," +
                LogTable.Columns.LOG_NAME+" text," +
                LogTable.Columns.LOG_DESCRIPTION+" text," +
                LogTable.Columns.DATE+" text"+
                ");");

        setAllInitialWords(db);

        if (BuildConfig.DEBUG)
        {
            new File(db.getPath()).setReadable(true, false);

            //
            //   ./adb -d pull //data/data/com.oktaysadoglu.gamification/databases/user.db /Users/oktaysadoglu/Desktop/user.db
        }*/

        /*db.execSQL("create table "+LastSeenWordTable.NAME+"(" +
                "_id integer primary key autoincrement," +
                LastSeenWordTable.Columns.LEVEL+" integer," +
                LastSeenWordTable.Columns.LAST_BASE_WORD_ID+" integer" +
                ");");

        setAllInitialWords(db);*/

    }

/*    private void setAllInitialWords(SQLiteDatabase database){

        for(int i = 0;i <= 50 ; i++){

            ContentValues contentValues = getContentValues(new LastSeenWord(i,1));

            long id = database.insert(UserDbScheme.LastSeenWordTable.NAME,null,contentValues);

        }

    }

    private ContentValues getContentValues(LastSeenWord lastSeenWord) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(UserDbScheme.LastSeenWordTable.Columns.LEVEL, lastSeenWord.getLevel());

        contentValues.put(UserDbScheme.LastSeenWordTable.Columns.LAST_BASE_WORD_ID, lastSeenWord.getBaseWordId());

        return contentValues;

    }*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




    public void createDataBase() throws IOException {

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                android.util.Log.e("my", "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }



    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DATABASE_NAME;
        //Log.v("mPath", mPath);
        mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDatabase != null;
    }


    @Override
    public synchronized void close()
    {
        if(mDatabase != null)
            mDatabase.close();
        super.close();
    }
}
