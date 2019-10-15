package com.example.android.newsappudacity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Story>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);
    }

    @Override
    public Loader<Story> onCreateLoader(int id, Bundle args) {
        return new StoryLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Story> loader, Story stories) {
        updateUI(stories);
    }

    @Override
    public void onLoaderReset(Loader<Story> loader) {
    }

    public void updateUI(Story story){

        TextView title = findViewById(R.id.title);
        title.setText(story.getTitle());

        TextView section = findViewById(R.id.section);
        section.setText(story.getSection());

        TextView author = findViewById(R.id.author);
        author.setText(story.getAuthor());

        TextView date = findViewById(R.id.date);
        date.setText(story.getDate() );
    }
}
