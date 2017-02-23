package com.example.kareemwaleed.newsreader;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kareem Waleed on 2/23/2017.
 */

public class StoriesAPI extends AsyncTask<String, Void, JSONArray> {
    @Override
    protected JSONArray doInBackground(String... urls) {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String result = "";

        try {
            url = new URL(urls[0]);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read();
            while (data != -1)
            {
                result += (char)data;
                data = inputStreamReader.read();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
