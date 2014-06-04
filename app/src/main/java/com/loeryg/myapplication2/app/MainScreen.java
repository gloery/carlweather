package com.loeryg.myapplication2.app;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
//A basic and initial fragment that displays the temperature and wind chill
public class MainScreen extends Fragment {


    public View mainScreenView;

    public MainScreen() {
        // Required empty public constructor
    }

    public void swapButtonText(String degrees) {
        Button b = (Button) getView().findViewById(R.id.degreeswap);
        if (degrees == "F") {
            b.setText("°F to °C");
        } else {
            b.setText("°C to °F");
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public void refreshDisplay(){
        System.out.println("DISPLAY REFRESH MAIN");
        View v = getView();

        Activity mainActivity = (MainActivity)getActivity();
        TextView d = (TextView) v.findViewById(R.id.degrees);
        TextView tv = (TextView) v.findViewById(R.id.ftempview);
        TextView fl = (TextView) v.findViewById(R.id.feelslike);
        RSSParser r = ((MainActivity) mainActivity).getParser();
        r.switchDegrees();
        ((MainActivity) mainActivity).setParser(r);
        HashMap<String, Object> ourData = r.getData();
        if (r.inFarenheit()){
            Integer fTemp = Math.round((Float) ourData.get("fTemp"));
            Integer fChill = Math.round((Float) ourData.get("fChill"));
            String f = fTemp.toString();
            fl.setText("Feels like: " + " " + fChill.toString() + " °F");
            d.setText("°F");
            tv.setText(f);
            swapButtonText("F");

        }
        else{
            Integer cTemp = Math.round((Float) ourData.get("cTemp"));
            Integer cChill = Math.round((Float) ourData.get("cChill"));
            String c = cTemp.toString();
            fl.setText("Feels like: " + " " + cChill.toString() + " °C");
            d.setText("°C");
            tv.setText(c);
            swapButtonText("C");
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainScreenView = inflater.inflate(R.layout.fragment_main_screen, container, false);

        Activity a = (MainActivity)getActivity();
        RSSFetchTask r = new RSSFetchTask(a);
        r.execute(mainScreenView);
        Button refresh = (Button) mainScreenView.findViewById(R.id.refreshbutton);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Activity m = (MainActivity)getActivity();
                RSSFetchTask r = new RSSFetchTask(m);
                Toast.makeText(mainScreenView.getContext(), "Weather data refreshed!",
                Toast.LENGTH_SHORT).show();
                r.execute(mainScreenView);
            }
        });
        Button b =  (Button) mainScreenView.findViewById(R.id.degreeswap);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity b = (MainActivity)getActivity();
                RSSParser parser = ((MainActivity) b).getParser();
                if (parser.getLastUpdated() == null){
                    return;
                }
                refreshDisplay();
            }
        });
        return mainScreenView;
    }



}
