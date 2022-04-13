package com.example.linconnu.loveapp;
/*
 *member#1: Hermann Yepdjio
 *SID: 40917845
 * member#2: Pushkin Feleke
 * SID: ??
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.example.linconnu.loveapp.R.layout.page_2;

/**
 * Created by linconnu on 07/11/17.
 */

public class Page_2 extends Fragment
{
    public dataBase db;
    EditText age_min, age_max, interests, sex_page2;
    ImageButton button_search;
    public Page_2(){};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View PageTwo = inflater.inflate(page_2, container, false);
        db  = new dataBase(PageTwo.getContext());
        age_min = (EditText) PageTwo.findViewById(R.id.age_min);
        age_max = (EditText) PageTwo.findViewById(R.id.age_max);
        sex_page2 = (EditText) PageTwo.findViewById(R.id.sex_page2);
        interests = (EditText) PageTwo.findViewById(R.id.interest_page2);
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

                            person person_instance = new person(show.getString(0), show.getString(1), show.getString(2), show.getString(3), show.getString(4));
                            String[] splitArray = person_instance.interests.split("\\s+");
                            String[] splitArray2;
                            boolean condition = false;
                            if(interests.getText().toString().equals(""))
                                condition = true;
                            else
                            {
                                splitArray2 = interests.getText().toString().split("\\s+");
                                for (String string : splitArray)
                                {
                                    for (String string1 : splitArray2)
                                        if (string.equals(string1))
                                        {
                                            condition = true;
                                            break;
                                        }
                                }
                            }
                            if (condition && (age_max.getText().toString().equals("") ||
                                    Double.parseDouble(person_instance.age) <= Double.parseDouble(age_max.getText().toString())) &&
                                        (age_min.getText().toString().equals("") ||
                                                Double.parseDouble(person_instance.age) >= Double.parseDouble(age_min.getText().toString()))&&
                                        (sex_page2.getText().toString().equals(person_instance.sex)||sex_page2.getText().toString().equals("")))
                                Page_1.al.add(person_instance);
                        }

                            Toast.makeText(getActivity(), "We have: " + Integer.toString(Page_1.al.size()) +
                                    " matches.", Toast.LENGTH_SHORT).show();
                            ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.MyPage);
                            viewPager.setCurrentItem(3);

                    }


                });
    }


}
