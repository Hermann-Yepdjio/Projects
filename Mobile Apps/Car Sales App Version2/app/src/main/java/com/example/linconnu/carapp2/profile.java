package com.example.linconnu.carapp2;
/*
 *Member#1: Hermann Yepdjio
 * SID: 40917845
 * Member#2: Pushkin Feleke
 * SID: ??
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class profile extends AppCompatActivity
{
    TextView name, year, price;
    EditText message;
    ImageView profile_picture, picture_1, picture_2, picture_3, picture_4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView) findViewById(R.id.profile_name);
        year = (TextView) findViewById(R.id.profile_year);
        price = (TextView) findViewById(R.id.profile_price);

        profile_picture = (ImageView) findViewById(R.id.picture);
        picture_1 = (ImageView) findViewById(R.id.picture_1);
        picture_2 = (ImageView) findViewById(R.id.picture_2);
        picture_3 = (ImageView) findViewById(R.id.picture_3);
        picture_4 = (ImageView) findViewById(R.id.picture_4);

        Intent intent = getIntent();
        car p = (car) intent.getSerializableExtra("instance");
        if(p.name.equals("nissan") || p.name.equals("Nissan"))
        {
            profile_picture.setImageResource(R.drawable.nissan_1);
            picture_1.setImageResource(R.drawable.nissan_2);
            picture_2.setImageResource(R.drawable.nissan_3);
            picture_3.setImageResource(R.drawable.nissan_4);
            picture_4.setImageResource(R.drawable.nissan_5);
        }
        else if(p.name.equals("jeep") || p.name.equals("Jeep"))
        {
            profile_picture.setImageResource(R.drawable.jeep_1);
            picture_1.setImageResource(R.drawable.jeep_2);
            picture_2.setImageResource(R.drawable.jeep_3);
            picture_3.setImageResource(R.drawable.jeep_4);
            picture_4.setImageResource(R.drawable.jeep_5);
        }
        else
        {
            profile_picture.setImageResource(R.drawable.generic);
            picture_1.setImageResource(R.drawable.generic);
            picture_2.setImageResource(R.drawable.generic);
            picture_3.setImageResource(R.drawable.generic);
            picture_4.setImageResource(R.drawable.generic);
        }
        name.setText(p.name);
        price.setText("$" + p.price);
        year.setText(p.year);
    }
}
