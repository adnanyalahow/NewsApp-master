package com.example.yalahow.newsApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adnan Yalahow  on 7/8/2018.
 */

public class NewsAdapter extends ArrayAdapter<News>{
    public static final String LOG_TAG = NewsAdapter.class.getName();

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_iew, parent, false);
        }
        //get the currentNews item at specific position
        News currentNews = getItem(position);

        // Find the TextView with view ID web_title
        TextView webTitleTextView = (TextView) listItemView.findViewById(R.id.web_title);
        webTitleTextView.setText(currentNews.getWebTitle());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);

        // Display the date of the current earthquake in that TextView
        dateView.setText(currentNews.getPublishedDate());

        // Find the TextView with view ID sectionName
        TextView webUrlTextView = (TextView) listItemView.findViewById(R.id.sectionName);
        webUrlTextView.setText(currentNews.getSectionName());


        return listItemView;
    }


}
