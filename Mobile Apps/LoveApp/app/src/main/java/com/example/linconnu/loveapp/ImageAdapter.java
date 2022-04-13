package com.example.linconnu.loveapp;
/*
 *member#1: Hermann Yepdjio
 *SID: 40917845
 * member#2: Pushkin Feleke
 * SID: ??
 */

import android.content.Context;
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
    private final ArrayList<person> persons;

    public ImageAdapter(Context context, ArrayList<person> persons)
    {
        this.context = context;
        this.persons = persons;
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
        ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView8);
        TextView textView = (TextView) gridView.findViewById(R.id.textView);
        TextView textView2 = (TextView) gridView.findViewById(R.id.textView2);
        TextView textView3 = (TextView) gridView.findViewById(R.id.textView3);
        TextView textView4 = (TextView) gridView.findViewById(R.id.textView4);
        if(persons.get(position).sex.equals("male") || persons.get(position).sex.equals("Male"))
            imageView.setImageResource(R.drawable.male);
        else
            imageView.setImageResource(R.drawable.female);
        textView.setText(persons.get(position).name);
        textView2.setText(persons.get(position).age);
        textView3.setText(persons.get(position).interests);
        textView4.setText(persons.get(position).id);

        return gridView;
    }
    @Override
    public int getCount()
    {
        return persons.size();
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
