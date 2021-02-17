package com.example.demo;

import java.lang.reflect.Field;

public class Utils {
    public static void injectObjects(Object target, String field, Object toInject){
        boolean wasPrivateMethod = false;

        try {
            Field f = target.getClass().getDeclaredField(field);

            if(!f.isAccessible()){
                f.setAccessible(true);
                wasPrivateMethod = true;
            }
            f.set(target, toInject);
            if(wasPrivateMethod){
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
