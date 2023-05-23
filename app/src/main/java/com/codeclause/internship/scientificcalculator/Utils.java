package com.codeclause.internship.scientificcalculator;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Utils {
    public static boolean isOperator(String s) {
        char c = s.charAt(0);
        return c == '+' || c == '-' || c == 'x' || c == '/' || c == '%' || c == '^';
    }

    public static boolean isBracket(String c) {
        return c.equals("(") || c.equals(")");
    }

    public static CustomDeque getCustomDequeInstance() {
        return new CustomDeque();
    }

    private static class CustomDeque extends LinkedList<String> {

        CustomDeque() {
            add("0");
        }

        @Override
        public void clear() {
            super.clear();
            add("0");
        }

        @Override
        public boolean add(String c) {
            try {
                if (c.equals(Evaluator.PI) || c.equals(Evaluator.E)) {
                    removeHeadingZero();
                }
                if (Evaluator.isTrignometricFunction(c.charAt(0)) != null) {
                    removeHeadingZero();
                    return super.add(Evaluator.isTrignometricFunction(c.charAt(0)) + "(");
                }
                if (isOperator(c)) {
                    if (isOperator(peekLast())) {
                        removeLast();
                    }
                    if (peekLast().equals("(") && !c.equals("-"))return  false;
                    return super.add(c);
                } else if (isBracket(c)) {
                    return super.add(c);
                }
                return super.add(c);
            } catch (NullPointerException | IndexOutOfBoundsException | NoSuchElementException ignored) {
                if (!isOperator(c))
                    return super.add(c);
            }
            return false;
        }

        private void removeHeadingZero() {
            if (size() == 1 && peekLast().equals("0"))
                removeLast();
        }

        @NonNull
        @Override
        public String toString() {
            Iterator<String> iterator = iterator();
            StringBuilder mExpression = new StringBuilder();
            while(iterator.hasNext())
                mExpression.append(iterator.next());
            return mExpression.toString();
        }
    }
}
