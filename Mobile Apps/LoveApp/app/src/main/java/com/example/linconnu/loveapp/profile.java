package com.example.linconnu.loveapp;
/*
 *member#1: Hermann Yepdjio
 *SID: 40917845
 * member#2: Pushkin Feleke
 * SID: ??
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class profile extends AppCompatActivity
{
    TextView name, age, interests, tip;
    EditText message;
    ImageView profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView) findViewById(R.id.name_profile);
        age = (TextView) findViewById(R.id.age_profile);
        interests = (TextView) findViewById(R.id.interest_profile);
        tip = (TextView) findViewById(R.id.tip_profile);
        profile_picture = (ImageView) findViewById(R.id.picture_profile);
        Intent intent = getIntent();
        person p = (person) intent.getSerializableExtra("instance");
        if(p.sex.equals("male") || p.sex.equals("Male"))
            profile_picture.setImageResource(R.drawable.male);
        else
            profile_picture.setImageResource(R.drawable.female);
        name.setText(p.name);
        age.setText(p.age);
        interests.setText(p.interests);
        tip.setText("Say hello to " + p.name + ":");


    }
}
