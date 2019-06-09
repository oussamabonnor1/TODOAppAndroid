package com.miamme.jetlightstudio.foodapp.Toolbox;

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
    String dbColumnId = "id";
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
                " ("
                + dbColumnId + " Integer, "
                + dbColumnStatus + " Boolean, "
                + dbColumnName + " Text "
                + ");";
        database.execSQL(query);
    }

    public void addTask(int id, boolean status, String taskName) {
        ContentValues value = new ContentValues();
        value.put(dbColumnId, id);
        value.put(dbColumnStatus, status);
        value.put(dbColumnName, taskName);
        database.insert(dbTableName, null, value);
    }

    public void removeTask(int id) {
        database.execSQL("DELETE FROM " + dbTableName + " WHERE " + dbColumnId + "=" + id + ";");
    }

    public void updateTask(Boolean taskStatus, int id) {
        ContentValues values = new ContentValues();
        values.put(dbColumnStatus, taskStatus);
        database.update(dbTableName, values, dbColumnId + " = " + id + "", null);
    }

    public ArrayList<TodoItem> readFromDB() throws ExecutionException, InterruptedException {
        if (APIManager.isNetworkAvailable(context)) {
            apiManager.execute("GET", "http://880bd4df.ngrok.io", "/api/todo");
            String data = apiManager.get();
            if (data != null) {
                System.out.println("it works");
                ArrayList<TodoItem> todoItems = apiManager.getTodoItemsList(data);
                return todoItems;
            } else {
                readinLocalDB();
            }
        } else readinLocalDB();
        return null;
    }

    ArrayList<TodoItem> readinLocalDB() {
        ArrayList<TodoItem> tastksTemp = new ArrayList<>();
        Cursor c = database.query(dbTableName, new String[]{dbColumnId, dbColumnName, dbColumnStatus}, null, null, null, null, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex(dbColumnName)) != null) {
                TodoItem item = new TodoItem(c.getInt(c.getColumnIndex(dbColumnId)),
                        c.getString(c.getColumnIndex(dbColumnName)),
                        c.getInt(c.getColumnIndex(dbColumnStatus)) == 1,
                        color);
                tastksTemp.add(item);
            }
            c.moveToNext();
        }
        return tastksTemp;
    }
}
