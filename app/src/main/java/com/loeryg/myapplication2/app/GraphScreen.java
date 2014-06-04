package com.loeryg.myapplication2.app;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */

//A fragment that stores and displays graph objects
public class GraphScreen extends Fragment {


    public GraphScreen() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_graph_screen, container, false);
        Activity a = getActivity();
        HTMLFetchTask h = new HTMLFetchTask(a);
        h.execute(view);
        return view;
    }


}
