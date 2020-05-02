package com.example.photos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.photos.R;
import com.example.photos.models.Tag;

import java.util.ArrayList;

public class TagAdapter extends ArrayAdapter<Tag> {
    ArrayList<Tag> tag;

    public TagAdapter (Activity activity, int textViewResourceId, ArrayList<Tag> tags) {
        super(activity, textViewResourceId, tags);
        tag = tags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.tag_item, null);
        TextView textView = (TextView) v.findViewById(R.id.type);
        textView.setText(tag.get(position).getName() + ": ");
        TextView textView2 = (TextView) v.findViewById(R.id.name);
        textView2.setText(tag.get(position).getValue());
        return v;

    }
}
