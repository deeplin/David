package com.david.common.util;

import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author: Ling Lin
 * created on: 2017/7/19 10:02
 * email: 10525677@qq.com
 * description:
 */

public class ReflectionUtil {

    public static void setMethod(Object obj, String method, int value) throws Exception {
        Method m = obj.getClass().getDeclaredMethod("set" + method, int.class);
        m.invoke(obj, value);
    }

    public static void setMethod(Object obj, String method, boolean value) throws Exception {
        Method m = obj.getClass().getDeclaredMethod("set" + method, boolean.class);
        m.invoke(obj, value);
    }

    public static void setMethod(Object obj, String method, String value) throws Exception {
        Method m = obj.getClass().getDeclaredMethod("set" + method, String.class);
        m.invoke(obj, value);
    }

    public static void addMethod(Object obj, String method, String value, String value2) throws Exception {
        Method m = obj.getClass().getDeclaredMethod("add" + method, String.class, String.class);
        m.invoke(obj, value, value2);
    }

    public static int getResourceStringId(Context context, String paramString) {
        return context.getResources().getIdentifier(paramString, "string",
                context.getPackageName());
    }

    /**
     * 根据名字，反射取得资源
     *
     * @param context context
     * @param name    resources name
     * @param type    enum of ResourcesType
     * @return resources id
     */
    public static int getResourceId(Context context, String name, ReflectionUtil.ResourcesType type) throws Exception {
        name = name.replace(".", "_");
        String className = context.getPackageName() + ".R";
        Class<?> c = Class.forName(className);
        for (Class childClass : c.getClasses()) {
            String simpleName = childClass.getSimpleName();

            if (simpleName.equals(type.name())) {
                for (Field field : childClass.getFields()) {
                    String fieldName = field.getName();
                    if (fieldName.equals(name)) {
                        return (int) field.get(null);
                    }
                }
            }
        }
        throw new Exception("Cannot find resource: " + name);
    }

    public enum ResourcesType {
        string,
        mipmap,
        layout,
        integer,
        id,
        drawable,
        dimen,
        color,
        bool,
    }
}