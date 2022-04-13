package com.example.ppushkinp.geoquizzzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button_true(View view)
    {

        Intent intent = new Intent(this, DisplayWrongMessage.class);
        startActivity(intent);

    }
    public void button_false(View view)
    {

        Intent intent = new Intent(this, DisplayRightMessage.class);

        startActivity(intent);

    }

}
