package com.example.kareemwaleed.newsreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayList<Pair<String, String>> pairArrayList;
    private ArrayAdapter viewAdapter;
    private ListView headlinesListView;
    private static DatabaseController storiesDatabase;
    private boolean databaseStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prepare();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.refresh:
                storiesDatabase.updateStories(updateStories());
                prepare();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepare()
    {
        storiesDatabase = new DatabaseController();
        databaseStatus = checkDatabaseStatus();
        storiesDatabase.createDatabase(this);
        if(!databaseStatus)
            storiesDatabase.updateStories(updateStories());
        pairArrayList = storiesDatabase.getHeadlines();
        ArrayList<String> stories = new ArrayList<>();
        for(int i=0; i < pairArrayList.size(); i++)
            stories.add(pairArrayList.get(i).first);
        viewAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stories);
        headlinesListView = (ListView) findViewById(R.id.newsListView);
        headlinesListView.setAdapter(viewAdapter);
        headlinesListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("Story URL", pairArrayList.get(position).second);
        startActivity(intent);
    }

    private ArrayList<Pair<String, String>> updateStories()
    {
        StoriesAPI storiesAPI = new StoriesAPI();
        ContentAPI contentAPI = new ContentAPI();
        JSONArray temp = null;
        ArrayList<Pair<String, String>> pairArrayList = null;
        try {
            temp = storiesAPI.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();
            pairArrayList = contentAPI.execute(temp).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return pairArrayList;
    }

    private boolean checkDatabaseStatus()
    {
        String[] databaseList = databaseList();
        if(databaseList == null || databaseList.length == 0)
            return false;
        return true;
    }
}
