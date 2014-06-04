package com.loeryg.myapplication2.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Erin on 4/29/14.
 */

//Fetches data from the Carleton RSS Feed and displays it
public class RSSFetchTask extends AsyncTask<View ,Void, Pair<View, ArrayList<String>>> {


    public Activity mainActivity;

    public RSSFetchTask(Activity activity){
        this.mainActivity = activity;
        mainActivity = activity;
    }

    @Override
    protected Pair<View, ArrayList<String>> doInBackground(View ... params) {
        InputStream in = null;
        String rssFeed = "";
        ArrayList<String> listOfTitles = new ArrayList<String>();
        RSSParser r = ((MainActivity)this.mainActivity).getParser();
        Date curDate = Calendar.getInstance().getTime();
        long now = curDate.getTime();
        Date storeDate = r.getLastUpdated();
        if (storeDate != null){
            long then = r.getLastUpdated().getTime();
            if (now < then + 120000) {
                return Pair.create(params[0], listOfTitles);
            }
        }
        try{
            Document doc = Jsoup.connect("http://weather.carleton.edu/weather.xml").get();
            Elements titles = doc.select("title");

            for (Element e : titles){
                String t = e.text();

                listOfTitles.add(t);
            }



        } catch (IOException e) {
            ArrayList<String> errorList = new ArrayList<String>();
            errorList.add("ERROR");
            e.printStackTrace();
            return Pair.create(params[0], errorList);
        }
        return Pair.create(params[0], listOfTitles);
    }

    @Override
    protected void onPostExecute(Pair<View, ArrayList<String>> result) {
        View v = result.first;
        ArrayList<String> titles = result.second;
        RSSParser s = ((MainActivity)this.mainActivity).getParser();
        //If blank data is passed from doOnBackground, get data from MainActivity
        if (titles.isEmpty() == false){
            if (titles.get(0) == "ERROR"){
                return;
            }
            else {
                s.setTitles(titles);
                s.parse();
                ((MainActivity) this.mainActivity).setParser(s);
            }
        }
        HashMap<String, Object> ourData = s.getData();
        TextView d = (TextView) v.findViewById(R.id.degrees);
        TextView tv = (TextView) v.findViewById(R.id.ftempview);
        TextView fl = (TextView) v.findViewById(R.id.feelslike);
        TextView lu = (TextView) v.findViewById(R.id.mscreenupdated);
        Button b = (Button) v.findViewById(R.id.degreeswap);

        if (s.inFarenheit()){
            Integer fTemp = Math.round((Float) ourData.get("fTemp"));
            Integer fChill = Math.round((Float) ourData.get("fChill"));
            String f = fTemp.toString();
            fl.setText("Feels like: " + " " + fChill.toString() + " °F");
            d.setText("°F");
            tv.setText(f);
            b.setText("°F to °C");

        }
        else{
            Integer cTemp = Math.round((Float) ourData.get("cTemp"));
            Integer cChill = Math.round((Float) ourData.get("cChill"));
            String c = cTemp.toString();
            fl.setText("Feels like: " + " " + cChill.toString() + " °C");
            d.setText("°C");
            tv.setText(c);
            b.setText("°C to °F");
        }
        lu.setText("Last Updated: " + ourData.get("lastUpdated").toString());
        ProgressBar p = (ProgressBar) v.findViewById(R.id.progressBarM);
        p.setVisibility(View.GONE);
        fl.setVisibility(View.VISIBLE);
        d.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);



    }



}

