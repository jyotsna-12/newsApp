package com.example.hp.mynewsapp;
class News {

    private String mNewsTitle;
    private String mNewsDescription;
    private String mdate;
    private String mUrl;
    private String mtype;

    News(String NewsTitle, String NewsDescription, String date, String type, String Url) {
        mNewsTitle = NewsTitle;
        mNewsDescription = NewsDescription;

        mtype = type;
        mdate=date;
        mUrl = Url;

    }

    String getmNewsTitle() {
        return mNewsTitle;
    }

    String getmtype() {
        return mtype;
    }
    String getmNewsDescription() {
        return mNewsDescription;
    }
    String getmUrl() {
        return mUrl;
    }
    String getmdate()
    {
        return mdate;
    }

}