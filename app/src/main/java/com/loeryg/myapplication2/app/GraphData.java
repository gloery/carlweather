package com.loeryg.myapplication2.app;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Erin on 5/25/2014.
 */

//Graph data object that stores an array of graph bitmaps
public class GraphData {
    ArrayList<Bitmap> bitmaps;
    Date lastUpdated;

    public GraphData(){
        this.bitmaps = new ArrayList<Bitmap>();
        this.lastUpdated = null;
    }

    public void setBitmap(ArrayList<Bitmap> b){
        this.bitmaps = b;
        this.lastUpdated = Calendar.getInstance().getTime();
    }

    public ArrayList<Bitmap> getBitmaps(){
        return this.bitmaps;
    }

    public Date getLastUpdated(){
        return this.lastUpdated;
    }
}
