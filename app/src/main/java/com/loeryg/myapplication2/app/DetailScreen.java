package com.loeryg.myapplication2.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Gordon on 5/10/14.
 */

//Fragment for detail subscreen.
public class DetailScreen extends Fragment {
    

    public View detailScreenView;


    public DetailScreen() {

    }

    public void swapButtonText(String degrees){
        Button b =  (Button) getView().findViewById(R.id.degreeswap2);
        if (degrees == "F"){
            b.setText("°F to °C");
        }
        else{
            b.setText("°C to °F");
        }
    }

    public void refreshDisplay(){
        Activity mainActivity = (MainActivity)getActivity();
        View v = getView();
        RSSParser s = ((MainActivity) mainActivity).getParser();
        s.switchDegrees();
        ((MainActivity) mainActivity).setParser(s);
        HashMap<String, Object> data = s.getData();
        Button b = (Button) v.findViewById(R.id.degreeswap2);
        TextView dtemp = (TextView) v.findViewById(R.id.detailtemp);
        TextView feelslike = (TextView) v.findViewById(R.id.dfeels);
        if (s.inFarenheit()){
            dtemp.setText(data.get("fTemp").toString() + " °F");
            b.setText("°F to °C");
            feelslike.setText("Feels like:" + "  " + data.get("fChill").toString() + " °F");
            swapButtonText("F");
        }
        else{
            dtemp.setText(data.get("cTemp").toString() + " °C");
            b.setText("°C to °F");
            feelslike.setText("Feels like:" + "  " + data.get("cChill").toString() + " °C");
            swapButtonText("C");

        }
    }

    //Instantiates detail fetch task, which sets up the view after asynchronously updating
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailScreenView = inflater.inflate(R.layout.detail_screen, container, false);
        Activity a = getActivity();
        DetailScreenFetchTask d = new DetailScreenFetchTask(a);
        d.execute(detailScreenView);
        Button refresh = (Button) detailScreenView.findViewById(R.id.detailrefreshbutton);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Activity m = (MainActivity)getActivity();
                DetailScreenFetchTask t = new DetailScreenFetchTask(m);
                Toast.makeText(detailScreenView.getContext(), "Weather data refreshed!",
                Toast.LENGTH_SHORT).show();
                t.execute(detailScreenView);
            }
        });
        Button b =  (Button) detailScreenView.findViewById(R.id.degreeswap2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity mainActivity = (MainActivity)getActivity();
                RSSParser r = ((MainActivity) mainActivity).getParser();
                if (r.getLastUpdated() == null){
                    return;
                }
                refreshDisplay();
            }
        });
        return detailScreenView;

    }


}
