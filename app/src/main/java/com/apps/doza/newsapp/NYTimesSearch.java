package com.apps.doza.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vaironl on 1/31/16.
 * <p/>
 * Uses the NYTimes API
 */
public class NYTimesSearch {

    private final String TAG = getClass().getName();

    //    private HttpsURLConnection urlConnection;'
    private OkHttpClient client;
    private Request request;

    //Constructor
    public NYTimesSearch() {

        URL url = null;
        try {
            url = new URL("http://api.nytimes.com/svc/topstories/v1/technology.json?api-key=0724a0f29652a6fb48b2e31833b20357:16:74247520");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        client = new OkHttpClient();
        request = new Request.Builder().url(url).build();


    }

    public String getResult() {

        String result = "";

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

                String result;
                result = response.body().string();
//                Log.v(TAG, result);

            }
        });

        String title = "";


        return result + title;
    }

}
