package com.example.linconnu.lab2;

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
    public void DisplayBreakfastMenu ( View view)
    {
        Intent intent = new Intent(this, BreakfastMenu.class);
        startActivity(intent);
    }
    public void DisplayDinnerMenu ( View view)
    {
        Intent intent = new Intent(this, DinnerMenu.class);
        startActivity(intent);
    }
}

