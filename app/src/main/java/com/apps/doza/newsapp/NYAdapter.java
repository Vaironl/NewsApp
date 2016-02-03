package com.apps.doza.newsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by vaironl on 2/2/16.
 */
public class NYAdapter extends ArrayAdapter<NewsObject> {

    private final String TAG = getClass().getName();

    public NYAdapter(Context context, int resource, ArrayList<NewsObject> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.news_row, null);
        }

        NewsObject object = getItem(position);

        if (object != null) {
            TextView articleTitleView = (TextView) v.findViewById(R.id.articleTitle);
            TextView articleAuthorView = (TextView) v.findViewById(R.id.articleAuthor);
            TextView articleDescriptionView = (TextView) v.findViewById(R.id.articleDescription);
            ImageView articleImage = (ImageView) v.findViewById(R.id.articleImage);

            if (articleTitleView != null) {
                articleTitleView.setText(object.getTitle());
            }
            if (articleAuthorView != null) {
                articleAuthorView.setText(object.getAuthor());
            }
            if (articleDescriptionView != null) {
                articleDescriptionView.setText(object.getDescription());
            }

            if (articleImage != null) {
                /*
                WARNING: THE FOLLOWING WILL FREEZE THE UI THREAD DEPENDING ON THE IMAGE DOWNLOAD
                FIND A MORE ELEGANT SOLUTION

                 */

                try {
                    URL url = new URL(object.getImageLink());
                    if (url != null) {
                        new DownloadImageTask(articleImage)
                                .execute(object.getImageLink());
                    }

                } catch (MalformedURLException ex) {
                    Log.v(TAG, "There was a MalformedURLException.\nThe culprit: " + object.getImageLink());
//                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }

        return v;
    }


}
