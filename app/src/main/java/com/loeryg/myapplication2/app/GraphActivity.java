package com.loeryg.myapplication2.app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.Serializable;


public class GraphActivity extends Activity{
    Drawable graph;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Button b =  (Button) findViewById(R.id.close);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                finish();
            }
        });
        Bitmap bitmap = (Bitmap) this.getIntent().getParcelableExtra("Image");
        Drawable graph = new BitmapDrawable(getResources(),bitmap);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ImageView bigGraph = (ImageView) findViewById(R.id.biggraph);
        bigGraph.setImageDrawable(graph);
    }





}
