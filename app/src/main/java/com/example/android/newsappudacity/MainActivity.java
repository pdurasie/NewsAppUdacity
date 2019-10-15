package com.example.android.newsappudacity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Story>> {

    private StoryAdapter mAdapter;
    private TextView emptyView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyView = findViewById(R.id.empty_view);
        progressBar = findViewById(R.id.progress_bar);

        mAdapter = new StoryAdapter(this, new ArrayList<Story>());
        final ListView mList = findViewById(R.id.list_view);
        mList.setAdapter(mAdapter);

        //Check for internet connectivity, if there is, initialize a loader
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this);
        } else{
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
        }

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Send an implicit intent to open story in browser when clicked
                Story clickedItem =
                        (Story) mList.getItemAtPosition(position);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedItem.getUrl()));
                startActivity(i);
            }
        });
    }

    @Override
    public Loader<List<Story>> onCreateLoader(int id, Bundle args) {
        return new StoryLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> stories) {
        mAdapter.clear();
        progressBar.setVisibility(View.GONE);
        if (stories != null && !stories.isEmpty()) {
            mAdapter.addAll(stories);
        }else{
            emptyView.setText(R.string.empty_view);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.clear();
    }

}
