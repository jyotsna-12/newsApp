package com.example.hp.mynewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


//I have taken help from earthquake app which I learnt to made from Networking lesson
//to ignore the warnings used the suggestion appearing with yellow light
public class NewsListAdapter extends ArrayAdapter<News> {

    NewsListAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.newsitem, parent, false);
        }

        News currentNews = getItem(position);
        TextView newsTitle =listItem.findViewById(R.id.newstitle);
        assert currentNews != null;
        String title = currentNews.getmNewsTitle();
        newsTitle.setText(title);

        TextView newsDescription = listItem.findViewById(R.id.news_desc);
        String newsDescipt = currentNews.getmNewsDescription();
        newsDescription.setText(newsDescipt);

        TextView date = listItem.findViewById(R.id.date);
        String date1 = currentNews.getmdate();
        date.setText(date1);


        TextView author = listItem.findViewById(R.id.type);
        String type1 = currentNews.getmtype();
        author.setText(type1);


        return listItem;

    }   }