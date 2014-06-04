package com.loeryg.myapplication2.app;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */

//A fragment that displays the moon cycles and sunrise/sunset times
public class MoonScreen extends Fragment {

    private HTMLParser h;


    public MoonScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moon_screen, container, false);
        Activity a = getActivity();
        MoonCycleFetchTask r = new MoonCycleFetchTask(a);
        r.execute(view);
        return view;
    }


}
