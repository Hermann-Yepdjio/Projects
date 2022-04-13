package com.example.linconnu.sqlitekino;

/*
 *Name: Hermann Yepdjio
 *SID: 40917845
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class movies extends AppCompatActivity
{
    GridView gridView;
    ArrayList<movie_instance> al;
    //final static String[] movie_titles = new String[4];
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Intent intent = getIntent();
        al = (ArrayList<movie_instance>)intent.getSerializableExtra("al");
        movie_instance[] movie_titles = new movie_instance[al.size()];
        for(int i =0; i<al.size(); i++)
                movie_titles[i] = al.get(i);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this, movie_titles));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)
                {

                        String string = ((TextView) v.findViewById(R.id.textView4)).getText().toString();
                        Intent intent_2 = new Intent(v.getContext(), movie.class);
                        int temp=0;
                        for (int i = 0; i < al.size(); i++)
                        {
                            if (al.get(i).id == string)
                                temp=i;

                        }

                    Toast.makeText(getApplicationContext(), al.get(temp).name, Toast.LENGTH_SHORT).show();

                        intent_2.putExtra("instance", al.get(temp));
                        v.getContext().startActivity(intent_2);

                }
        });
    }
    public  void previous_button(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
