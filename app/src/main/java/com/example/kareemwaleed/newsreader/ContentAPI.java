package com.example.kareemwaleed.newsreader;

import android.os.AsyncTask;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Kareem Waleed on 2/23/2017.
 */

public class ContentAPI extends AsyncTask<JSONArray, Void, ArrayList<Pair<String, String>>> {
    @Override
    protected ArrayList<Pair<String, String>> doInBackground(JSONArray... params) {
        JSONArray jsonArray = params[0];
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        ArrayList<Pair<String, String>> pairArrayList = new ArrayList<>();
        for(int i=0; i < 10; i++)
        {
            try {
                url = new URL("https://hacker-news.firebaseio.com/v0/item/"+ jsonArray.get(i).toString() +".json?print=pretty");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                String result = "";
                while (data != -1)
                {
                    if(data != 10)
                    {
                        result += (char) data;
                    }
                    data = inputStreamReader.read();
                }
                JSONObject jsonObject = new JSONObject(result);
                String title = jsonObject.getString("title");
                String link = jsonObject.getString("url");
                title = title.replace("'" , "''");
                Pair<String, String> temp = new Pair<>(title, link);
                pairArrayList.add(temp);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pairArrayList;
    }
}
