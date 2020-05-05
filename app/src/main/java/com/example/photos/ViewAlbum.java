package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.example.photos.adapter.PhotoAdapter;
import com.example.photos.models.*;

public class ViewAlbum extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    private ArrayList<Album> albums;
    private Album currentAlbum;
    private ListView imageList;
    private ArrayList<Photo> images;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album);
        Intent intent = getIntent();
        try {
            FileInputStream fis = this.openFileInput("data.dat");
            ObjectInputStream is = new ObjectInputStream(fis);
            albums = (ArrayList<Album>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        int albumPosition = intent.getIntExtra("albumPosition", 0);
        currentAlbum = albums.get(albumPosition);
        imageList = findViewById(R.id.imageList);
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener()
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
        images = new ArrayList<Photo>();

        for (Photo p : currentAlbum.getPhotos()) {
            images.add(p);
        }

        PhotoAdapter adapter = new PhotoAdapter(this, R.layout.photo_view, images);
        adapter.setNotifyOnChange(true);

        imageList.setAdapter(adapter);
        imageList.setItemChecked(0, true);
    }

    public void addPhoto(View view){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                Bitmap bitmap = null;
                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                String caption = uri.getLastPathSegment();


                ArrayList<Photo> arrayList = new ArrayList<>();
                for (Photo p : currentAlbum.getPhotos()) {
                    arrayList.add(p);
                }
                PhotoAdapter adapter = new PhotoAdapter(this, R.layout.photo_view,arrayList);
                adapter.setNotifyOnChange(true);
                ListView listView = findViewById(R.id.imageList);
                listView.setAdapter(adapter);
                listView.setItemChecked(0, true);

                //imageView.setImageBitmap(bitmap);

                // Do something with the bitmap

                currentAlbum.addPhoto(bitmap, caption);
                //save
                try {
                    FileOutputStream fos = this.openFileOutput("data.dat", Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(albums);
                    os.close();
                    fos.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public void viewPhoto(View view){
        Intent intent = new Intent(this, ViewPhoto.class);

        //intent.putExtra("album", albums);
        intent.putExtra("albumPosition", 1);
        intent.putExtra("photoPosition", 0);
        startActivity(intent);
    }

    public void deletePhoto(View view) {
        if (currentAlbum.getPhotos().size() == 0)
            return;
        currentAlbum.getPhotos().remove(currentAlbum.getPhotos().get(selectedPosition));
        setDisplay();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
