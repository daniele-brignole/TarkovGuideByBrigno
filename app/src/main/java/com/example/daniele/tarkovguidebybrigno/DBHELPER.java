package com.example.daniele.tarkovguidebybrigno;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.daniele.tarkovguidebybrigno.FeedReaderContract;

/**
 * Created by Daniele on 24/05/2018.
 */
final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "inventory";
        public static final String COLUMN_NAME_OBJECT = "oggetto";
        public static final String COLUMN_CHECK = "gotItem";
    }
}
public class DBHELPER extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_OBJECT + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_CHECK + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public DBHELPER(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedReaderContract.FeedEntry.COLUMN_NAME_OBJECT,"pistola");
        contentValues.put(FeedReaderContract.FeedEntry.COLUMN_CHECK,0);

        long result = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, contentValues);
        Log.d("result",""+result);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
}
