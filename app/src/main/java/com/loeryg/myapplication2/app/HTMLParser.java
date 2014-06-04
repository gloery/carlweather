package com.loeryg.myapplication2.app;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Gordon on 5/16/14.
 */

//Parses HTML for MoonScreen and stores it
public class HTMLParser {


    private HashMap<String, Object> data;
    private Date lastUpdated;

    public HTMLParser() {
        this.data = new HashMap<String, Object>();
        lastUpdated = null;
    }

    public void updateData(ArrayList<String> sunmoon) {
        if (sunmoon.size() < 8) {
            return;
        }
        data.put("Dawn", sunmoon.get(0));
        data.put("Dusk", sunmoon.get(1));
        data.put("Sunrise", sunmoon.get(2));
        data.put("Sunset", sunmoon.get(3));
        data.put("New", sunmoon.get(4));
        data.put("Waxing", sunmoon.get(5));
        data.put("Full", sunmoon.get(6));
        data.put("Waning", sunmoon.get(7));
        lastUpdated = Calendar.getInstance().getTime();
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }


    public Date getLastUpdated() {
        return this.lastUpdated;
    }
}
