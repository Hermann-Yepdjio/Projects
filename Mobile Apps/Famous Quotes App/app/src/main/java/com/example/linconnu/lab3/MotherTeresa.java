package com.example.linconnu.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MotherTeresa extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_teresa);
    }
    public void home_button(View view)
    {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void previous_button(View view)
    {
        Intent intent = new Intent (this, women_page.class);
        startActivity(intent);
    }
}
