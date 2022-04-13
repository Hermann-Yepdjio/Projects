package com.example.linconnu.lab2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public  class ImageAdapter extends BaseAdapter
{
    private Context context;
    private final String[] breakfastValues;
    public ImageAdapter(Context context, String[] breakfastValues)
    {
        this.context=context;
        this.breakfastValues=breakfastValues;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null)
        {
            gridView = new View(context);

            //get layout from breakfast.xml
            gridView = inflater.inflate (R.layout.breakfast, null);

            //set value into textview
            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
            textView.setText(breakfastValues[position]);

            //set image based on selected text
            ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

            String breakfast = breakfastValues[position];

            if (breakfast.equals("Toast"))
            {
                imageView.setImageResource(R.drawable.toast);
            }
            else if (breakfast.equals("Juice"))
            {
                imageView.setImageResource(R.drawable.juice);
            }
            else if (breakfast.equals("Pancake"))
            {
                imageView.setImageResource(R.drawable.panckake);
            }
            else if (breakfast.equals("Burrito"))
            {
                imageView.setImageResource(R.drawable.burrito);
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
        return breakfastValues.length;
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
