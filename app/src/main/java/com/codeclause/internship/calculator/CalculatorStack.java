package com.codeclause.internship.calculator;

import androidx.annotation.NonNull;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

public class CalculatorStack {
    Stack<Character> mExpression;
    CalculatorStack() {
        mExpression = new Stack<Character>();
    }

    public Character pop() {
        return mExpression.pop();
    }

    public Character peek() {
        return mExpression.peek();
    }

    public boolean isOperator() {
        return isOperator(mExpression.peek());
    }

    public boolean isNotEmpty() {
        return !mExpression.isEmpty();
    }

    private boolean isOperator(Character c) {
        return c == '+' || c == '-' || c == 'x' || c == '/' || c == '%';
    }

    public void push(Character s) {
        try {
            Character peek = peek();
            if (isOperator(s)) {
                if (isOperator()) {
                    pop();
                    mExpression.push(s);
                    return;
                }
            }
                mExpression.push(s);
        } catch (EmptyStackException e) {
            if (!isOperator(s))
                mExpression.push(s);
        }
    }

    public void clear() {
        mExpression.clear();
    }

    @NonNull
    @Override
    public String toString() {
        Iterator iter = mExpression.iterator();
        String mExpression = "";
        while(iter.hasNext())
           mExpression += iter.next();
        return mExpression;
    }
}
