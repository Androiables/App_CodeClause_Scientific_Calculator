package com.codeclause.internship.scientificcalculator;

import android.util.Log;

import org.apache.commons.math3.special.Gamma;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.codeclause.internship.scientificcalculator.Math;

public class Evaluator {

    private static final Map<Character, Integer> OPERATOR_PRECEDENCE;
    private boolean mDeg;
    private String mExpression;

    public static final String PI = "π";
    public static final String E = "e";

    public Evaluator() {
        mExpression = "";
    }

    public Evaluator(String expression) {
        mExpression = expression;
    }

    public static String isTrignometricFunction(char s) {
        return s == 's' ? "sin" :
                s == 'c' ? "cos" :
                s == 't' ? "tan" : null;
    }

    public void setExpression(String expression) {
        mExpression = expression;
    }

    static {
        OPERATOR_PRECEDENCE = new HashMap<>();
        OPERATOR_PRECEDENCE.put('!', 4);
        OPERATOR_PRECEDENCE.put('+', 1);
        OPERATOR_PRECEDENCE.put('-', 1);
        OPERATOR_PRECEDENCE.put('*', 2);
        OPERATOR_PRECEDENCE.put('/', 2);
        OPERATOR_PRECEDENCE.put('^', 3);
        OPERATOR_PRECEDENCE.put('s', 1);
        OPERATOR_PRECEDENCE.put('c', 1);
        OPERATOR_PRECEDENCE.put('t', 1);
        // Hacks for log - > g
        // ln -> n
        OPERATOR_PRECEDENCE.put('g', 3);
        OPERATOR_PRECEDENCE.put('n', 3);
        OPERATOR_PRECEDENCE.put('%', 4);
    }

    public String solveExpression(String expression) {
        setExpression(expression);
        return solveExpression();
    }

    public String solveExpression() {
        Queue<String> rpnQueue = convertToRPN();
        Log.d("df", rpnQueue.toString());
        return evaluateRPN(rpnQueue);
    }

    private Queue<String> convertToRPN() {
        Queue<String> outputQueue = new LinkedList<>();
        Stack<Character> operatorStack = new Stack<>();

        StringBuilder currentNumber = new StringBuilder();
        boolean isParsingNumber = false;

        for (int i = 0; i < mExpression.length(); i++) {
            char c = mExpression.charAt(i);
            if (Character.isDigit(c) || c == '.'
                    || (c == '-' && (i == 0 || !Character.isDigit(mExpression.charAt(i - 1))))
            ) {
                currentNumber.append(c);
                isParsingNumber = true;
            } else if (String.valueOf(c).equals(PI)) {
                isParsingNumber = true;
                currentNumber.append(Math.PI);
            } else if (String.valueOf(c).equals(E)) {
                isParsingNumber = true;
                currentNumber.append(Math.E);
            } else {
                if (isParsingNumber) {
                    outputQueue.add(currentNumber.toString());
                    currentNumber.setLength(0);
                    isParsingNumber = false;
                }
                if (Character.isAlphabetic(c)) {
                    // Check for log
                    if (c == 'l') {
                        if (mExpression.charAt(i + 2) == 'g')
                            operatorStack.push('g');
                        else if (mExpression.charAt(i+2) == 'n')
                            operatorStack.push('n');
                    }
                    else
                        operatorStack.push(c);
                    i = i+2;
                }
                else if (OPERATOR_PRECEDENCE.containsKey(c)) {
                    while (!operatorStack.isEmpty() && isHigherPrecedence(operatorStack.peek(), c)) {
                        outputQueue.add(String.valueOf(operatorStack.pop()));
                    }
                    operatorStack.push(c);
                } else if (c == '(') {
                    operatorStack.push(c);
                } else if (c == ')') {
                    while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                        outputQueue.add(String.valueOf(operatorStack.pop()));
                    }
                    if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
                        operatorStack.pop();
                    } else {
                        throw new IllegalArgumentException("Mismatched parentheses.");
                    }
                }
            }
        }

        if (isParsingNumber) {
            outputQueue.add(currentNumber.toString());
        }

        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == '(' || operatorStack.peek() == ')') {
                throw new IllegalArgumentException("Mismatched parentheses.");
            }
            outputQueue.add(String.valueOf(operatorStack.pop()));
        }

        return outputQueue;
    }

    private boolean isHigherPrecedence(char op1, char op2) {
        int precedence1 = OPERATOR_PRECEDENCE.containsKey(op1) ? OPERATOR_PRECEDENCE.get(op1) : 0;
        int precedence2 = OPERATOR_PRECEDENCE.containsKey(op2) ? OPERATOR_PRECEDENCE.get(op2) : 0;
        return precedence1 >= precedence2;
    }

    private String evaluateRPN(Queue<String> rpnQueue) {
        Stack<Double> operandStack = new Stack<>();
        double operand1 = 0, operand2;

        while (!rpnQueue.isEmpty()) {
            String token = rpnQueue.poll();
            if (isNumeric(token)) {
                operandStack.push(Double.parseDouble(token));
            } else {
                operand2 = operandStack.pop();
                if (isTrignometricFunction(token.charAt(0)) != null) {
                    if (mDeg)
                        operand2 = Math.toRadians(operand2);
                } else {
                    try {
                        operand1 = operandStack.pop();
                    } catch (EmptyStackException ignored) {}
                }

                switch (token) {
                    case "+":
                        operandStack.push(operand1 + operand2);
                        break;
                    case "-":
                        operandStack.push(operand1 - operand2);
                        break;
                    case "*":
                        operandStack.push(operand1 * operand2);
                        break;
                    case "/":
                        operandStack.push(operand1 / operand2);
                        break;
                    case "^":
                        operandStack.push(Math.pow(operand1, operand2));
                        break;
                    case "!":
                        operandStack.push(factorial(operand2));
                        Log.d("fsdf", operand2 + "");
                        break;
                    case "%":
                        operandStack.push(operand1 * operand2 / 100.0);
                        break;
                    case "s":
                        operandStack.push(Math.sin(operand2));
                        break;
                    case "c":
                        operandStack.push(Math.cos(operand2));
                        break;
                    case "t":
                        operandStack.push(Math.tan(operand2));
                        break;
                    case "g":
                        operandStack.push(Math.log10(operand2));
                        break;
                    case "n":
                        operandStack.push(Math.log(operand2));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + token);
                }
            }
        }

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression.");
        }

        String ret = Double.toString(operandStack.pop());
        try {
            if (ret.substring(ret.indexOf('.')).equals(".0"))
                ret = ret.substring(0, ret.indexOf('.'));
        } catch (StringIndexOutOfBoundsException ignored) {
        }finally {
            return ret;
        }
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static double factorial(double n) {
        return Gamma.gamma(n + 1);
    }

    public void changeDeg() {
        mDeg = !mDeg;
    }

    public static Evaluator getInstance() {
        return new Evaluator();
    }

    public static Evaluator getInstance(String mExpression) {
        return new Evaluator(mExpression);
    }

    public static String solve(String expression) {
        return getInstance(expression).solveExpression();
    }

    public boolean getIsDeg() {
        return mDeg;
    }
}