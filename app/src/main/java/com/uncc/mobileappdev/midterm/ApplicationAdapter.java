package com.uncc.mobileappdev.midterm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stephen on 3/12/2018.
 */

public class ApplicationAdapter extends ArrayAdapter<Application> {


    Context context;

    public static class ViewHolder{
        ImageView appImage;
        TextView appName;
        TextView artistName;
        TextView genre;
        TextView releaseDate;

        public TextView getGenre() {
            return genre;
        }

        public void setGenre(TextView genre) {
            this.genre = genre;
        }

        public TextView getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(TextView releaseDate) {
            this.releaseDate = releaseDate;
        }



        public ImageView getAppImage(){
            return appImage;
        }

        public void setAppImage(ImageView appImage){
            this.appImage = appImage;
        }
        public TextView getAppName() {
            return appName;
        }

        public void setAppName(TextView appName) {
            this.appName = appName;
        }

        public TextView getArtistName() {
            return artistName;
        }

        public void setArtistName(TextView artistName) {
            this.artistName = artistName;
        }

    }


    public ApplicationAdapter(@NonNull Context context, int resource, @NonNull List<Application> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Application app = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.application_inflator, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.setAppImage((ImageView) convertView.findViewById(R.id.app_image));
            viewHolder.setArtistName((TextView) convertView.findViewById(R.id.artist_name));
            viewHolder.setAppName((TextView) convertView.findViewById(R.id.app_name));
            viewHolder.setGenre((TextView) convertView.findViewById(R.id.genre));
            viewHolder.setReleaseDate((TextView) convertView.findViewById(R.id.release_date));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.getArtistName().setText(app.getReleaseDate());
        viewHolder.getAppName().setText(app.getAppName());
        viewHolder.getArtistName().setText(app.getArtistName());
        viewHolder.getReleaseDate().setText(app.getReleaseDate());

        ArrayList<String> genres = app.getGenres();
        Collections.sort(genres);
        StringBuilder sb = new StringBuilder();
        for(String str : genres){
            sb.append(str + ", ");
        }

        sb.deleteCharAt(sb.length()-2); //Remove the final comma

        viewHolder.getGenre().setText(sb.toString());
        Picasso.with(getContext()).load(app.getUrlToImage()).into(viewHolder.appImage);

        return convertView;

    }

}
