package com.example.android.newsappudacity;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

public class StoryLoader extends AsyncTaskLoader<Story> {

    public StoryLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Story loadInBackground() {
        URL url = QueryUtils.createURL();
        String jsonString = "";
        try {
             jsonString = QueryUtils.getJsonResponse(url);
        }catch (IOException e){
            Log.e("StoryLoader", "IOException thrown by Loader", e);
        }
        return QueryUtils.parseJSON(jsonString);
    }
}
