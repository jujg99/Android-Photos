package com.example.photos.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import static android.graphics.Bitmap.createBitmap;

public class Photo implements Serializable, Cloneable {
    private static final long serialVersionUID = -928455348061061924L;
    private int[] pixels;
    private int width;
    private int height;
    private String caption;
    private ArrayList<Tag> tags;

    public Photo(Bitmap image, String caption) {
        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width * height];
        image.getPixels(pixels,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
        this.caption = caption;
        tags = new ArrayList<Tag>();
    }

    public Bitmap getImage() {
        return createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String s) {
        caption = s;
    }

    public ArrayList<Tag> getTags(){
        return tags;
    }

    public void setTags(ArrayList<Tag> t) {
        tags = t;
    }

    public boolean addTag(String name, String value) {
        for(Tag t: tags) {
            if(t.getName().equals(name)) {
                //if the tag-value pair already exists
                if(t.getValue().equals(value)) {
                    return false;
                }
            }
        }
        //the tag name didn't exist
        tags.add(new Tag(name, value));
        return true;
    }

    public boolean deleteTag(String name) {
        for(Tag t: tags) {
            if(t.getName().equals(name)) {
                tags.remove(t);
                return true;
            }
        }
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Photo cloned = (Photo)super.clone();
        ArrayList<Tag> clonedtags = new ArrayList<Tag>();
        Iterator<Tag> iterator = tags.iterator();
        while(iterator.hasNext()){
            clonedtags.add((Tag) iterator.next().clone());
        }
        cloned.setTags(clonedtags);
        return cloned;
    }
}
