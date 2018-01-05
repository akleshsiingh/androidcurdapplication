package com.androidfizz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aklesh on 1/2/2018.
 */

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "personDB";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DbController.TBL_PERSON +
                " ( " + DbController.PERSON_ID + " INTEGER PRIMARY KEY ," +
                DbController .PERSON_NAME + " TEXT ," + DbController . PERSON_EMAIL + " TEXT, " +  DbController .PERSON_AGE + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " +  DbController.TBL_PERSON;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }


}
