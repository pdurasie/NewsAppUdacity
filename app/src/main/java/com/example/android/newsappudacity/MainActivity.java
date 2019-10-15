package com.example.android.newsappudacity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();
    }

    public void updateUI(){

        Story story = null;
        try {
            story = QueryUtils.makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
