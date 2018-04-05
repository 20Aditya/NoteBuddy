package com.example.aditya.notebuddy;

/**
 * Created by aditya on 6/4/18.
 */

public class Details {

    private String name;

    private int imageresourceid;

    public Details(String notename, int filetype){

        name = notename;
        imageresourceid = filetype;
    }

    public String getName(){
        return name;
    }

    public int getImageresourceid(){
        return imageresourceid;
    }

}
