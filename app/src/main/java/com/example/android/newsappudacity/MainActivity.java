package com.example.android.newsappudacity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<Story>>{

    private StoryAdapter mAdapter;
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new StoryAdapter(this, new ArrayList<Story>());
        mList = findViewById(R.id.list_view);
        mList.setAdapter(mAdapter);

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);

        final ListView storyListView = findViewById(R.id.list_view);

        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story clickedItem =
                        (Story) storyListView.getItemAtPosition(position);
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

        if (stories != null && !stories.isEmpty()) {
            mAdapter.addAll(stories);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mAdapter.clear();
    }

}
