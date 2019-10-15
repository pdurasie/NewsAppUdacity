package com.example.android.newsappudacity;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    //private constructor, because we only need access to static methods
    private QueryUtils() {
    }

    public static URL createURL() {

        //as shown in StackOverflow question "Use URI builder in Android or create URL with variables"
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("tag", "environment/climate-change")
                .appendQueryParameter("api-key", "de12bde0-3860-421d-8ea1-ae3c3f53139b")
                .appendQueryParameter("show-tags", "contributor");

        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            Log.e("", "Error creating the URL", e);
        }
        return url;
    }

    public static String getJsonResponse(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            // Validate the server response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            } else {
                Log.e("", "Response code is not 200");
            }
        } catch (IOException e) {
            Log.e("", "IOException thrown in getJsonResponse", e);
        } finally {
            //close everything if they existed
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<Story> parseJSON(String fetchedJson) {

        ArrayList<Story> stories = new ArrayList<>();

        try {
            JSONObject guardianJsonString = new JSONObject(fetchedJson);
            JSONObject guardianResponse = guardianJsonString.getJSONObject("response");
            JSONArray guardianResults = guardianResponse.getJSONArray("results");

            for(int i = 0; i < guardianResults.length(); i++) {
                JSONObject currentResult = guardianResults.getJSONObject(i);

                //there can be multiple authors, so we use an ArrayList
                List<String> authors = new ArrayList<String>() {};
                String finalAuthors = "";
                String date = currentResult.getString("webPublicationDate");
                String title = currentResult.getString("webTitle");
                String section = currentResult.getString("sectionName");

                JSONArray currentResultTags = currentResult.getJSONArray("tags");
                if(currentResultTags.length() > 0) {
                    //we could have multiple authors and therefore need this for loop
                    for (int y = 0; y < currentResultTags.length(); y++) {
                        JSONObject tag = currentResultTags.getJSONObject(y);
                        String type = tag.getString("type");
                        //Just to make sure, as I don't know if it will always be a contributor
                        if (type.equals("contributor")) {
                            authors.add(tag.getString("webTitle"));
                        }
                    }
                }
                //concatenate all authors
                for(int x = 0; x < authors.size(); x++){
                    //if this condition is true, it means there are more authors to come
                    if(x < authors.size() -1) {
                        finalAuthors += authors.get(x) + ", ";
                    } else{
                        //here we are at the last or the only author and don't need a comma
                        finalAuthors += authors.get(x);
                    }
                }
                stories.add(new Story(title, section, finalAuthors, date));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Error parsing the JSON data");
        }
        return stories;
    }

}
