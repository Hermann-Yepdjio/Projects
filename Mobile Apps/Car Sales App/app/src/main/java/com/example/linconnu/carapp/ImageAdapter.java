package com.example.linconnu.carapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by linconnu on 07/11/17.
 */

public class ImageAdapter extends BaseAdapter
{
    private Context context;
    private final ArrayList<car> cars;

    public ImageAdapter(Context context, ArrayList<car> cars)
    {
        this.context = context;
        this.cars = cars;
    }

    public View getView(int position, View  convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if(convertView == null)
        {
            gridView = new View(context);

            //get layout from page_3_grid.xml
            gridView= inflater.inflate(R.layout.page_3_grid, parent, false);


        }
        else
            gridView = (View) convertView;
        //set image based on selected text
        TextView textView = (TextView) gridView.findViewById(R.id.textView);
        TextView textView2 = (TextView) gridView.findViewById(R.id.textView2);
        TextView textView3 = (TextView) gridView.findViewById(R.id.textView3);
        if(position%2 == 0)
        {
            textView.setTextColor(Color.WHITE);
            textView2.setTextColor(Color.WHITE);
            textView3.setTextColor(Color.WHITE);
        }
        else
        {
            textView.setTextColor(Color.YELLOW);
            textView2.setTextColor(Color.YELLOW);
            textView3.setTextColor(Color.YELLOW);
        }
        textView.setText("Vehicule: " + cars.get(position).name);
        textView2.setText("Year: " + cars.get(position).year);
        textView3.setText("Price: $" + cars.get(position).price);

        return gridView;
    }
    @Override
    public int getCount()
    {
        return cars.size();
    }
    @Override
    public Object getItem(int position)
    {
        return null;
    }
    @Override
    public long getItemId(int position)
    {
        return 0;
    }
}
