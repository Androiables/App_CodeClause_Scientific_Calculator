package com.codeclause.internship.scientificcalculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Evaluator local unit test.
 */
public class EvaluatorUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals("4", Evaluator.solveExpression("2 + 2"));
    }

    @Test
    public void subtraction_isCorrect() {
        assertEquals("4", Evaluator.solveExpression("6 - 2"));
    }

    @Test
    public void product1_isCorrect() {
        assertEquals("4", Evaluator.solveExpression("2 x 2"));
    }

    @Test
    public void product2_isCorrect() {
        assertEquals("4", Evaluator.solveExpression("2 * 2"));
    }

    @Test
    public void division_isCorrect() {
        assertEquals("3", Evaluator.solveExpression("6 / 2"));
    }

    @Test
    public void divisionByZero_isCorrect() {
        assertEquals("Infinity", Evaluator.solveExpression("6 / 0"));
    }

    @Test
    public void divisionZeroByZero_isCorrect() {
        assertEquals("NaN", Evaluator.solveExpression("0 / 0"));
    }

    @Test
    public void power_isCorrect() {
        assertEquals("4", Evaluator.solveExpression("2 ^ 2"));
    }

    @Test
    public void generalExpression_isCorrect() {
        assertEquals("-12", Evaluator.solveExpression("2 + 1 - 5 * 6 / 2 ^ 1"));
    }

    @Test
    public void bracesExpression_isCorrect() {
        assertEquals("6", Evaluator.solveExpression("( 2 + 1 ) * 2"));
    }

    @Test
    public void doubleExpression_isCorrect() {
        assertEquals("3.5", Evaluator.solveExpression("( ( 2.5 + 3.5 ) + 1.0 ) / 2.0"));
    }

    @Test
    public void inverseExpression_isCorrect() {
        assertEquals("0.2", Evaluator.solveExpression("2^-1"));
    }
}