package com.loeryg.myapplication2.app;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Erin on 5/22/2014.
 */

//Fetches the moon cycle and sunrise/sunset information for the Moon Screen
public class MoonCycleFetchTask extends AsyncTask<View ,Void, Pair<View, ArrayList<String>>>  {

    public Activity mainActivity;

    public MoonCycleFetchTask(Activity activity){
        this.mainActivity = activity;
    }


    @Override
    //Retrieves Moon cycle data by scraping the Carleton weather homepage
    protected Pair<View, ArrayList<String>> doInBackground(View... params) {
        Document d;
        ArrayList<String> sunmoon = new ArrayList<String>();
        HTMLParser h = ((MainActivity)this.mainActivity).getHTMLParser();
        //If there is current data in the main activity, avoid calling for fresh data and return blank arrays
        Date curDate = Calendar.getInstance().getTime();
        long now = curDate.getTime();
        Date storeDate = h.getLastUpdated();
        if (storeDate != null){
            long then = h.getLastUpdated().getTime();
            if (now < then + 600000) {
                return Pair.create(params[0], sunmoon);
            }
            else{
            }
        }
        //Otherwise, fetch fresh data from the internet
        try {
            d = Jsoup.connect("http://weather.carleton.edu").get();
            for (Element td : d.select("td")) {
                String matchPattern = "^[A-Z][a-z][a-z0]\\. [0-9][0-9]?th$";
                Pattern p = Pattern.compile(matchPattern);
                Matcher m = p.matcher(td.text());
                while (m.find()) {
                    sunmoon.add(m.group());
                }
                String mp2 = "^(Dawn|Dusk|Sunrise|Sunset): ([0-9][0-9]?:[0-9][0-9])( am| pm)";
                p = Pattern.compile(mp2);
                m = p.matcher(td.text());
                while(m.find()){
                    sunmoon.add(m.group(2) + m.group(3));
                }
            }
        }
        catch(IOException e){
            ArrayList<String> errorList = new ArrayList<String>();
            errorList.add("ERROR");
            e.printStackTrace();
            return Pair.create(params[0], errorList);
        }
        return Pair.create(params[0], sunmoon);

    }

    @Override
    protected void onPostExecute(Pair<View, ArrayList<String>> result) {
        View v = result.first;
        ArrayList<String> sunmoon = result.second;
        HTMLParser s = ((MainActivity)this.mainActivity).getHTMLParser();
        //If a blank array is returned, there is current data in the MainActivity
        if (sunmoon.isEmpty() == false){
            if (sunmoon.get(0) == "ERROR"){
                return;
            }
            else {
                s.updateData(sunmoon);
                ((MainActivity) this.mainActivity).setHTMLParser(s);
            }
        }
        HashMap<String, Object> data = s.getData();
        //Get text views
        TextView dawn = (TextView) v.findViewById(R.id.dawn);
        TextView dusk = (TextView) v.findViewById(R.id.dusk);
        TextView sunrise = (TextView) v.findViewById(R.id.sunrise);
        TextView sunset = (TextView) v.findViewById(R.id.sunset);
        TextView waxm = (TextView) v.findViewById(R.id.waxingMoonDate);
        TextView wanm = (TextView) v.findViewById(R.id.waningMoonDate);
        TextView fullm = (TextView) v.findViewById(R.id.fullMoonDate);
        TextView newm = (TextView) v.findViewById(R.id.newMoonDate);
        //Set text views on screen
        waxm.setText((String) data.get("Waxing"));
        wanm.setText((String) data.get("Waning"));
        fullm.setText((String) data.get("Full"));
        newm.setText((String) data.get("New"));
        Resources r = v.getContext().getResources();
        String dawnLabel = r.getString(R.string.dawn);
        String duskLabel = r.getString(R.string.dusk);
        String sunsetLabel = r.getString(R.string.sunset);
        String sunriseLabel = r.getString(R.string.sunrise);
        dawn.setText (dawnLabel+"  "+(String) data.get("Dawn"));
        dusk.setText(duskLabel + "  " + (String) data.get("Dusk"));
        sunrise.setText(sunriseLabel + "  " + (String) data.get("Sunrise"));
        sunset.setText(sunsetLabel + "  "  + (String) data.get("Sunset"));



    }

}
