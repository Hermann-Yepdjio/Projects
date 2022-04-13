package com.example.linconnu.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class BreakfastMenu extends AppCompatActivity
{
    GridView gridView;
    static final String[] Breakfast_Items = new String []{"Toast", "Juice", "Pancake", "Burrito" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_menu);

        gridView = (GridView)findViewById(R.id.gridView1);
        gridView.setAdapter(new ImageAdapter(this, Breakfast_Items));
        gridView.setOnItemClickListener (new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                        Toast.makeText(getApplicationContext(), ((TextView) v.findViewById(R.id.grid_item_label)).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
