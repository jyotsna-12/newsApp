package com.example.hp.mynewsapp;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
//for the json and http request I have taken help from the earthquake app that i made following up the lessons.
class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils()
    {}

    static List<News>fetchdata(String url)
    {
        URL url_for_connection=  createUrl(url);
        String jsonResponse = null;

        try {
            jsonResponse = HttpRequest(url_for_connection);

        } catch(IOException e) {
            Log.e(LOG_TAG, "There is an HTTP connection issue", e);
        }


        return extractingDataFromJson(jsonResponse);
    }



    private static String HttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 );
            urlConnection.setConnectTimeout(15000 );
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;

    }



    private static List<News> extractingDataFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> list_of_all_news = new ArrayList<>();

        try {


            JSONObject baseJsonResponse = new JSONObject(newsJSON);
               JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = response.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {

                JSONObject properties = newsArray.getJSONObject(i);

                String title= properties.getString("webTitle") ;
                String description= properties.getString("sectionName") ;

                String date= properties.getString("webPublicationDate") ;

                JSONArray tags= properties.getJSONArray("tags");
                String authors ="";
                if(tags.length()!=0)
                {
                    JSONObject obj =  tags.getJSONObject(0);
                    authors =  obj.getString("webTitle");
                }

                String url = properties.getString("webUrl");

                News newnews = new News( title,description,date, authors, url);
                list_of_all_news.add(newnews);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);

        }
        return list_of_all_news;
    }
    private static URL createUrl(String url) {

        URL url_2 = null;
        try {
            url_2= new URL(url);
        } catch(MalformedURLException e)
        {
            Log.e(LOG_TAG,"URl not build");
        }

        return url_2;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}

