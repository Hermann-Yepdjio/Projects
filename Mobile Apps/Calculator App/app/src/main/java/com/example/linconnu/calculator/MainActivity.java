package com.example.linconnu.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;

public class MainActivity extends AppCompatActivity
{
    Button button_1, button_2, button_3, button_4, button_5,button_6, button_7, button_8, button_9, button_0,
           button_dot, button_plus, button_minus, button_multiply, button_divide,button_equal, button_left_p,
           button_right_p, button_power, button_reset;
    TextView result_screen, expression_screen;
    String expression = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result_screen = (TextView) findViewById(R.id.result_screen);
        expression_screen = (TextView) findViewById(R.id.expression_screen);
        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);
        button_5 = (Button) findViewById(R.id.button_5);
        button_6 = (Button) findViewById(R.id.button_6);
        button_7 = (Button) findViewById(R.id.button_7);
        button_8 = (Button) findViewById(R.id.button_8);
        button_9 = (Button) findViewById(R.id.button_9);
        button_0 = (Button) findViewById(R.id.button_0);
        button_power = (Button) findViewById(R.id.button_power);
        button_plus = (Button) findViewById(R.id.button_plus);
        button_minus = (Button) findViewById(R.id.button_minus);
        button_multiply = (Button) findViewById(R.id.button_multiply);
        button_divide = (Button) findViewById(R.id.button_divide);
        button_dot = (Button) findViewById(R.id.button_dot);
        button_left_p = (Button) findViewById(R.id.button_left_p);
        button_right_p = (Button) findViewById(R.id.button_right_p);
        button_equal = (Button) findViewById(R.id.button_equal);
        button_reset = (Button) findViewById(R.id.button_reset);

        calculator();
    }

    public void calculator ()
    {
         result_screen = (TextView) findViewById(R.id.result_screen);
         expression_screen = (TextView) findViewById(R.id.expression_screen);
        button_1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 1;
                expression_screen.setText(expression);
            }
        });
        button_2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 2;
                expression_screen.setText(expression);
            }
        });
        button_3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 3;
                expression_screen.setText(expression);
            }
        });
        button_4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 4;
                expression_screen.setText(expression);
            }
        });
        button_5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 5;
                expression_screen.setText(expression);
            }
        });
        button_6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 6;
                expression_screen.setText(expression);
            }
        });
        button_7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 7;
                expression_screen.setText(expression);
            }
        });
        button_8.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 8;
                expression_screen.setText(expression);
            }
        });
        button_9.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 9;
                expression_screen.setText(expression);
            }
        });
        button_0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + 0;
                expression_screen.setText(expression);
            }
        });
        button_right_p.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + ")";
                expression_screen.setText(expression);
            }
        });
        button_left_p.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (expression.length()>0 && ((expression.charAt(expression.length()-1) >='0'  && expression.charAt(expression.length()-1) <='9')
                                          || expression.charAt(expression.length()-1)==')'))
                    expression = expression + "*(";
                else
                    expression = expression + "(";
                expression_screen.setText(expression);
            }
        });
        button_power.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + "^";
                expression_screen.setText(expression);
            }
        });
        button_divide.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + "/";
                expression_screen.setText(expression);
            }
        });
        button_dot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                expression = expression + ".";
                expression_screen.setText(expression);
            }
        });
        button_multiply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + "*";
                expression_screen.setText(expression);
            }
        });
        button_minus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + "-";
                expression_screen.setText(expression);
            }
        });
        button_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = expression + "+";
                expression_screen.setText(expression);
            }
        });
        button_equal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    Evaluator evaluator = new Evaluator();
                    if (evaluator.isValid(expression)) {
                        double result = evaluator.eval(expression);
                        if (result == POSITIVE_INFINITY || result == NEGATIVE_INFINITY)
                            result_screen.setText("Error. Please check Expression");
                        else
                            result_screen.setText(Double.toString(result));
                    } else
                        result_screen.setText("Error. Please check Expression");
                }
                catch (ArithmeticException ae){result_screen.setText("Error. Please check Expression");}
                catch (IllegalArgumentException iae){result_screen.setText("Error. Please check Expression");}


            }
        });
        button_reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                expression = "";
                expression_screen.setText(expression);
                result_screen.setText("");
            }
        });

    }




}
