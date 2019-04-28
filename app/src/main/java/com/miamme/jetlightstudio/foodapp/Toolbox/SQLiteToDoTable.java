package com.miamme.jetlightstudio.foodapp.Toolbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miamme.jetlightstudio.foodapp.Model.TodoItem;

import java.util.ArrayList;

/**
 * Created by oussama on 22/02/2018.
 */

public class SQLiteToDoTable extends SQLiteOpenHelper {

    private static final int dbVersion = 1;
    private static final String dbName = "task.db";
    public static final String dbTableName = "task";
    public static final String dbCulumnStatus = "status";
    public static final String dbCulumnName = "taskName";

    public SQLiteToDoTable(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "Create Table " + dbTableName +
                " (" + dbCulumnStatus + " Boolean, " + dbCulumnName + " Text " + ");";
        sqLiteDatabase.execSQL(query);
    }

    public void addTask(boolean status, String taskName) {
        ContentValues value = new ContentValues();
        value.put(dbCulumnStatus, status);
        value.put(dbCulumnName, taskName);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(dbTableName, null, value);

    }

    public void removeTask(String taskName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + dbTableName + " WHERE " + dbCulumnName + "=\"" + taskName + "\";");
    }

    public ArrayList<TodoItem> readFromDB() {
        ArrayList<TodoItem> tastksTemp = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + dbTableName;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(dbCulumnName)) != null) {
                TodoItem item = new TodoItem(c.getString(c.getColumnIndex(dbCulumnName)),
                        Boolean.getBoolean(c.getString(c.getColumnIndex(dbCulumnStatus))));
                tastksTemp.add(item);
            }
            c.moveToNext();
        }
        db.close();
        return tastksTemp;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + dbTableName);
        onCreate(sqLiteDatabase);
    }
}
