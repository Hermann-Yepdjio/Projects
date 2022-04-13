package com.example.linconnu.lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void women_button(View view)
    {
        Intent intent =  new Intent(this, women_page.class);
        startActivity(intent);
    }

    public void men_button(View view)
    {
        Intent intent = new Intent (this, men_page.class);
        startActivity(intent);
    }
}
