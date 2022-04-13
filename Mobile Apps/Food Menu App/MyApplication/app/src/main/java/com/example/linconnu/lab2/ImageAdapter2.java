package com.example.linconnu.lab2;

/**
 * Created by linconnu on 11/10/17.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ImageAdapter2 extends BaseAdapter
{
    private Context context;
    private final String[] dinnerValues;
    public ImageAdapter2(Context context, String[] dinnerValues)
    {
        this.context=context;
        this.dinnerValues=dinnerValues;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null)
        {
            gridView = new View(context);

            //get layout from breakfast.xml
            gridView = inflater.inflate (R.layout.dinner, null);

            //set value into textview
            TextView textView = (TextView) gridView.findViewById(R.id.textView);
            textView.setText(dinnerValues[position]);

            //set image based on selected text
            ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView);

            String breakfast = dinnerValues[position];

            if (breakfast.equals("Salmon"))
            {
                imageView.setImageResource(R.drawable.salmon);
            }
            else if (breakfast.equals("Filet"))
            {
                imageView.setImageResource(R.drawable.filet);
            }
            else if (breakfast.equals("Vegan"))
            {
                imageView.setImageResource(R.drawable.vegan);
            }
            else if (breakfast.equals("Pasta"))
            {
                imageView.setImageResource(R.drawable.noodle);
            }
        }
        else
        {
            gridView = (View) convertView;
        }
        return gridView;

    }

    @Override
    public int getCount()
    {
        return dinnerValues.length;
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