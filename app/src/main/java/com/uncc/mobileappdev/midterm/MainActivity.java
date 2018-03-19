package com.uncc.mobileappdev.midterm;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements AsyncData.IData{

    private static final String API_ENDPOINT = "https://rss.itunes.apple.com/api/v1/us/ios-apps/top-grossing/all/50/explicit.json";
    public static final String INTENT_APPLICATION = "App";

    private ArrayList<Application> apps = new ArrayList<>();
    private ArrayList<String> globalGenres = new ArrayList<>();
    private String selectedGenre = "All";

    private TextView genreText;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Apps");

        new AsyncData(this, this).execute(API_ENDPOINT);

        genreText = findViewById(R.id.genre_selection);
        listView = (ListView)findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DisplayAppInformation.class);
                Application app = apps.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(INTENT_APPLICATION, app);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button filter = findViewById(R.id.filter_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(globalGenres);

            }
        });

    }

    private void showPopup(ArrayList<String> genres){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Choose Genre");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);

        if(!genres.contains("All")){
            Collections.sort(genres);
            genres.set(0, "All"); //Add ALL as the first entry in the list.
        }

        for(String str : genres){
            arrayAdapter.add(str);
            Log.d("Keyword", str);
        }

        alert.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedGenre = arrayAdapter.getItem(which);
                genreText.setText(selectedGenre);

                //new AsyncData(MainActivity.this, MainActivity.this).execute(API_ENDPOINT);
                setupData(MainActivity.this.apps, MainActivity.this.globalGenres);
                dialog.dismiss();
            }
        });

        alert.show();
    }

    @Override
    public void setupData(ArrayList<Application> result, ArrayList<String> globalGenres) {
        ArrayList<Application> selectedApps;
        if("All".equals(selectedGenre)){
            selectedApps = new ArrayList<>(result);
            this.apps = result;
        } else {
            selectedApps = new ArrayList<>();
            for(Application app : result){
                if(app.getGenres().contains(selectedGenre)){
                    selectedApps.add(app);
                }
            }
        }

        ApplicationAdapter adapter = new ApplicationAdapter(this, R.layout.activity_main, selectedApps);
        listView.setAdapter(adapter);
        this.globalGenres = globalGenres;
    }
}
