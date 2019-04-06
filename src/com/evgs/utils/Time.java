package com.evgs.utils;

public class Time {
    public static final long SECOND = 1000000000l; // секунда в наносекундах

    public static long get() { // возвращает время
        return System.nanoTime();
    }
}
