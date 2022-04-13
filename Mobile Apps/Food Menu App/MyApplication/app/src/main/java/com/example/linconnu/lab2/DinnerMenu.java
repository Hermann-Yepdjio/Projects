package com.example.linconnu.lab2;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class DinnerMenu extends AppCompatActivity
{
    GridView gridView;
    static final String[] Dinner_Items = new String []{"Salmon", "Filet", "Pasta", "Vegan" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner_menu);

        gridView = (GridView)findViewById(R.id.gridView1);
        gridView.setAdapter(new ImageAdapter2(this, Dinner_Items));
        gridView.setOnItemClickListener (new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Toast.makeText(getApplicationContext(), ((TextView) v.findViewById(R.id.textView)).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
