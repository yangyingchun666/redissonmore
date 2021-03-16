package com.yyc.lock_demo.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @program: aqs
 * @description:
 * @author: Anakin Yang
 * @create: 2021-01-19 21:19
 **/
public class UnsafeInstanceUtils {
    public static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
