package com.example.photos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.photos.adapter.PhotoAdapter;
import com.example.photos.models.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SearchPhotos extends AppCompatActivity {
    private String ttype = "";
    private ArrayList<Album> albums;
    private ArrayList<Photo> result = new ArrayList<Photo>();
    private ListView listView;
    private PhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photos);

        try {
            FileInputStream fis = this.openFileInput("data.dat");
            ObjectInputStream is = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        adapter = new PhotoAdapter(this, R.layout.photo_view, result);
        adapter.setNotifyOnChange(true);
        listView = findViewById(R.id.photoList);
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.person:
                if (checked)
                    ttype = "Person";
                    break;
            case R.id.location:
                if (checked)
                    ttype = "Location";
                    break;
        }
    }

    public void search(View view){
        result.clear();
        String tagValue = "";
        EditText tg  = (EditText) findViewById(R.id.tagValue);
        tagValue = tg.getText().toString();

        if(tagValue.equals("") || ttype.equals("")){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Please choose both a tag type and tag name.");
            builder1.show();
            return;
        }

        for(Album a: albums){
            for(Photo p: a.getPhotos()){
                for(Tag t: p.getTags()){
                    if(t.getName().equals(ttype) && t.getValue().contains(tagValue)){
                        result.add(p);
                    }
                }

            }
        }
        if(result.size() == 0){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("No photos with matching tag name and value pair.");
            builder1.show();
            return;
        }
        adapter.notifyDataSetChanged();
    }
}
