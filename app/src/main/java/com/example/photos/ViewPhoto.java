package com.example.photos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.photos.adapter.TagAdapter;
import com.example.photos.models.*;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ViewPhoto extends AppCompatActivity {
    private ArrayList<Album> albums;
    private Album currentAlbum;
    private int currentPhoto;
    private ListView listView;
    private TagAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
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
        currentPhoto = intent.getIntExtra("photoPosition", 0);

        currentAlbum.getPhotos().get(currentPhoto).getTags().clear();
        currentAlbum.getPhotos().get(currentPhoto).addTag("Location", "Home");

        //set listview
        try {
            adapter = new TagAdapter(this, R.layout.tag_item,
                    currentAlbum.getPhotos().get(currentPhoto).getTags());
            //adapter.setNotifyOnChange(true);
            listView = findViewById(R.id.tagList);
            if(listView != null){
                listView.setAdapter(adapter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        //listView.setItemChecked(0, true);

        //set photo
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = currentAlbum.getPhotos().get(currentPhoto).getImage();
        imageView.setImageBitmap(bitmap);
    }

    public void nextPhoto(View view){
        if(currentPhoto == currentAlbum.getPhotos().size() - 1) {
            currentPhoto = 0;
        }else {
            currentPhoto++;
        }
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = currentAlbum.getPhotos().get(currentPhoto).getImage();
        imageView.setImageBitmap(bitmap);
    }

    public void previousPhoto(View view){
        if(currentPhoto == 0) {
            currentPhoto = currentAlbum.getPhotos().size() - 1;
        }else {
            currentPhoto--;
        }
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = currentAlbum.getPhotos().get(currentPhoto).getImage();
        imageView.setImageBitmap(bitmap);
    }

    public void addTag(View view){
        final int[] choice = {-1};
        EditText tagname  = (EditText) findViewById(R.id.tagname);
        String tag = "";
        tag = tagname.getText().toString();
        final String[] tagtype = {""};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Tag");
        final String finalTag = tag;
        builder.setSingleChoiceItems(R.array.choices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice[0] = which;
            }
        })
        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(choice[0] == -1){
                    //user didn't pick location or person
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(builder.getContext());
                    builder1.setMessage("Please choose either Person or Location as tag type.");
                    builder1.show();

                }else if(finalTag.equals("")){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(builder.getContext());
                    builder1.setMessage("Please enter a tag name.");
                    builder1.show();
                }else{
                    if(choice[0] == 0){
                        tagtype[0] = "Person";
                    }else{
                        tagtype[0] = "Location";
                    }
                    if(currentAlbum.getPhotos().get(currentPhoto).addTag(tagtype[0], finalTag)){
                        adapter.add(new Tag(tagtype[0], finalTag));
                        //save
                        try {
                            FileOutputStream fos = getApplicationContext().openFileOutput("data.dat", Context.MODE_PRIVATE);
                            ObjectOutputStream os = new ObjectOutputStream(fos);
                            os.writeObject(albums);
                            os.close();
                            fos.close();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }else{
                        //tag already exists
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(builder.getContext());
                        builder1.setMessage("This tag already exists.");
                        builder1.show();
                    }
                }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    public void deleteTag(View view){

    }
}
