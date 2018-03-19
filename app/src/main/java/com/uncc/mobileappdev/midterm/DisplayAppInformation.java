package com.uncc.mobileappdev.midterm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class DisplayAppInformation extends AppCompatActivity {

    TextView appName;
    TextView releaseDate;
    ImageView appImage;
    TextView genres;
    TextView appDev;
    TextView copyright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_app_information);
        setTitle("App Details");

        appName = findViewById(R.id.app_name_display);
        releaseDate = findViewById(R.id.release_date_display);
        appImage = findViewById(R.id.app_image_display);
        genres = findViewById(R.id.genre_display);
        appDev = findViewById(R.id.dev_display);
        copyright = findViewById(R.id.copyright_display);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Application app = (Application) bundle.getSerializable(MainActivity.INTENT_APPLICATION);

        appName.setText(app.getAppName());
        releaseDate.setText(app.getReleaseDate());
        appDev.setText(app.getArtistName());
        copyright.setText(app.getCopyright());

        ArrayList<String> genresList = app.getGenres();
        Collections.sort(genresList);
        StringBuilder sb = new StringBuilder();
        for(String str : genresList){
            sb.append(str + ", ");
        }

        sb.deleteCharAt(sb.length()-2); //Remove the final comma

        genres.setText(sb.toString());

        Picasso.with(this).load(app.getUrlToImage()).into(appImage);

    }
}
