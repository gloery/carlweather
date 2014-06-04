package com.loeryg.myapplication2.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Erin on 4/29/14.
 */
//Scrapes HTML for Graphs and stores them in a graph data object
public class HTMLFetchTask extends  AsyncTask<View ,Void, Pair<View, ArrayList<Bitmap>>> {
    public Activity mainActivity;
    public HTMLFetchTask(Activity activity){
        this.mainActivity = activity;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }
    @Override
    protected Pair<View, ArrayList<Bitmap>> doInBackground(View... params) {
        Document d;
        ArrayList<String> graphLinks = new ArrayList<String>();
        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        GraphData h = ((MainActivity)this.mainActivity).getGraphData();
        //If there exists current graph data in the MainActivity, return a blank array and skip downloading
        Date curDate = Calendar.getInstance().getTime();
        long now = curDate.getTime();
        Date storeDate = h.getLastUpdated();
        if (storeDate != null){
            long then = h.getLastUpdated().getTime();
            if (now < then + 600000) {
                return Pair.create(params[0], bitmaps);
            }
            else{
            }
        }
        //Otherwise, download the bitmaps and store them in an object
        //Step 1: scrape graph urls, they're dynamic
        try{
            d = Jsoup.connect("http://weather.carleton.edu").get();
            Elements links = d.select("a[href]");
            for (Element l : links){
                String link = l.attr("abs:href");
//                System.out.println("LINK: "+link);
                String pattern1 = "rawplot.php";
                String pattern2 = "dailyplot.php";
                if (link.contains(pattern1)|| link.contains(pattern2)){
                    graphLinks.add(link);

                }
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }
        //Step 2: scrape graphs
        for (String i : graphLinks){
            try {
                Document e = Jsoup.connect(i).get();
                Elements graph = e.select("[src]");

                for (Element elm : graph){

                    if (elm.attr("abs:src").contains("rplot") || elm.attr("abs:src").contains("dplot")){
                        try {
                            InputStream in = null;
                            int response = -1;

                            URL url = new URL(elm.attr("abs:src"));
                            URLConnection conn = url.openConnection();

                            HttpURLConnection httpConn = (HttpURLConnection) conn;
                            httpConn.setAllowUserInteraction(false);
                            httpConn.setInstanceFollowRedirects(true);
                            httpConn.setRequestMethod("GET");
                            httpConn.connect();
                            response = httpConn.getResponseCode();
                            if (response == HttpURLConnection.HTTP_OK) {
                                in = httpConn.getInputStream();
                            }
                            Bitmap image = BitmapFactory.decodeStream(in);
                            in.close();
                            bitmaps.add(image);
                        } catch (Exception ex) {
                            throw new IOException("Error connecting");
                        }

                        //params[0] is our HTMLParser object
                    }
                }


            } catch (IOException e) {
                ArrayList<Bitmap> errorList = new ArrayList<Bitmap>();
                Bitmap errorBitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.RGB_565);
                e.printStackTrace();
                return Pair.create(params[0], errorList);
            }
        }
        return Pair.create(params[0], bitmaps);
    }
    @Override
    protected void onPostExecute(Pair<View, ArrayList<Bitmap>> result) {
        View v = result.first;
        ArrayList<Bitmap> bitmaps = result.second;
        GraphData g = ((MainActivity)this.mainActivity).getGraphData();
        //If a blank array is returned, use data from MainActivity
        if (bitmaps.isEmpty()){
            bitmaps = g.getBitmaps();
        }
        //Otherwise, store fresh data in MainActivity
        else {
            if (bitmaps.get(0).getConfig() == Bitmap.Config.RGB_565){
                return;
            }
            else {
                g.setBitmap(bitmaps);
                ((MainActivity) this.mainActivity).setGraphData(g);
            }
        }


        //Get imageviews
        ImageView iv = (ImageView) v.findViewById(R.id.graph1);
        ImageView iv2 = (ImageView) v.findViewById(R.id.graph2);
        ImageView iv3 = (ImageView) v.findViewById(R.id.graph3);
        ImageView iv4 = (ImageView) v.findViewById(R.id.graph4);

        //Convert bitmaps to drawables and set them
        Drawable tempgraph = new BitmapDrawable(v.getResources(),bitmaps.get(0));
        Drawable bgraph = new BitmapDrawable(v.getResources(),bitmaps.get(1));
        Drawable temp7graph = new BitmapDrawable(v.getResources(),bitmaps.get(2));
        Drawable temphlgraph = new BitmapDrawable(v.getResources(),bitmaps.get(3));
        final Bitmap b1 = bitmaps.get(0);
        final Bitmap b2 = bitmaps.get(1);
        final Bitmap b3 = bitmaps.get(2);
        final Bitmap b4 = bitmaps.get(3);
        iv.setImageDrawable(tempgraph);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, GraphActivity.class);
                intent.putExtra("Image", b1);
                mainActivity.startActivity(intent);

            }
        });
        iv2.setImageDrawable(bgraph);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, GraphActivity.class);
                intent.putExtra("Image", b2);
                mainActivity.startActivity(intent);

            }
        });
        iv3.setImageDrawable(temp7graph);
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, GraphActivity.class);
                intent.putExtra("Image", b3);
                mainActivity.startActivity(intent);

            }
        });
        iv4.setImageDrawable(temphlgraph);
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, GraphActivity.class);
                intent.putExtra("Image", b4);
                mainActivity.startActivity(intent);

            }
        });
        ProgressBar p = (ProgressBar) v.findViewById(R.id.progressBar);
        p.setVisibility(View.GONE);
        iv.setVisibility(View.VISIBLE);
        iv2.setVisibility(View.VISIBLE);
        iv3.setVisibility(View.VISIBLE);
        iv4.setVisibility(View.VISIBLE);
        ImageView border1 = (ImageView) v.findViewById(R.id.graph1border);
        ImageView border2 = (ImageView) v.findViewById(R.id.graph2border);
        ImageView border3 = (ImageView) v.findViewById(R.id.graph3border);
        ImageView border4 = (ImageView) v.findViewById(R.id.graph4border);
        border1.setVisibility(View.VISIBLE);
        border2.setVisibility(View.VISIBLE);
        border3.setVisibility(View.VISIBLE);
        border4.setVisibility(View.VISIBLE);
        TextView temp1 = (TextView) v.findViewById(R.id.temp24hrs);
        TextView bar24 = (TextView) v.findViewById(R.id.bar24hrs);
        TextView temp7 = (TextView) v.findViewById(R.id.temp7days);
        TextView hl130 = (TextView) v.findViewById(R.id.hl30days);
        temp1.setVisibility(View.VISIBLE);
        bar24.setVisibility(View.VISIBLE);
        temp7.setVisibility(View.VISIBLE);
        hl130.setVisibility(View.VISIBLE);






    }

}
