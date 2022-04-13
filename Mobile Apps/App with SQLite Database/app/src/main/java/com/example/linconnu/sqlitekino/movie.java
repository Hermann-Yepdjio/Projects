package com.example.linconnu.sqlitekino;
/*
 *Name: Hermann Yepdjio
 *SID: 40917845
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class movie extends AppCompatActivity
{
    GridView gridview;
    ImageButton imageButton;
    //final static String[] movie_titles = new String[4];
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();
        movie_instance instance = (movie_instance) intent.getSerializableExtra("instance");
        gridview = (GridView) findViewById(R.id.gridView1);
        gridview.setAdapter(new ImagaAdapter2(this, instance));

        imageButton = (ImageButton) findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public  void onClick(View view)
                    {
                        finish();
                    }
                }
        );


    }

    public  void home_button(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
