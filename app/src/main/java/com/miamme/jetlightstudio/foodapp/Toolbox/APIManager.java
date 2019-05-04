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
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class APIManager extends AsyncTask<String, Void, Void> {
    String data;

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String path = strings[1];
            URL url = new URL(strings[0] + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s = "";
            while (s != null) {
                s = bufferedReader.readLine();
                data += s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println(data);
        /*ArrayList<TodoItem> tasks = new ArrayList<>();
        if (data != null) {
            try {
                data = data.replace("null", "");
                JSONArray jsonArray = new JSONArray(data);
                JSONObject json = (JSONObject) jsonArray.get(0);
                tasks.add(new TodoItem(json.getString("taskName"), json.getBoolean("status"), "blue"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //TODO: bring shit form Database here
        }*/
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
