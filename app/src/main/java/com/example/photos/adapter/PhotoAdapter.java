package com.example.photos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photos.R;
import com.example.photos.models.*;

import java.util.ArrayList;

public class PhotoAdapter extends ArrayAdapter<Photo> {
    ArrayList<Photo> photos;
    Context mContext;

    public PhotoAdapter (Context context, int textViewResourceId, ArrayList<Photo> photo) {
        super(context, textViewResourceId, photo);
        this.photos = photo;
        this.mContext=context;
    }

    private static class ViewHolder {
        TextView caption;
        ImageView thumbnail;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        Photo photo = getItem(position);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.photo_view, null);
            holder = new ViewHolder();
            holder.caption = (TextView) view.findViewById(R.id.caption);
            holder.thumbnail = (ImageView) view.findViewById(R.id.photo);
            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        holder.caption.setText(photo.getCaption());
        holder.thumbnail.setImageBitmap(photo.getImage());

        return view;
    }

}
