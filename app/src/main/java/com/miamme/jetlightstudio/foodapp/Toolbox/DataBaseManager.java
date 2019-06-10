package com.miamme.jetlightstudio.foodapp.Toolbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.miamme.jetlightstudio.foodapp.Model.TodoItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DataBaseManager {

    SQLiteDatabase database;
    SQLiteManager sqLiteManager;
    public APIManager apiManager;
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
        apiManager = new APIManager();
        if (APIManager.isNetworkAvailable(context) && apiManager.getStatus() == AsyncTask.Status.PENDING) {
            JSONObject jsonObject = APIManager.itemToJSON(id, status, taskName);
            apiManager.execute("POST", "http://5924649e.ngrok.io", "/api/todo", jsonObject.toString());
        }
    }

    public void removeTask(int id) {
        database.execSQL("DELETE FROM " + dbTableName + " WHERE " + dbColumnId + "=" + id + ";");
    }

    public void updateTask(Boolean taskStatus, int id, String taskName) {
        ContentValues values = new ContentValues();
        values.put(dbColumnStatus, taskStatus);
        values.put(dbColumnName, taskName);
        database.update(dbTableName, values, dbColumnId + " = " + id, null);
        apiManager = new APIManager();
        if (APIManager.isNetworkAvailable(context) && apiManager.getStatus() == AsyncTask.Status.PENDING) {
            JSONObject jsonObject = APIManager.itemToJSON(id, taskStatus, taskName);
            apiManager.execute("PUT", "http://5924649e.ngrok.io", "/api/todo/" + id, jsonObject.toString());
        }
    }

    public ArrayList<TodoItem> readFromDB(boolean updateFromServer) throws ExecutionException, InterruptedException {
        apiManager = new APIManager();
        if (updateFromServer && APIManager.isNetworkAvailable(context) && apiManager.getStatus() == AsyncTask.Status.PENDING) {
            apiManager.execute("GET", "http://5924649e.ngrok.io", "/api/todo");
            String data = apiManager.get();
            if (!data.matches("")) {
                Log.e("Result", "GET response " + data);
                return apiManager.getTodoItemsList(data);
            } else {
                return readinLocalDB();
            }
        } else {
            return readinLocalDB();
        }
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

    public int getCurrentBiggestId() {
        String query = "SELECT MAX(" + dbColumnId + ") FROM " + dbTableName;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null)
            if (cursor.moveToFirst()) return cursor.getInt(0) + 1;
        return 0;
    }
}
