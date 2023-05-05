package com.codeclause.internship.calculator;

import java.util.*;

public class Evaluator {

    public static String solveExpression(String expression) {
        Queue<String> outputQueue = new LinkedList<String>();
        Stack<String> operatorStack = new Stack<String>();
        String[] tokens = expression.split("\\s+");
        String ret;

        for (String token : tokens) {
            if (isNumber(token)) {
                outputQueue.add(token);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && hasHigherPrecedence(operatorStack.peek(), token)) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.peek().equals("(")) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.pop();
            }
        }

        while (!operatorStack.isEmpty()) {
            outputQueue.add(operatorStack.pop());
        }

        Stack<Double> evaluationStack = new Stack<Double>();

        for (String token : outputQueue) {
            if (isNumber(token)) {
                evaluationStack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                double operand2 = evaluationStack.pop();
                double operand1 = evaluationStack.pop();
                double result = applyOperator(token.charAt(0), operand1, operand2);
                evaluationStack.push(result);
            }
        }
        ret = Double.toString(evaluationStack.pop());
        try {
            if (ret.substring(ret.indexOf('.')).equals(".0"))
                ret = ret.substring(0, ret.indexOf('.'));
        } catch (StringIndexOutOfBoundsException ignored) {
        }finally {
            return ret;
        }
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isOperator(String token) {
        return token.matches("[+\\-x*/^]");
    }

    private static boolean hasHigherPrecedence(String operator1, String operator2) {
        int precedence1 = getPrecedence(operator1);
        int precedence2 = getPrecedence(operator2);
        return precedence1 >= precedence2;
    }

    private static int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "x":
            case "/":
                return 2;
            case "(":
            case ")":
                return 0;
            case "^":
                return 3;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    private static double applyOperator(char operator, double operand1, double operand2) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
            case 'x':
                return operand1 * operand2;
            case '/':
                return operand1 / operand2;
            case '^':
                return Math.pow(operand1, operand2);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}