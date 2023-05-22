package com.codeclause.internship.scientificcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.number.LocalizedNumberFormatter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Deque;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity {

    private TextView mMainDisplay, mResultDisplay;
    private Deque<String> mExpression;
    private String mRes;
    private Evaluator mEvaluator;

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
        mEvaluator = Evaluator.getInstance();
        mRes = "";
    }

    public void onPressHandler(View v) {
        if (v instanceof MaterialButton) {
            MaterialButton btn = (MaterialButton) v;
            String mText = btn.getText().toString();
            if (btn.getId() == R.id.inverse_btn) mText = "^-1";
            if (btn.getId() == R.id.clear_btn) clear(true, true);
            else if (btn.getId() == R.id.calculate_btn) calculate();
            else if (btn.getId() == R.id.change_deg_btn) changeDeg(btn);
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

    @SuppressLint("ResourceAsColor")
    private void changeDeg(MaterialButton button) {
        mEvaluator.changeDeg();
        Log.d("sfsdfs", mEvaluator.getIsDeg() + "");
        if (mEvaluator.getIsDeg()) {
            button.setTypeface(null, Typeface.BOLD_ITALIC);
        } else {
            button.setTypeface(null, Typeface.BOLD);
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculate() {
        try {

            mRes = "= " + Evaluator.solveExpression(mExpression.toString());
            mResultDisplay.setText(mRes);
        } catch (EmptyStackException ignored) {
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Mismatched parentheses.")) {
                mResultDisplay.setText("NaN");
            }
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
            else {
                mExpression.removeLast();
            }
        } catch (NoSuchElementException | NullPointerException ignored) {
        } finally {
            mMainDisplay.setText(mExpression.toString());
            mResultDisplay.setText(mRes);
        }
    }
}