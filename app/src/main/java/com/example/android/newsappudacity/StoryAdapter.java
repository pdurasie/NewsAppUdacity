package com.example.android.newsappudacity;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StoryAdapter extends ArrayAdapter<Story> {

    public StoryAdapter(Activity context, ArrayList<Story> stories) {
        super(context, 0, stories);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }
        Story currentStory = getItem(position);

        TextView title = convertView.findViewById(R.id.title);
        title.setText(currentStory.getTitle());

        TextView section = convertView.findViewById(R.id.section);
        section.setText(currentStory.getSection());

        TextView author = convertView.findViewById(R.id.author);
        if (currentStory.getAuthor().equals("")) {
            author.setText(R.string.unknown_author);
        } else {
            author.setText(currentStory.getAuthor());
        }

        TextView date = convertView.findViewById(R.id.date);
        try {
            date.setText(formatDate(currentStory.getDate()));
        } catch (ParseException e) {
            Log.e("StoryAdapter", "Error formatting the time");
            date.setText(currentStory.getDate());
        }

        return convertView;
    }

    private String formatDate(String date) throws ParseException {
        SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date parsedDate = inputDate.parse(date);
        SimpleDateFormat outputDate = new SimpleDateFormat("HH:mm, dd. LLL, yyyy");
        return outputDate.format(parsedDate);
    }
}
