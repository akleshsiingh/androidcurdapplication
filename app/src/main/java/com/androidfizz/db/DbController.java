package com.androidfizz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.androidfizz.model.ModelPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aklesh on 1/2/2018.
 */

public class DbController extends DbHandler {
    public static final String TBL_PERSON = "tbl_person";
    public static final String PERSON_ID = "personID";
    public static final String PERSON_NAME = "personName";
    public static final String PERSON_EMAIL = "personEmail";
    public static final String PERSON_AGE = "personAge";

    public DbController(Context context) {
        super(context);
    }

    public boolean addPerson(ModelPerson person) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERSON_NAME, person.getName());
        values.put(PERSON_EMAIL, person.getEmail());
        values.put(PERSON_AGE, person.getAge());
        result = db.insert(TBL_PERSON, null, values) > 0;
        db.close();
        return result;
    }

    public List<ModelPerson> getAllPerson() {

        List<ModelPerson> mTempList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor mCursor = null;
        try {
            db = this.getReadableDatabase(); //GETTING READABLE DB INSTANCE
            String query = "Select * from " + TBL_PERSON;
            mCursor = db.rawQuery(query, null);
            if (mCursor != null && mCursor.getCount() > 0 && mCursor.moveToFirst()) {
                do {
                    int personID = mCursor.getInt(mCursor.getColumnIndexOrThrow(PERSON_ID));
                    String personName = mCursor.getString(mCursor.getColumnIndexOrThrow(PERSON_NAME));
                    String personEmail = mCursor.getString(mCursor.getColumnIndexOrThrow(PERSON_EMAIL));
                    String personAge = mCursor.getString(mCursor.getColumnIndexOrThrow(PERSON_AGE));
                    mTempList.add(new ModelPerson(personID, personName, personEmail, personAge));
                } while (mCursor.moveToNext());
            }
        } catch (SQLiteException se) {
            se.printStackTrace();
            mTempList = null;
        }
        if (mCursor != null)
            mCursor.close();
        if (db != null)
            db.close();
        return mTempList;
    }

    public boolean delete(int mPersonID) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        result = db.delete(TBL_PERSON, PERSON_ID + "=" + mPersonID, null) > 0;
        db.close();
        return result;
    }

    public boolean updatePersonRecord(ModelPerson person) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PERSON_NAME, person.getName());
        values.put(PERSON_EMAIL, person.getEmail());
        values.put(PERSON_AGE, person.getAge());
        result = db.update(TBL_PERSON, values, PERSON_ID + "=" + person.getPersonID(), null) > 0;
        db.close();
        return result;
    }
}
