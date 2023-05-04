package com.codeclause.internship.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Deque;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity {

    private TextView mMainDisplay, mResultDisplay;
    private Deque<Character> mExpression;
    private String mRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainDisplay = findViewById(R.id.main_display);
        mResultDisplay = findViewById(R.id.res_display);
        findViewById(R.id.backspace_btn).setOnLongClickListener(view -> {
            clear(true, true);
            return true;
        });
        mExpression = Utils.getCustomDequeInstance();
        mRes = "";
    }

    public void onPressHandler(View v) {
        if (v instanceof MaterialButton) {
            MaterialButton btn = (MaterialButton) v;
            char mText = btn.getText().charAt(0);
            if (btn.getId() == R.id.clear_btn) clear(true, true);
            else if (btn.getId() == R.id.calculate_btn) calculate();
            else {
                if (mRes.length() != 0) {
                    clear(true, true);
                }
                mExpression.add(mText);
                mMainDisplay.setText(mExpression.toString());
            }
        } else if (v instanceof AppCompatImageButton) {
            clear(false, false);
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculate() {
        try {
            mRes = "= " + Evaluator.solveExpression(mExpression.toString());
            mResultDisplay.setText(mRes);
        } catch (EmptyStackException ignored) {
        }
    }

    /**
     * This method is used to clear the Stack of the expression.
     * @param fullClear This is to check whether we need to clear
     *                  the whole stack or just remove the last.
     */
    private void clear(boolean fullClear, boolean clearResult) {
        try {
            if (clearResult) mRes = "";
            if (fullClear) mExpression.clear();
            else mExpression.removeLast();
        } catch (NoSuchElementException ignored) {
        } finally {
            mMainDisplay.setText(mExpression.toString());
            mResultDisplay.setText(mRes);
        }
    }
}