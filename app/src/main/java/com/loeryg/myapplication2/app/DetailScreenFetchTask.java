package com.loeryg.myapplication2.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
 * Created by Erin on 5/24/2014.
 */
public class DetailScreenFetchTask extends AsyncTask<View,Void, Pair<View, ArrayList<String>>> {
//we should probably rename this

    public Activity mainActivity;
    public DetailScreenFetchTask(Activity activity){
        this.mainActivity = activity;
    }

    @Override
    protected Pair<View, ArrayList<String>> doInBackground(View... params) {
        InputStream in = null;
        String rssFeed = "";
        ArrayList<String> listOfTitles = new ArrayList<String>();
        RSSParser r = ((MainActivity)this.mainActivity).getParser();

        //If the data object is already current, pass blank data to onPostExecute
        Date curDate = Calendar.getInstance().getTime();
        long now = curDate.getTime();
        Date storeDate = r.getLastUpdated();
        if (storeDate != null){
            long then = r.getLastUpdated().getTime();
            if (now < then + 120000) {
                System.out.println(now);
                System.out.println(then);
                System.out.println("DETAILSCREENFETCHTASK called data already in MainActivity");
                return Pair.create(params[0], listOfTitles);
            }
            else{
                System.out.println("nope");
            }
        }
        //Else, download current data
        try {
            Document doc = Jsoup.connect("http://weather.carleton.edu/weather.xml").get();
            Elements titles = doc.select("title");

            for (Element e : titles) {
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
    //Parses retrieved data and sets up the screen
    protected void onPostExecute(Pair<View, ArrayList<String>> result) {
        RSSParser s = ((MainActivity) this.mainActivity).getParser();
        View v = result.first;
        ArrayList<String> titles = result.second;
        //If blank data has been passed, there is a current data object stored in the Main Activity.
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
        TextView dtemp = (TextView) v.findViewById(R.id.detailtemp);
        HashMap<String, Object> data = s.getData();
        Button b =  (Button) v.findViewById(R.id.degreeswap2);
        TextView feelslike = (TextView) v.findViewById(R.id.dfeels);
        if (s.inFarenheit()) {
            dtemp.setText(data.get("fTemp").toString() + " °F");
            b.setText("°F to °C");
            feelslike.setText("Feels like:" + "  " + data.get("fChill").toString() + " °F");
        }
        else{
            dtemp.setText(data.get("cTemp").toString() + " °C");
            b.setText("°C to °F");
            feelslike.setText("Feels like:" + "  " + data.get("cChill").toString() + " °C");

        }
        //Get textview components of detail screen
        TextView lastUpdated = (TextView) v.findViewById(R.id.lastUpdated);
        TextView bpressure = (TextView) v.findViewById(R.id.bpressure);
        TextView humidity = (TextView) v.findViewById(R.id.humidity);
        TextView precipday = (TextView) v.findViewById(R.id.preciptoday);
        TextView precipm = (TextView) v.findViewById(R.id.precipmonth);
        TextView precipy = (TextView) v.findViewById(R.id.precipyear);
        TextView wspeed = (TextView) v.findViewById(R.id.dspeed);

        //Set components of detail screen
        bpressure.setText("Pressure:" + "  " + data.get("barometricIn").toString() + " inches");
        humidity.setText("Humidity:   " + data.get("humidity") + "%");
        precipday.setText("Precip Today:   " + data.get("precipTodayInches") + " inches");
        precipm.setText("Monthly Precip:   " + data.get("precipMonthInches") + " inches");
        precipy.setText("Yearly Precip:   " + data.get("precipYearInches") + " inches");

        wspeed.setText("Wind: " + data.get("windMph") + " mph" + " " + data.get("windDir"));
        lastUpdated.setText("Last Updated: " + data.get("lastUpdated"));

        ProgressBar p = (ProgressBar) v.findViewById(R.id.progressBar);
        p.setVisibility(View.GONE);
        feelslike.setVisibility(View.VISIBLE);
        dtemp.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);
        bpressure.setVisibility(View.VISIBLE);
        humidity.setVisibility(View.VISIBLE);
        precipday.setVisibility(View.VISIBLE);
        precipm.setVisibility(View.VISIBLE);
        precipy.setVisibility(View.VISIBLE);
        wspeed.setVisibility(View.VISIBLE);
    }
}