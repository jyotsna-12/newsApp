package com.example.hp.mynewsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.*;

//I have taken help from earthquake app
public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>>{

    private TextView Notext;
    private NewsListAdapter mAdapter;


    private static final String URL="http://content.guardianapis.com/search?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newslist = findViewById(R.id.list);
        Notext = findViewById(R.id.no_text);
        newslist.setEmptyView(Notext);
        mAdapter = new NewsListAdapter(this, new ArrayList<News>());

         newslist.setAdapter(mAdapter);

        newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                assert currentNews != null;
                Uri NewsUri = Uri.parse(currentNews.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connection= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=  connection.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_bar);
            loadingIndicator.setVisibility(View.GONE);
            Notext.setText(R.string.no_internet_connection);
        }
    }


    @Override

        public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minNews = sharedPreferences.getString(getString(R.string.settings_min_news_key), getString(R.string.settings_min_news_default));
        String orderBy= sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));
        String section = sharedPreferences.getString(getString(R.string.section_key),getString(R.string.section_default));

        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("page-size", minNews);
        uriBuilder.appendQueryParameter("order-by",orderBy);
        if (!section.equals(getString(R.string.section_default))) {
            uriBuilder.appendQueryParameter("section", section);
        }



            return new NewsLoader(this, uriBuilder.toString());
        }





    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        View loadingIndicator = findViewById(R.id.loading_bar);
        loadingIndicator.setVisibility(View.GONE);
        Notext.setText(R.string.no_news);
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);

        }
    }

    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    }
