package com.example.linconnu.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class men_page extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men_page);
    }

    public void home_button(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void previous_button(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void nikola_tesla_button(View view)
    {
        Intent intent =  new Intent(this, NikolaTesla.class);
        startActivity(intent);
    }

    public void mlk_button(View view)
    {
        Intent intent = new Intent (this, MLK.class);
        startActivity(intent);
    }
}
