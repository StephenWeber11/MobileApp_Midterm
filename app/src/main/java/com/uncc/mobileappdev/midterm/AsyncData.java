package com.uncc.mobileappdev.midterm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Stephen on 3/12/2018.
 */

public class AsyncData extends AsyncTask<String, Void, ArrayList<Application>> {

    ProgressDialog progressDialog;
    IData iDataActivity;
    Activity activity;
    ArrayList<String> globalGenresList = new ArrayList<>();

    public AsyncData(IData iDataActivity, Activity activity){
        this.iDataActivity = iDataActivity;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Articles...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected ArrayList<Application> doInBackground(String... params) {
        HttpURLConnection connection = null;
        ArrayList<Application> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();

            JSONObject root = new JSONObject(json);
            JSONObject feedObject = root.getJSONObject("feed");
            JSONArray appArray = feedObject.getJSONArray("results");
            for (int i = 0; i < appArray.length(); i++) {
                JSONObject appObject = appArray.getJSONObject(i);

                Application app = new Application();
                app.setArtistName(appObject.getString("artistName"));
                app.setAppName(appObject.getString("name"));
                app.setReleaseDate(appObject.getString("releaseDate"));
                app.setUrlToImage(appObject.getString("artworkUrl100"));
                app.setCopyright(appObject.getString("copyright"));

                JSONArray genreArray = appObject.getJSONArray("genres");
                ArrayList<String> genres = new ArrayList<>();
                for(int j=0; j < genreArray.length(); j++){
                    JSONObject genreName = genreArray.getJSONObject(j);
                    genres.add(genreName.getString("name"));

                    if(!globalGenresList.contains(genreName.getString("name"))){
                        globalGenresList.add(genreName.getString("name"));
                    }

                }
                app.setGenres(genres);

                result.add(app);

            }
        }catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } finally {
            if(connection != null){
                connection.disconnect();
            }
        }

        return result;
    }

    @Override
    protected  void onPostExecute(ArrayList<Application> result){
        if(result != null && !result.isEmpty()){
            iDataActivity.setupData(result, globalGenresList);
            for(Application app : result){
                Log.d("Demo", app.getAppName());
            }
            progressDialog.dismiss();
        } else {
            Log.d("Demo", "NULL result!");
            Toast toast = Toast.makeText(activity, "No News Found", Toast.LENGTH_SHORT);
            progressDialog.dismiss();
        }
    }

    static public interface IData {
        public void setupData(ArrayList<Application> result, ArrayList<String> globalGenres);
    }

}
