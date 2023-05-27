package com.codeclause.internship.scientificcalculator;

import java.text.DecimalFormat;

public class Math {

    private static final DecimalFormat decfor = new DecimalFormat("0.0000");

    private static double format(double data) {
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
}
