package com.loeryg.myapplication2.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Erin on 4/29/14.
 */

//Parses RSS feeds using regular expressions and stores the parsed information
public class RSSParser {
    private ArrayList<String> params;
    private HashMap<String, Object> data;
    private boolean inFarenheit;
    private Date lastUpdated;

    public RSSParser(){
        this.data = new HashMap<String, Object>();
        this.inFarenheit = true;
        lastUpdated = null;
    }

//Switch from farenheit to celsius
    public void switchDegrees(){
        if (inFarenheit){
            inFarenheit = false;
        }
        else{
            inFarenheit = true;
        }

    }

    public boolean inFarenheit(){
        return inFarenheit;
    }
    public void setTitles(ArrayList<String> listOfTitles){
        this.params = listOfTitles;
    }

    public ArrayList<String> getParams(){
        return this.params;
    }

//Parses feed titles to retrieve and store weather data
    public void parse(){
        String matchPattern = "(Conditions as of:  )(.+)";
        Pattern p = Pattern.compile(matchPattern);
        Matcher m = p.matcher(params.get(1));
        while (m.find()){
            data.put("lastUpdated", m.group(2));
        }
        matchPattern = "(.*?)([0-9][0-9]?\\.?[0-9]?)(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(2));

        while (m.find()){
            data.put("fTemp", Float.parseFloat(m.group(2)));
        }

        matchPattern = "(.*)( [0-9][0-9]?\\.?[0-9]? )(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(2));

        while (m.find()){
            data.put("cTemp", Float.parseFloat(m.group(2)));
        }
        matchPattern = "(.*?)([0-9][0-9]?\\.?[0-9]?)(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(3));


        while (m.find()){
            data.put("fChill", Float.parseFloat(m.group(2)));
        }
        matchPattern = "(.*)( [0-9][0-9]?\\.?[0-9]? )(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(3));


        while (m.find()){
            data.put("cChill", Float.parseFloat(m.group(2)));
        }
        matchPattern = "(.*)([0-9]+)(.*)([0-9]+)(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(4));


        while (m.find()){
            data.put("windMph", Integer.parseInt(m.group(2)));
            data.put("windMps", Integer.parseInt(m.group(4)));
        }
        matchPattern = "(Current Wind Direction: )(.+)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(5));


        while (m.find()){
            data.put("windDir", m.group(2));
        }
        matchPattern = "(.*?)([0-9]+\\.?[0-9]*)(.*?)([0-9]+)(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(6));


        while (m.find()){
            data.put("barometricIn", Float.parseFloat(m.group(2)));
            data.put("barometricHpa", Integer.parseInt(m.group(4)));
        }
        matchPattern = "(.*?)([0-9][0-9]?\\.?[0-9]?)(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(7));


        while (m.find()){
            data.put("humidity", Float.parseFloat(m.group(2)));
        }
        matchPattern = "(.*?)([0-9][0-9]?\\.?[0-9]?[0-9]?)(.*?)([0-9][0-9]?\\.?[0-9]?[0-9]?)(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(8));


        while (m.find()){
            data.put("precipTodayInches", Float.parseFloat(m.group(2)));
            data.put("precipTodayCm", Float.parseFloat(m.group(4)));
        }
        m = p.matcher(params.get(9));


        while (m.find()){
            data.put("precipMonthInches", Float.parseFloat(m.group(2)));
            data.put("precipMonthCm", Float.parseFloat(m.group(4)));
        }
        matchPattern = "(.*?)(1st.*?)([0-9][0-9]?\\.?[0-9]?[0-9]?)(.*?)([0-9][0-9]?\\.?[0-9]?[0-9]?)(.*)";
        p = Pattern.compile(matchPattern);
        m = p.matcher(params.get(10));


        while (m.find()){
            data.put("precipYearInches", Float.parseFloat(m.group(3)));
            data.put("precipYearCm", Float.parseFloat(m.group(5)));
        }
        Date lastUpdated = Calendar.getInstance().getTime();
        this.lastUpdated = lastUpdated;
    }

    public Date getLastUpdated(){
        return this.lastUpdated;
    }

    public HashMap<String, Object> getData(){
        return this.data;
    }
}
