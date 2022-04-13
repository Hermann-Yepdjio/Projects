package com.example.linconnu.carapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.linconnu.carapp.R.layout.page_3;

/**
 * Created by linconnu on 07/11/17.
 */

public class Page_3 extends Fragment
{
    GridView gridview;
    ArrayList<car> car_instances = new ArrayList<car>();

    public Page_3(){};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View PageThree = inflater.inflate(page_3, container, false);
        gridview = (GridView) PageThree.findViewById(R.id.gridView);
        return PageThree;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
        {
            // called here
           /* car_instances.add(new car("123", "carina","2010" , "12500"));
            car_instances.add(new car("124", "carina","2014" , "17500"));
            car_instances.add(new car("125", "carina","2017" , "11500"));
            StringBuffer buffer = new StringBuffer();*/
            if (Page_1.al.size() == 0)
            {
                //showMessage("Error", "Nothing Found");
                Toast.makeText(getActivity(), "Error Show Cars/ Empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                /*for (car car_instance : Page_2.al)
                {
                    buffer.append("name: " + car_instance.name + "\n");
                    buffer.append("genre: " + car_instance.price + "\n");
                    buffer.append("price: $" + car_instance.year + "\n\n");
                }
                showMessage("Data", buffer.toString());*/
                gridview.setAdapter(new ImageAdapter(getActivity().getBaseContext(), Page_1.al));
            }
        }
    }

    /*public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new  AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }*/
}

