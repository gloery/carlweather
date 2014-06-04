package com.loeryg.myapplication2.app;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by Gordon on 5/8/14.
 */

//A very basic adapter class that constructs the fragments in response to tab presses and passes in appropriate data objects
public class PageAdapter extends FragmentPagerAdapter{
    private RSSParser rssParser;
    private HTMLParser htmlParser;
    private GraphData graphData;
    private FragmentManager fm;

    public PageAdapter(FragmentManager supportFragmentManager, RSSParser r, HTMLParser h, GraphData g){
        super(supportFragmentManager);
        this.fm = supportFragmentManager;
        this.rssParser = r;
        this.htmlParser = h;
        this.graphData = g;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (position == 0){
            fragment = new MainScreen();
        }

        if (position == 1){
            fragment = new MoonScreen();
        }
        if (position == 2){
            fragment = new DetailScreen();
        }
        if (position == 3){
            fragment = new GraphScreen();
        }


        return fragment;
    }



    @Override
    public int getCount() {
        return 4;
    }
}
