package com.example.cs427001_22.lab2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void Breakfast_Button(View view)
    {
        Intent intent = new Intent(this, DisplayBreakfastMenu.class);
        startActivity(intent);
    }
    public void Dinner_Button(View view)
    {
        Intent intent = new Intent(this, DisplayDinnerMenu.class);
        startActivity(intent);
    }
}
