package com.example.android.newsappudacity;

public class Story {

    private String mTitle;
    private String mSection;
    private String mAuthor = "Unknown author";
    private long mDate = -1L;

    public Story(String title, String section, String author, long date){
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public long getDate() {
        return mDate;
    }
}
