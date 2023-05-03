package com.codeclause.internship.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private TextView mMainDisplay;
    private CalculatorStack mExpression;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainDisplay = (TextView) findViewById(R.id.display);
        mExpression = new CalculatorStack();
    }

    public void onPressHandler(View v) {
        if (v instanceof MaterialButton) {
            MaterialButton btn = (MaterialButton) v;
            char mText = btn.getText().charAt(0);
            if (mText == 'C') clear();
            else if (mText == '=') calculate();
            else {
                mExpression.push(mText);
                mMainDisplay.setText(mExpression.toString());
            }
        }
    }

    private String operation(String op1, String op2, Character op) {
        double n1 = Double.parseDouble(op1);
        double n2 = Double.parseDouble(op2);
        if (op == '+')
            return (n1 + n2) + "";
        if (op == '-')
            return (n1 - n2) + "";
        if (op == '/')
            return (n1 / n2) + "";
        if (op == 'x')
            return (n1 * n2) + "";
        return "NaN";
    }

    @SuppressLint("SetTextI18n")
    private void calculate() {
        StringBuilder exp1 = new StringBuilder();
        StringBuilder exp2 = new StringBuilder();
        Character opr = '\0';
        while (mExpression.isNotEmpty()) {
            if (mExpression.isOperator())
                opr = mExpression.peek();
            else if (opr == '\0')
                exp2.append(mExpression.peek());
            else
                exp1.append(mExpression.peek());
            mExpression.pop();
        }
        exp1.reverse();
        exp2.reverse();

        mMainDisplay.setText(operation(String.valueOf(exp1), String.valueOf(exp2), opr));
    }

    private void clear() {
        mExpression.clear();
        mMainDisplay.setText(null);
    }
}