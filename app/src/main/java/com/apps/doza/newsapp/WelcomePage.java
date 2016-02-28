package com.apps.doza.newsapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private final String[] sections =
            {
                    "Home",
                    "World",
                    "National",
                    "Politics",
                    "Nyregion",
                    "Business",
                    "Opinion",
                    "Technology",
                    "Science",
                    "Health",
                    "Sports",
                    "Arts",
                    "Fashion",
                    "Dining",
                    "Travel",
                    "Magazine",
                    "Realestate"};

    private String selectedSection = "science";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_welcome_page);
        setContentView(R.layout.recycler_view_holder);

        this.setTitle(selectedSection);

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

        initHttp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.section_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sectionSelectItem:

                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("Select a Section");
                b.setItems(sections, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int selected) {
                        dialog.dismiss();
                        selectedSection = sections[selected].toLowerCase();
                        setTitle(selectedSection);
                        initHttp();
                    }

                });

                b.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initHttp() {
        URL url = null;
        try {
            String link = "http://api.nytimes.com/svc/topstories/v1/" + "technology" + ".json?api-key=" + APIKEY.KEY;
            url = new URL(link);
            Log.e(TAG, link);
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
                            JSONObject object = new JSONObject(result);
                            JSONArray resultsArray = object.getJSONArray("results");

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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
