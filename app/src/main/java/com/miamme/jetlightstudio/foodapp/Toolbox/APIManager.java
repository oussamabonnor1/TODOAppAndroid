package com.miamme.jetlightstudio.foodapp.Toolbox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.miamme.jetlightstudio.foodapp.Model.TodoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APIManager extends AsyncTask<String, Void, String> {
    String data = "";

    @Override
    protected String doInBackground(String... strings) {
        if (strings[0].matches("GET")) {
            GETTodoItems(strings[1], strings[2]);
        } else if (strings[0].matches("POST")) {
            POSTTodoItem(strings[1], strings[2], strings[3]);
        }
        return data;
    }

    public ArrayList<TodoItem> getTodoItemsList(String data) {
        ArrayList<TodoItem> todoItems = new ArrayList<>();
        try {
            data = data.replace("null", "");
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                todoItems.add(new TodoItem(json.getInt("id"), json.getString("task"), json.getBoolean("status"), "blue"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return todoItems;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void GETTodoItems(String path, String endpoint) {
        try {
            URL url = new URL(path + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            while (s != null) {
                s = bufferedReader.readLine();
                System.out.println("s is: " + s);
                if (s != null && data != null) data = data.concat(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void POSTTodoItem(String path, String endpoint, String data) {
        String urlString = path + endpoint; // URL to call
        OutputStream out = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
