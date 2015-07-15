package com.project.zee.spotspotify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Zee on 11-07-2015.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> text;
    private final ArrayList<String> imageId;
    private ArrayList<String> albumNameArr;

    public CustomAdapter(Activity context, ArrayList<String> text, ArrayList<String> imageId) {
        super(context, R.layout.list_text, text);
        this.context = context;
        this.text = text;
        this.imageId = imageId;
    }

    public CustomAdapter(Activity context, ArrayList<String> text, ArrayList<String> imageId, ArrayList<String> albumNameList) {
        this(context, text, imageId);
        this.albumNameArr = albumNameList;
    }

    public void add(String artist, String image) {
        super.add(artist);
        imageId.add(image);
    }

    public void add(String track, String image, String albumName) {
        add(track, image);
        this.albumNameArr.add(albumName);
    }

    @Override
    public void clear() {
        super.clear();
        imageId.clear();
    }

    public void customClear() {
        clear();
        albumNameArr.clear();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView;
        if (context.getLocalClassName().equals(TopTen.class.getSimpleName())) {
            rowView = inflater.inflate(R.layout.list_text_t10, null, true);
            TextView album = (TextView) rowView.findViewById(R.id.list_item_album);
            album.setText(albumNameArr.get(position));
        }
        else {
            rowView = inflater.inflate(R.layout.list_text, null, true);
        }
        TextView txtTitle = (TextView) rowView.findViewById(R.id.list_item_text_view);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_item_image_view);
        txtTitle.setText(text.get(position));
        // if (imageId != null) {
        if (imageId.get(position) != null) {
            Picasso.with(context).load(imageId.get(position)).into(imageView);
        }
        else {
            imageView.setImageResource(R.drawable.artist);
        }
        // }
        return rowView;
    }

}
