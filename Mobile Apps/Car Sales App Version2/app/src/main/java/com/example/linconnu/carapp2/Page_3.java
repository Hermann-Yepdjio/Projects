package com.example.linconnu.carapp2;

/*
 *Member#1: Hermann Yepdjio
 * SID: 40917845
 * Member#2: Pushkin Feleke
 * SID: ??
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.linconnu.carapp2.R.layout.page_3;

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

            if (Page_1.al.size() == 0)
            {
                Toast.makeText(getActivity(), "Error Show Cars/ Empty", Toast.LENGTH_SHORT).show();
            }
            else
            {

                gridview.setAdapter(new ImageAdapter(getActivity().getBaseContext(), Page_1.al));
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                    {

                        String string = ((TextView) v.findViewById(R.id.textView4)).getText().toString();
                        Intent intent= new Intent(v.getContext(), profile.class);
                        int temp=0;
                        for (int i = 0; i < Page_1.al.size(); i++)
                        {
                            if (Page_1.al.get(i).id == string)
                                temp=i;

                        }

                        Toast.makeText(getActivity().getBaseContext(), Page_1.al.get(temp).name, Toast.LENGTH_SHORT).show();

                        intent.putExtra("instance", Page_1.al.get(temp));
                        v.getContext().startActivity(intent);

                    }
                });
            }
        }
    }


}

