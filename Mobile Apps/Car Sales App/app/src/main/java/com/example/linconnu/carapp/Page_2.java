package com.example.linconnu.carapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.linconnu.carapp.R.layout.page_2;

/**
 * Created by linconnu on 07/11/17.
 */

public class Page_2 extends Fragment
{
    public dataBase db;
    EditText name, minimum, maximum;
    ImageButton button_search;
    public Page_2(){};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View PageTwo = inflater.inflate(page_2, container, false);
        db  = new dataBase(PageTwo.getContext());
        name = (EditText) PageTwo.findViewById(R.id.car2);
        minimum = (EditText) PageTwo.findViewById(R.id.minimum);
        maximum = (EditText) PageTwo.findViewById(R.id.maximum);
        button_search = (ImageButton) PageTwo.findViewById(R.id.button_search);

        searchData();

        return PageTwo;

    }
    public void searchData()
    {
        button_search.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Page_1.al.clear();
                        Cursor show = db.showCars();
                        while (show.moveToNext())
                        {
                            car car_instance = new car(show.getString(0), show.getString(1), show.getString(2), show.getString(3));
                            if ((name.getText().toString().equals("") || car_instance.name.equals(name.getText().toString())) &&
                                    (maximum.getText().toString().equals("") ||
                                            Double.parseDouble(car_instance.price) <= Double.parseDouble(maximum.getText().toString())) &&
                                    (minimum.getText().toString().equals("") ||
                                            Double.parseDouble(car_instance.price) >= Double.parseDouble(minimum.getText().toString())))
                                Page_1.al.add(car_instance);
                        }

                            Toast.makeText(getActivity(), "We have: " + Integer.toString(Page_1.al.size()) +
                                    " matches.", Toast.LENGTH_SHORT).show();
                            ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.MyPage);
                            viewPager.setCurrentItem(3);

                    }


                });
    }

}
