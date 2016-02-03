package com.apps.doza.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomePage extends AppCompatActivity {

    private String TAG = getClass().getName();

    //    private TextView logTV;
    private OkHttpClient client;
    private Request request;
    private ListView listView;
    private RecyclerView newsRecyclerView;
    private ArrayList<NewsObject> newsObjects;
    //    private NYAdapter adapter;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_welcome_page);
        setContentView(R.layout.recycler_view_holder);

        newsObjects = new ArrayList<>();

        newsRecyclerView = (RecyclerView) findViewById(R.id.newsRecyclerView);

        //User a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(layoutManager);

        //Recycler view adapter
        adapter = new NYRecyclerViewAdapter(newsObjects);
        newsRecyclerView.setAdapter(adapter);


        //Use this if ou know that changes in the content do not change the layout size of the Recyclerview
//        newsRecyclerView.setHasFixedSize(true);


//        listView = (ListView) findViewById(R.id.listView);
        initHttp();
//        adapter = new NYAdapter(this, R.layout.news_row, newsObjects);
//        listView.setAdapter(adapter);

    }

    private void initHttp() {
        URL url = null;
        try {
            url = new URL("http://api.nytimes.com/svc/topstories/v1/technology.json?api-key=" + APIKEY.KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        client = new OkHttpClient();
        request = new Request.Builder().url(url).build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected: " + response);
                }

                final String result;
                result = response.body().string();


                WelcomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
//                            JSONArray array = new JSONArray(result);
                            JSONObject object = new JSONObject(result);
                            JSONArray resultsArray = object.getJSONArray("results");

//                            Log.v(TAG, "Length: " + resultsArray.length());
                            for (int index = 0; index < resultsArray.length(); index++) {
                                JSONObject technologyArticle = resultsArray.getJSONObject(index);

                                String articleTitle = technologyArticle.getString("title");
                                String articleDescription = technologyArticle.getString("abstract");
                                String articleAuthor = technologyArticle.getString("byline");
                                Object multimedia = technologyArticle.get("multimedia");

                                if (multimedia.toString().isEmpty()) {
                                    newsObjects.add(new NewsObject(articleTitle, null, articleAuthor, articleDescription));
                                } else {
                                    //If there is an image link get it
                                    if (multimedia.toString().contains("Normal")) {
                                        JSONArray multimediaCast = (JSONArray) multimedia;
                                        JSONObject imageLink = multimediaCast.getJSONObject(2);
                                        String imageURL = imageLink.getString("url");
                                        newsObjects.add(new NewsObject(articleTitle, imageURL, articleAuthor, articleDescription));
                                    } else if (multimedia.toString().contains("url")) {

                                        JSONArray multimediaCast = (JSONArray) multimedia;
                                        JSONObject imageLink = multimediaCast.getJSONObject(0);
                                        String imageURL = imageLink.getString("url");
                                        newsObjects.add(new NewsObject(articleTitle, imageURL, articleAuthor, articleDescription));

                                    }
                                }

                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        appendLog(result);


                    }
                });


//                Log.v(TAG, result);

            }
        });
    }


}
