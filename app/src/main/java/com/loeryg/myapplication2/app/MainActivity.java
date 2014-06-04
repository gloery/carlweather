package com.loeryg.myapplication2.app;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

//Manages fragments and stores their shared data objects
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public RSSParser parser;
    public HTMLParser hparser;
    public int clicked;
    public ActionBar toolBar;
    public ViewPager viewPager;
    public GraphData graphData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clicked = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instantiate toolbar
        toolBar = getActionBar();
        toolBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //instantiate data objects
        this.parser = new RSSParser();
        ImageView imView = new ImageView(this.getApplicationContext());
        this.hparser = new HTMLParser();
        this.graphData = new GraphData();
        viewPager = (ViewPager) findViewById(R.id.pager);
        //viewpager stores pointers to data objects
        PageAdapter p = new PageAdapter(getSupportFragmentManager(), this.parser, this.hparser, this.graphData);
        viewPager.setAdapter(p);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){



            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {                }

            @Override
            public void onPageSelected(int position) {
                toolBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {               }
        });
//configure toolbar tabs
        ActionBar.Tab mainTab = toolBar.newTab();
        mainTab.setText("home");
        mainTab.setTabListener(this);

        ActionBar.Tab detailTab = toolBar.newTab();
        detailTab.setText("details");
        detailTab.setTabListener(this);
        ActionBar.Tab moonTab = toolBar.newTab();
        moonTab.setText("sun");
        moonTab.setTabListener(this);

        ActionBar.Tab graphTab = toolBar.newTab();
        graphTab.setText("graph");
        graphTab.setTabListener(this);

        toolBar.addTab(mainTab);
        toolBar.addTab(moonTab);
        toolBar.addTab(detailTab);
        toolBar.addTab(graphTab);
    }




    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
    //Getters and setters for data objects called by the AsyncTasks
    public void setParser(RSSParser parser){
        this.parser = parser;
    }
    public void setHTMLParser(HTMLParser p) {this.hparser = p;}
    public void setGraphData(GraphData g){this.graphData = g;}
    public RSSParser getParser(){
        return this.parser;
    }
    public HTMLParser getHTMLParser() {return this.hparser;}
    public GraphData getGraphData(){return this.graphData;}



}
