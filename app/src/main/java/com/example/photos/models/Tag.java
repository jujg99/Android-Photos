package com.example.photos.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Tag implements Serializable, Cloneable {
    private static final long serialVersionUID = 3056383083068193385L;
    private String name;
    private String value;

    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return name + ": " + value;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
