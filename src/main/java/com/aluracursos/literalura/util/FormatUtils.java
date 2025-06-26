package com.aluracursos.literalura.util;

//ayuda a redondear truncar valores decimales
public class FormatUtils {
    public static String formatDouble(double value) {
        return String.format("%.2f", value); // 2 decimales
    }

    public static String formatInt(int value) {
        return String.format("%d", value);
    }

    public static String formatLong(long value) {
        return String.format("%d", value);
    }
}
