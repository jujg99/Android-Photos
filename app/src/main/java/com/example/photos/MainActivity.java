package com.example.photos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.example.photos.models.*;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Album> albums;
    private int selectedPosition;
    private ArrayList<String> arrayList;
    ListView albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File(getFilesDir(), "data.dat");
        if (!file.exists() || !file.isFile()) {
            try {
                file.createNewFile();
                albums = new ArrayList<Album>();
                albums.add(new Album("stock"));

                FileOutputStream fos = this.openFileOutput("data.dat", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(albums);
                os.close();
                fos.close();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }else{
            try {
                FileInputStream fis = this.openFileInput("data.dat");
                ObjectInputStream is = new ObjectInputStream(fis);
                albums = (ArrayList<Album>) is.readObject();
                is.close();
                fis.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        albumList=(ListView)findViewById(R.id.albumList);
        albumList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                selectedPosition = position;
            }
        });

        setDisplay();
    }

    public void setDisplay() {
        arrayList = new ArrayList<String>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        albumList.setAdapter(arrayAdapter);

        for (Album a : albums) {
            arrayList.add(a.getAlbum());
        }

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        albumList.setAdapter(arrayAdapter);
    }

    public void viewAlbum(View view){
        try {
            FileOutputStream fos = this.openFileOutput("data.dat", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(albums);
            os.close();
            fos.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Intent intent = new Intent(this, ViewAlbum.class);
        String str = albumList.getItemAtPosition(selectedPosition).toString();
        int i = 0;
        for (Album a : albums) {
            if (a.getAlbum().equals(str))
                i = albums.indexOf(a);
        }
        intent.putExtra("albumPosition", i);
        startActivity(intent);
    }

    public void searchPhotos(View view){
        //save data before switching activity
        try {
            FileOutputStream fos = this.openFileOutput("data.dat", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(albums);
            os.close();
            fos.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Intent intent = new Intent(this, SearchPhotos.class);
        startActivity(intent);
    }

    public void addAlbum(View view) {
        EditText albumToAdd  = findViewById(R.id.newAlbum);
        boolean bool = true;
        for (Album a : albums)
            if (a.getAlbum().equals(albumToAdd.getText().toString())) {
                bool = false;
                break;
            }
        if (bool) {
            albums.add(new Album(albumToAdd.getText().toString()));
            setDisplay();
        }
    }

    public void renameAlbum(View view) {
        EditText albumToRename = findViewById(R.id.renameText);
        if (albumToRename.getText().toString().equals("stock")) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("This album already exists. Please choose a new name.");
            builder1.show();
            return;
        }
        for (Album a: albums) {
            if (a.getAlbum().equals(albumToRename.getText().toString())) {
                //album already exists
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("This album already exists. Please choose a new name.");
                builder1.show();
                return;
            }
        }
        albumList=(ListView)findViewById(R.id.albumList);
        String a = albumList.getItemAtPosition(selectedPosition).toString();
        if (a.equals("stock")) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Cannot rename stock.");
            builder1.show();
            return;
        }
        for (Album album : albums) {
            if (album.getAlbum().equals(a))
                album.setAlbum(albumToRename.getText().toString());
        }
        setDisplay();
    }

    public void deleteAlbum(View view) {
        albumList=(ListView)findViewById(R.id.albumList);
        String str = albumList.getItemAtPosition(selectedPosition).toString();
        if (str.equals("stock")) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Cannot delete Stock.");
            return;
        }

        for (Album a: albums) {
            if (a.getAlbum().equals(str)) {
                albums.remove(a);
            }
        }
        setDisplay();
    }

}
