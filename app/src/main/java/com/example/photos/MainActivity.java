package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.example.photos.models.*;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Album> albums;

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
    }

    public void viewAlbum(View view){
        Intent intent = new Intent(this, ViewAlbum.class);
        //intent.putExtra("albums", albums);
        intent.putExtra("albumPosition", 0);
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
        Intent intent = new Intent(this, ViewAlbum.class);
        startActivity(intent);
    }
}
