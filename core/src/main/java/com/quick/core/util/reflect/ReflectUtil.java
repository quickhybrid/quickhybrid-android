package com.quick.core.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by dailichun on 2017/12/7.
 * 反射机制调用工具类
 */
public class ReflectUtil {

    /**
     * 更改class的变量值
     *
     * @param className 类名(包括包名)
     * @param fieldName 变量名，必须是public类型
     * @param value     赋值
     * @return boolean
     */
    public static boolean setField(String className, String fieldName, String value) {
        try {
            Class<?> myclass = Class.forName(className);
            Field field = myclass.getField(fieldName);// 获取class中的变量
            Class<?> typeClass = field.getType();
            Constructor<?> con = typeClass.getConstructor(typeClass);
            Object o = con.newInstance(value);// 要赋的值
            field.set(myclass, o);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取class的变量值
     *
     * @param className 类名(包括包名)
     * @param fieldName 变量名，必须是public类型
     * @return Object
     */
    public static Object getField(String className, String fieldName) {
        try {
            Class<?> myclass = Class.forName(className);
            // 获取class中的变量
            Field field = myclass.getField(fieldName);
            return field.get(fieldName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 反射机制调用无参静态方法
     *
     * @param className  类名(包括包名)
     * @param methodName 方法名
     * @return
     */
    public static Object invokeMethod(String className, String methodName) {
        return invokeMethod(className,methodName,null,null);
    }

    /**
     * 反射调用方法
     *
     * @param className  类名(包括包名)
     * @param methodName 方法名
     * @param argsClass  参数类型数组
     * @param args       参数数组
     * @return 返回值
     * @throws Exception
     */
    public static Object invokeMethod(String className, String methodName, Class[] argsClass, Object[] args) {
        try {
            Class c = Class.forName(className);
            if (c != null) {
                Method method;
                if (argsClass == null) {
                    method = c.getMethod(methodName);
                } else {
                    method = c.getMethod(methodName, argsClass);
                }
                if (method != null) {
                    if (args == null) {
                        return method.invoke(c);
                    } else {
                        return method.invoke(c, args);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
