package com.example.linconnu.sqlitekino;

/*
 *Name: Hermann Yepdjio
 *SID: 40917845
 */

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity
{
    dataBase db;
    EditText   name, genre, price;
    ImageButton button_add;
    ImageButton button_show;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new dataBase(this);

        name = (EditText) findViewById(R.id.plainText_name);
        genre = (EditText) findViewById(R.id.plainText_genre);
        price = (EditText) findViewById(R.id.double_price);
        button_add = (ImageButton) findViewById(R.id.button_add);
        button_show = (ImageButton) findViewById(R.id.button_show);

        addData();
        viewMovies();

    }

    public void addData()
    {
        button_add.setOnClickListener(
          new View.OnClickListener()
          {
            @Override
            public void onClick(View view)
            {
                boolean addData_works = db.addData(name.getText().toString(), genre.getText().toString(), price.getText().toString());
                if(addData_works)
                    Toast.makeText(MainActivity.this, "Movie Added", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Movie Not Added", Toast.LENGTH_SHORT).show();
            }
          });
    }

    public void viewMovies()
    {
        button_show.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Cursor show = db.showMovies();
                        if (show.getCount()==0)
                        {
                            showMessage("Error", "Nothing Found");
                            Toast.makeText(MainActivity.this, "Error Show Movies/ Empty", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            Toast.makeText(MainActivity.this, "number of records in the database: " + Integer.toString(show.getCount()), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(view.getContext(), movies.class);
                            ArrayList<movie_instance> al = new ArrayList<movie_instance>();
                            while (show.moveToNext())
                            {
                                movie_instance movie = new movie_instance(show.getString(0), show.getString(1), show.getString(2), show.getString(3));
                                al.add(movie);
                            }
                            intent.putExtra("al", al);
                            startActivity(intent);

                           /* StringBuffer buffer = new StringBuffer();
                            while (show.moveToNext())
                            {
                                buffer.append("name: " + show.getString(1) + "\n");
                                buffer.append("genre: " + show.getString(2) + "\n");
                                buffer.append("price: $" + show.getString(3) + "\n\n");
                            }

                            showMessage("Data", buffer.toString());*/
                        }
                     }
                 });
    }

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
