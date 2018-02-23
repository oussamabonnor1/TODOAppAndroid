package com.miamme.jetlightstudio.foodapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by oussama on 22/02/2018.
 */

public class SQLiteHelperMine extends SQLiteOpenHelper {

    private static final int dbVersion = 1;
    private static final String dbName = "task.db";
    public static final String dbTableName = "task";
    public static final String dbCulumnID = "_id";
    public static final String dbCulumnName = "taskName";

    public SQLiteHelperMine(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "Create Table " + dbTableName +
                " (" + dbCulumnID + " Integer Primary Key Autoincrement, " + dbCulumnName + " Text " + ");";
        sqLiteDatabase.execSQL(query);
    }

    public void addTask(String taskName) {
        ContentValues value = new ContentValues();
        value.put(dbCulumnName, taskName);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(dbTableName, null, value);

    }

    public void removeTask(String taskName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + dbTableName + " WHERE " + dbCulumnName + "=\"" + taskName + "\";");
    }

    public ArrayAdapter<String> readFromDB(Context context) {
        ArrayList<String> tastksTemp = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + dbTableName;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(dbCulumnName)) != null) {
                tastksTemp.add(c.getString(c.getColumnIndex(dbCulumnName)));
            }
            c.moveToNext();
        }
        ArrayAdapter<String> result = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tastksTemp);
        db.close();
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + dbTableName);
        onCreate(sqLiteDatabase);
    }
}
