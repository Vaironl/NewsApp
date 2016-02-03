package com.apps.doza.newsapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Vaironl on 2/3/2016.
 */
public class NYRecyclerViewAdapter extends RecyclerView.Adapter<NYRecyclerViewAdapter.ViewHolder> {

    private final String TAG = getClass().getName();

    private ArrayList<NewsObject> newsDataset;

    public NYRecyclerViewAdapter(ArrayList<NewsObject> newsObjects) {
        newsDataset = newsObjects;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView, authorTextView, descriptionTextView;
        public ImageView articleImage;

        public ViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.articleTitle);
            authorTextView = (TextView) v.findViewById(R.id.articleAuthor);
            descriptionTextView = (TextView) v.findViewById(R.id.articleDescription);
            articleImage = (ImageView) v.findViewById(R.id.articleImage);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleTextView.setText(newsDataset.get(position).getTitle());
        holder.authorTextView.setText(newsDataset.get(position).getAuthor());
        holder.descriptionTextView.setText(newsDataset.get(position).getDescription());

        try {
            URL url = new URL(newsDataset.get(position).getImageLink());
            if (url != null) {
                new DownloadImageTask(holder.articleImage)
                        .execute(newsDataset.get(position).getImageLink());
            }

        } catch (MalformedURLException ex) {
            Log.v(TAG, "There was a MalformedURLException.\nThe culprit: " + newsDataset.get(position).getImageLink());
//                    ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return newsDataset.size();
    }
}
