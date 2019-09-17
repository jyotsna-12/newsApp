package com.example.hp.mynewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;


public class NewsLoader extends AsyncTaskLoader<List<News>>{

    private String mUrl;

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<News> loadInBackground() {
        if(mUrl==null)
          return null;

        return QueryUtils.fetchdata(mUrl);
    }
        }





