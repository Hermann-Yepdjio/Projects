package com.example.linconnu.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class women_page extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women_page);
    }
    public void home_button(View view)
    {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void previous_button(View view)
    {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
    public void mother_teresa_button(View view)
    {
        Intent intent =  new Intent(this, MotherTeresa.class);
        startActivity(intent);
    }

    public void princess_diana_button(View view)
    {
        Intent intent = new Intent (this, PrincessDiana.class);
        startActivity(intent);
    }
}
