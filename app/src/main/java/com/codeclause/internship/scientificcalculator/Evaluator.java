package com.codeclause.internship.scientificcalculator;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.text.DecimalFormat;
import java.util.*;

public class Evaluator {

    public static final String PI = "π";
    public static final String E = "e";

    private String mExpression;
    private boolean mIsDeg;
    private static final DecimalFormat decfor = new DecimalFormat("0.0000");

    private static final Map<String, Integer> OPERATOR_PRECEDENCE;

    static {
        OPERATOR_PRECEDENCE = new HashMap<>();
        OPERATOR_PRECEDENCE.put("e", 0);
        OPERATOR_PRECEDENCE.put("+", 1);
        OPERATOR_PRECEDENCE.put("-", 1);
        OPERATOR_PRECEDENCE.put("*", 2);
        OPERATOR_PRECEDENCE.put("x", 2);
        OPERATOR_PRECEDENCE.put("/", 2);
        OPERATOR_PRECEDENCE.put("^", 3);
        OPERATOR_PRECEDENCE.put("sin", 1);
        OPERATOR_PRECEDENCE.put("cos", 1);
        OPERATOR_PRECEDENCE.put("tan", 1);
    }

    public Evaluator() {
        mExpression = "";
    }

    public Evaluator(String expression) {
        mExpression = expression;
    }

    public void changeDeg() {
        mIsDeg = !mIsDeg;
    }

    public void setExpression(String expression) {
        mExpression = expression;
    }
    public String solveExpression(String expression) {
        setExpression(expression);
        return solveExpression();
    }

    public String solveExpression() {
        Queue<String> rpnQueue = convertToRPN();
        return evaluateRPN(rpnQueue);
    }

    @NonNull
    private Queue<String> convertToRPN() {
        Queue<String> outputQueue = new LinkedList<>();
        Stack<Character> operatorStack = new Stack<>();

        StringBuilder currentNumber = new StringBuilder();
        boolean isParsingNumber = false;

        for (int i = 0; i < mExpression.length(); i++) {
            char c = mExpression.charAt(i);
            if (Character.isDigit(c) || c == '.' || (c == '-' && (i == 0 || !Character.isDigit(mExpression.charAt(i - 1))))) {
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

                if (c != 'x' && Character.isAlphabetic(c)) {
                    String operation = mExpression.substring(i, i + 3);
                    while (!operatorStack.isEmpty() && isHigherPrecedence(String.valueOf(operatorStack.peek()), operation)) {
                        outputQueue.add(String.valueOf(operatorStack.pop()));
                    }
                    operatorStack.push(c);
                    i = i + 2;

                } else if (OPERATOR_PRECEDENCE.containsKey(c + "")) {
                    while (!operatorStack.isEmpty() && isHigherPrecedence(String.valueOf(operatorStack.peek()), String.valueOf(c))) {
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

    @Nullable
    @Contract(pure = true)
    public static String isTrignometricFunction(char s) {
        return s == 's' ? "sin" :
                s == 'c' ? "cos" :
                        s == 't' ? "tan" : null;
    }

    private boolean isHigherPrecedence(String op1, String op2) {
        int precedence1 = OPERATOR_PRECEDENCE.containsKey(op1) ? OPERATOR_PRECEDENCE.get(op1) : 0;
        int precedence2 = OPERATOR_PRECEDENCE.containsKey(op2) ? OPERATOR_PRECEDENCE.get(op2) : 0;
        return precedence1 >= precedence2;
    }

    private String evaluateRPN(Queue<String> rpnQueue) {
        Stack<Double> operandStack = new Stack<>();
        double operand1 = 0, operand2;
        String ret;

        while (!rpnQueue.isEmpty()) {
            String token = rpnQueue.poll();
            if (isNumeric(token)) {
                operandStack.push(Double.parseDouble(token));
            } else {
                operand2 = operandStack.pop();
                if (isTrignometricFunction(token.charAt(0)) != null && mIsDeg) {
                    operand2 = Double.parseDouble(decfor.format(Math.toRadians(operand2)));
                }
                try {
                    operand1 = operandStack.pop();
                } catch (EmptyStackException ignored){}

                switch (token) {
                    case "+":
                        operandStack.push(operand1 + operand2);
                        break;
                    case "-":
                        operandStack.push(operand1 - operand2);
                        break;
                    case "x":
                    case "*":
                        operandStack.push(operand1 * operand2);
                        break;
                    case "/":
                        operandStack.push(operand1 / operand2);
                        break;
                    case "^":
                        operandStack.push(Math.pow(operand1, operand2));
                        break;
                    case "s":
                        operandStack.push(Double.parseDouble(decfor.format(Math.sin(operand2))));
                        break;
                    case "c":
                        operandStack.push(Double.parseDouble(decfor.format(Math.cos(operand2))));
                        break;
                    case "t":
                        if (operand2 == 1.5708) operandStack.push(Double.NaN);
                        else operandStack.push(Double.parseDouble(decfor.format(Math.tan(operand2))));
                        break;
                    case PI:
                        operandStack.push(Math.E);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + token);
                }
            }
        }

        if (operandStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression.");
        }

        ret = Double.toString(operandStack.pop());
        try {
            if (ret.substring(ret.indexOf('.')).equals(".0"))
                ret = ret.substring(0, ret.indexOf('.'));
        } catch (StringIndexOutOfBoundsException ignored) {
        }finally {
            return ret;
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public boolean getIsDeg() {
        return mIsDeg;
    }

    public static Evaluator getInstance() {
        return new Evaluator();
    }

    public static Evaluator getInstance(String expression) {
        return new Evaluator(expression);
    }

    public static String solveExpressionExplicit(String expression) {
        return getInstance(expression).solveExpression();
    }
}
