package com.example.photos.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private static final long serialVersionUID = 4320237921141767959L;
    private String album;
    private ArrayList<Photo> photos;

    public Album (String album) {
        this.album = album;
        photos = new ArrayList<Photo>();
    }

    public String getAlbum() {
        return album;
    }

    public ArrayList<Photo> getPhotos(){
        return photos;
    }

    public void setAlbum(String newalbum) {
        album = newalbum;
    }

    public void addPhoto(Bitmap image, String caption) {
        photos.add(new Photo(image, caption));
    }

    public void addClonedPhoto(Photo p) {
        photos.add(p);
    }

    public void deletePhoto(Photo p) {
        photos.remove(p);
    }
}
