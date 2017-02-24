package com.example.kareemwaleed.newsreader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kareem Waleed on 2/23/2017.
 */

public class DatabaseController {
    private SQLiteDatabase database;

    public void createDatabase(Context context)
    {
        database = context.openOrCreateDatabase("Stories", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS stories (headline VARCHAR, url VARCHAR)");
    }

    public ArrayList<Pair<String, String>> getHeadlines()
    {
        ArrayList<Pair<String, String>> pairArrayList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM stories", null);
        int headlineIndex = cursor.getColumnIndex("headline");
        int urlIndex = cursor.getColumnIndex("url");
        Pair<String, String> temp = null;
        while (cursor.moveToNext())
        {
            temp = new Pair<>(cursor.getString(headlineIndex), cursor.getString(urlIndex));
            pairArrayList.add(temp);
        }
        return pairArrayList;
    }

    public void updateStories(ArrayList<Pair<String, String>> pairArrayList)
    {
        database.execSQL("DROP TABLE IF EXISTS stories");
        database.execSQL("CREATE TABLE stories (headline VARCHAR, url VARCHAR)");
        String temp;
        for(int i=0; i < pairArrayList.size(); i++)
        {
            temp = "INSERT INTO stories (headline, url) VALUES ('" + pairArrayList.get(i).first
                    + "', '" + pairArrayList.get(i).second + "')";
            database.execSQL(temp);
        }
    }
}
