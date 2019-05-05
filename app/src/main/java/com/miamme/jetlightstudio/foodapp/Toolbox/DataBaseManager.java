package com.miamme.jetlightstudio.foodapp.Toolbox;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miamme.jetlightstudio.foodapp.Model.TodoItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DataBaseManager {

    SQLiteDatabase database;
    SQLiteManager sqLiteManager;
    APIManager apiManager;
    Context context;

    String dbTableName = "task";
    String dbColumnStatus = "status";
    String dbColumnName = "taskName";
    String color;

    public DataBaseManager(Context context, String dbTableName, String color) {
        this.dbTableName = dbTableName;
        this.color = color;
        this.context = context;
        apiManager = new APIManager();

        SQLiteManager.dbTableName = dbTableName;
        sqLiteManager = new SQLiteManager(context);
        database = sqLiteManager.getWritableDatabase();
        String query = "Create Table IF NOT EXISTS " + dbTableName +
                " (" + dbColumnStatus + " Boolean, " + dbColumnName + " Text " + ");";
        database.execSQL(query);
    }

    public void addTask(boolean status, String taskName) {
        ContentValues value = new ContentValues();
        value.put(dbColumnStatus, status);
        value.put(dbColumnName, taskName);
        database.insert(dbTableName, null, value);
    }

    public void removeTask(String taskName) {
        database.execSQL("DELETE FROM " + dbTableName + " WHERE " + dbColumnName + "=\"" + taskName + "\";");
    }

    public void updateTask(Boolean taskStatus, String taskName) {
        ContentValues values = new ContentValues();
        values.put(dbColumnStatus, taskStatus);
        database.update(dbTableName, values, dbColumnName + " = '" + taskName + "'", null);
    }

    public ArrayList<TodoItem> readFromDB() throws ExecutionException, InterruptedException {
        if (APIManager.isNetworkAvailable(context)) {
            apiManager.execute("http://7a5b2443.ngrok.io", "/Api/Todo");
            System.out.println("Read");
            ArrayList<TodoItem> todoItems = apiManager.get();
            if (todoItems != null) return apiManager.get();
            else readinLocalDB();
        } else readinLocalDB();
        return null;
    }

    public ArrayList<TodoItem> readinLocalDB() {
        ArrayList<TodoItem> tastksTemp = new ArrayList<>();
        Cursor c = database.query(dbTableName, new String[]{dbColumnName, dbColumnStatus}, null, null, null, null, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(dbColumnName)) != null) {
                TodoItem item = new TodoItem(c.getString(c.getColumnIndex(dbColumnName)),
                        c.getInt(c.getColumnIndex(dbColumnStatus)) == 1, color);
                tastksTemp.add(item);
            }
            c.moveToNext();
        }
        return tastksTemp;
    }
}
