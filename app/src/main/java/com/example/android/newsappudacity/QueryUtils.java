package com.example.android.newsappudacity;

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

public class QueryUtils {

    //private constructor, because we only need access to static methods
    private QueryUtils(){}

    final static String SAMPLE_URL = "https://content.guardianapis.com/search?api-key=de12bde0-3860-421d-8ea1-ae3c3f53139b&show-tags=contributor";

    public static URL createURL(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("", "Error creating the URL", e);
        }
        return url;
    }

    public static String getJsonResponse(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            // Validate the server response
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            }else{
                Log.e("", "Response code is not 200" );
            }
        }catch (IOException e){
            Log.e("", "IOException thrown in getJsonResponse", e);
        } finally{
            //close everything if they existed
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readInputStream(InputStream inputStream)throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader streamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static Story parseJSON(String fetchedJson) {
        try {
            JSONObject guardianJsonString = new JSONObject(fetchedJson);
            JSONObject guardianResponse = guardianJsonString.getJSONObject("response");
            JSONArray guardianResults = guardianResponse.getJSONArray("results");
            JSONObject currentResult = guardianResults.getJSONObject(0);

            String author = "";
            String date = currentResult.getString("webPublicationDate");
            String title = currentResult.getString("webTitle");
            String section = currentResult.getString("sectionName");

            JSONArray currentResultTags = currentResult.getJSONArray("tags");
            for (int i = 0; i < currentResultTags.length(); i++) {
                JSONObject tag = currentResultTags.getJSONObject(i);
                String type = tag.getString("type");
                if (type.equals("contributor")){
                    author = tag.getString("webTitle");
                    //the author tag is all we need, so we break the for loop
                    break;
                }
            }

            return new Story(title, section, author, date);

        } catch (JSONException e) {
            Log.e("QueryUtils", "Error parsing the JSON data");
        }
        return null;
    }

}
