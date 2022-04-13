package com.example.linconnu.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NikolaTesla extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nikola_tesla);
    }

    public void home_button(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void previous_button(View view)
    {
        Intent intent = new Intent(this, men_page.class);
        startActivity(intent);
    }
}
