package com.codeclause.internship.scientificcalculator;

import java.text.DecimalFormat;

public class Math {

    public static final double PI = java.lang.Math.PI;
    public static final double E = java.lang.Math.E;
    private static final DecimalFormat decfor = new DecimalFormat("0.0000");

    public static double format(double data) {
        return Double.parseDouble(decfor.format(data));
    }

    public static double toRadians(double angle) {
        return format(java.lang.Math.toRadians(angle));
    }

    public static double sin(double angle) {
        return format(java.lang.Math.sin(angle));
    }

    public static double cos(double angle) {
        return format(java.lang.Math.cos(angle));
    }

    public static double pow(double base, double exp) {
        return format(java.lang.Math.pow(base, exp));
    }

    public static Double tan(double angle) {
        if (angle == 1.5708) return Double.NaN;
        return format(java.lang.Math.tan(angle));
    }

    public static Double log(double value) {
        return format(java.lang.Math.log(value));
    }

    public static Double log10(double value) {
        return format(java.lang.Math.log10(value));
    }
}
