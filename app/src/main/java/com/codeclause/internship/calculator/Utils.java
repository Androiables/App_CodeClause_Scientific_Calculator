package com.codeclause.internship.calculator;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Utils {
    public static boolean isOperator(Character c) {
        return c == '+' || c == '-' || c == 'x' || c == '/' || c == '%' || c == '^';
    }

    public static CustomDeque getCustomDequeInstance() {
        return new CustomDeque();
    }

    private static class CustomDeque extends LinkedList<Character> {

        @Override
        public Character peekLast() {
            if (size() > 2)
                return get(size() - 2);
            else
                return super.peekLast();
        }

        public void removeLastIncludeSpaces() {
            super.removeLast();
            super.removeLast();
        }

        @Override
        public boolean add(Character c) {
            try {
                if (isOperator(c)) {
                    if (isOperator(peekLast())) {
                        removeLastIncludeSpaces();
                    } else {
                        super.add(' ');
                    }
                    super.add(c);
                    return super.add(' ');
                }
                return super.add(c);
            } catch (NullPointerException | IndexOutOfBoundsException | NoSuchElementException ignored) {
                if (!isOperator(c))
                    return super.add(c);
            }
            return false;
        }

        @NonNull
        @Override
        public String toString() {
            Iterator<Character> iterator = iterator();
            StringBuilder mExpression = new StringBuilder();
            while(iterator.hasNext())
                mExpression.append(iterator.next());
            return mExpression.toString();
        }
    }
}
