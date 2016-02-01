package com.apps.doza.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private ArrayList<String> titles;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        listView = (ListView) findViewById(R.id.listView);

        titles = new ArrayList<>();


        initHttp();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);


        listView.setAdapter(adapter);


//        appendLog(search.getResult());


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
                            JSONArray technology = object.getJSONArray("results");

                            Log.v(TAG, "Length: " + technology.length());
                            for (int index = 0; index < technology.length(); index++) {
                                titles.add(technology.getJSONObject(index).getString("title"));
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
