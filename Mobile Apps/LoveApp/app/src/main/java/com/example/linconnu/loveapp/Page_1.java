package com.example.linconnu.loveapp;
/*
 *member#1: Hermann Yepdjio
 *SID: 40917845
 * member#2: Pushkin Feleke
 * SID: ??
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.linconnu.loveapp.R.layout.page_1;

/**
 * Created by linconnu on 07/11/17.
 */

public class Page_1 extends Fragment
{
    public dataBase db;
    EditText name, age, sex, interests;
    public static ArrayList<person> al = new ArrayList<person>();
    ImageButton button_add;
    public Page_1(){};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View PageOne = inflater.inflate(page_1, container, false);
        db = new dataBase(getActivity().getBaseContext());

        name = (EditText) PageOne.findViewById(R.id.name);
        age = (EditText) PageOne.findViewById(R.id.age);
        sex = (EditText) PageOne.findViewById(R.id.sex_page1);
        interests = (EditText) PageOne.findViewById(R.id.interest_page1);
        button_add = (ImageButton) PageOne.findViewById(R.id.button_add);

        addData();

        return PageOne;
    }

    public void addData()
    {
        button_add.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        boolean addData_works = db.addData(name.getText().toString(), age.getText().toString(), sex.getText().toString(), interests.getText().toString());
                        if(addData_works)
                            Toast.makeText(getActivity().getBaseContext(), "Person Added", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity().getBaseContext(), "Person Not Added", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
