package com.miamme.jetlightstudio.foodapp.Toolbox;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.miamme.jetlightstudio.foodapp.Model.TodoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APIManager extends AsyncTask<String, Void, ArrayList<TodoItem>> {
    String data = "";

    @Override
    protected ArrayList<TodoItem> doInBackground(String... strings) {
        try {
            String path = strings[1];
            URL url = new URL(strings[0] + path);
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
        return new ArrayList<>();
    }


    @Override
    protected void onPostExecute(ArrayList<TodoItem> todoItems) {
        super.onPostExecute(todoItems);
        System.out.println(data);
        if (data != null) {
            try {
                System.out.println("todoitems");
                data = data.replace("null", "");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);
                    todoItems.add(new TodoItem(json.getString("taskName"), json.getBoolean("status"), "blue"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
