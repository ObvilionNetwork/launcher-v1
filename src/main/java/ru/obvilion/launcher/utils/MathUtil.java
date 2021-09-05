package ru.obvilion.launcher.utils;

public class MathUtil {
    public static double round(double value, int index) {
        int newValue = (int) Math.round(value * Math.pow(10, index));
        return newValue / Math.pow(10, index);
    }
}
