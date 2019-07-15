package com.example.roundcornerlayout.reflect;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * @author: robin
 * @description:反射工具类
 * @date: 2014/4/12
 **/
public class ReflectHelper {
    private static final String TAG = "ReflectHelper";

    /**
     * 调用该对象所有可调用的公有方法,包括父类方法
     * 参数：obj 调用者对象
     * methodname 调用的方法名，与obj合在一起即为 obj.methodname
     * types      调用方法的参数类型
     * values	调用方法的参数值
     * 返回     methodname所返回的对象
     */
    public static Object callMethod(Object obj, String methodname, Class<?> types[], Object values[]) {
        // 注：数组类型为:基本类型+[].class,如String[]写成 new Class<?>[]{String[].class}
        if (obj == null) {
            return null;
        }
        Class<?> classz = obj.getClass();
        Method method = null;
        Object retValue = null;
        try {
            method = classz.getMethod(methodname, types);
            retValue = method.invoke(obj, values);
        } catch (NoSuchMethodException ex) {
        } catch (Exception ex) {
            Log.e(TAG, "callMethod " + methodname + " reason=" + ex + " " + ex.getMessage());
        }
        return retValue;
    }

    /**
     * 调用该对象所声明的方法，不包括父类方法，可以调用到该方法的私有方法
     * 参数：obj 调用者对象
     * methodname 调用的方法名，与obj合在一起即为 obj.methodname
     * types      调用方法的参数类型，
     * 注：数组类型为:基本类型+[].class,如String[]写成 new Class<?>[]{String[].class}，
     * int 类型是为int.class
     * values	调用方法的参数值
     * 返回     methodname所返回的对象
     */
    public static Object callDeclaredMethod(Object obj, String methodname, Class<?> types[], Object values[]) {
        if (obj == null) {
            return null;
        }
        Class<?> classz = obj.getClass();
        Method method = null;
        Object retValue = null;
        try {
            method = classz.getDeclaredMethod(methodname, types);
            // 设置安全检查，设为true使得可以访问私有方法
            method.setAccessible(true);
            retValue = method.invoke(obj, values);
        } catch (NoSuchMethodException ex) {
        } catch (Exception ex) {
            Log.e(TAG, "callDeclaredMethod " + methodname + " reason=" + ex + " " + ex.getMessage());
        }
        return retValue;
    }

    public static Object callDeclaredMethod(Object obj, String declaredclassname,
                                            String methodname, Class<?> types[], Object values[]) {
        Class<?> classz = null;
        Method method = null;
        Object retValue = null;
        try {
            classz = Class.forName(declaredclassname);
            method = classz.getDeclaredMethod(methodname, types);
            // 设置安全检查，设为true使得可以访问私有方法
            method.setAccessible(true);
            retValue = method.invoke(obj, values);
        } catch (NoSuchMethodException ex) {
        } catch (Exception ex) {
            Log.e(TAG, "callDeclaredMethod2 " + declaredclassname + ":" + methodname + " reason=" + ex + " " + ex.getMessage());
        }
        return retValue;
    }

    /**
     * 调用该类的静态方法，包括静态方法
     * 参数：classz 	类类对象
     * methodname 调用的方法名，与obj合在一起即为 obj.methodname
     * types      调用方法的参数类型，
     * 注：数组类型为:基本类型+[].class,如String[]写成 new Class<?>[]{String[].class}，
     * int 类型是为int.class
     * values	调用方法的参数值
     * 返回     methodname所返回的对象
     */
    public static Object callStaticMethod(Class<?> classz, String methodname, Class<?> types[], Object values[]) {
        Method method = null;
        Object retValue = null;
        try {
            method = classz.getDeclaredMethod(methodname, types);
            method.setAccessible(true);
            retValue = method.invoke(null, values);
        } catch (NoSuchMethodException ex) {
        } catch (Exception ex) {
            Log.e(TAG, "callStaticMethod " + methodname + "," + ex + " " + ex.getMessage());
        }
        return retValue;
    }

    /**
     * 调用该类的静态方法，包括静态方法
     * 参数：className 	类名
     * methodname 调用的方法名，与obj合在一起即为 obj.methodname
     * types      调用方法的参数类型，
     * 注：数组类型为:基本类型+[].class,如String[]写成 new Class<?>[]{String[].class}，
     * int 类型是为int.class
     * values	调用方法的参数值
     * 返回     methodname所返回的对象
     */
    public static Object callStaticMethod(String className, String methodname, Class<?> types[], Object values[]) {
        Class<?> classz;
        try {
            classz = Class.forName(className);
            return callStaticMethod(classz, methodname, types, values);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "callStaticMethod2 " + className + ":" + methodname + " reason=" + e + " " + e.getMessage());
        }
        return null;
    }

    public static <T> T callMethod(Object receiver, Method method, Object... args) {
        try {
            return (T) method.invoke(receiver, args);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "callDeclaredMethod", e);
        } catch (InvocationTargetException e) {
            Log.w(TAG, "callDeclaredMethod", e);
        } catch (Exception e) {
            Log.w(TAG, "callDeclaredMethod--" + method, e);
        }
        return null;
    }

    public static final Method getDeclaredMethod(Class<?> clazz, String method, Class<?>... parameterTypes) {
        Method met = null;
        try {
            met = clazz.getDeclaredMethod(method, parameterTypes);
            if (!met.isAccessible()) {
                met.setAccessible(true);
            }
        } catch (SecurityException e) {
            Log.w(TAG, "getMethod--" + method, e);
        } catch (NoSuchMethodException e) {
        } catch (Exception e) {
            Log.w(TAG, "getMethod--" + method, e);
        }
        return met;
    }

    /**
     * 获得对象的公有数据成员,包括父类
     * 参数： obj 	对象
     * fieldname  公有成员变量名
     * 返回     成员对象
     */
    public static Object getFieldValue(Object obj, String fieldname) {
        if (obj == null) {
            return null;
        }
        Class<?> classz = obj.getClass();
        Field field = null;
        Object retValue = null;
        try {
            field = classz.getField(fieldname);
            retValue = field.get(obj);
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "getFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "getFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
        return retValue;
    }

    /**
     * 获得对象所在类的数据成员，包括私有成员
     * 参数： obj 	对象
     * fieldname  成员变量名
     * 返回     成员对象
     */
    public static Object getDeclaredFieldValue(Object obj, String fieldname) {
        if (obj == null) {
            return null;
        }
        Class<?> classz = obj.getClass();
        Field field = null;
        Object retValue = null;
        try {
            field = classz.getDeclaredField(fieldname);
            field.setAccessible(true);
            retValue = field.get(obj);
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "getDeclaredFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "getDeclaredFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
        return retValue;
    }

    public static Object getDeclaredFieldValue(Object obj,
                                               String declaredclassname, String fieldname) {
        Class<?> classz = null;
        Field field = null;
        Object retValue = null;
        try {
            classz = Class.forName(declaredclassname);
            field = classz.getDeclaredField(fieldname);
            field.setAccessible(true);
            retValue = field.get(obj);
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "getDeclaredFieldValue2 " + declaredclassname + ":" + fieldname + ",reason=" + ex + " " + ex.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "getDeclaredFieldValue2 " + declaredclassname + ":" + fieldname + ",reason=" + e + " " + e.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "getDeclaredFieldValue2 " + declaredclassname + ":" + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
        return retValue;
    }

    /**
     * 获得类的静态数据成员，包括私有成员
     * 参数： classz 	类类对象
     * fieldname  成员变量名
     * 返回     成员对象
     */

    public static Object getStaticFieldValue(Class<?> classz, String fieldname) {
        Field field = null;
        Object retValue = null;
        try {
            field = classz.getDeclaredField(fieldname);
            field.setAccessible(true);
            retValue = field.get(null);
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "getStaticFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "getStaticFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
        return retValue;
    }

    /**
     * 获得类的静态数据成员，包括私有成员
     * 参数： classname 	类名称
     * fieldname  成员变量名
     * 返回     成员对象
     */
    public static Object getStaticFieldValue(String className, String fieldname) {
        try {
            Class<?> classz = Class.forName(className);
            return getStaticFieldValue(classz, fieldname);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "getStaticFieldValue2 " + className + ":" + fieldname + ",reason=" + e + " " + e.getMessage());
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String name) {
        try {
            Field f = clazz.getField(name);
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            return f;
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "getField", e);
        } catch (Exception e) {
            Log.w(TAG, "getField--" + name, e);
        }
        return null;
    }

    public static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            return f;
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "getDeclaredField", e);
        } catch (Exception e) {
            Log.w(TAG, "getDeclaredField--" + name, e);
        }
        return null;
    }

    public static <T> T getFieldValue(Object obj, Field field) {
        try {
            return (T) field.get(obj);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "getFieldValue", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "getFieldValue", e);
        } catch (Exception e) {
            Log.w(TAG, "getFieldValue", e);
        }
        return null;
    }

    public static boolean getFieldValue(Object obj, Field field, boolean defaultVal) {
        try {
            return field.getBoolean(obj);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "getFieldValue", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "getFieldValue", e);
        } catch (Exception e) {
            Log.w(TAG, "getFieldValue", e);
        }
        return defaultVal;
    }

    public static int getFieldValue(Object obj, Field field, int defaultVal) {
        try {
            return field.getInt(obj);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "getFieldValue", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "getFieldValue", e);
        } catch (Exception e) {
            Log.w(TAG, "getFieldValue", e);
        }
        return defaultVal;
    }

    public static boolean setFieldValue(Object obj, Field field, Object value) {
        try {
            field.set(obj, value);
            return true;
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "setFieldValue", e);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "setFieldValue", e);
        }
        return false;
    }

    /**
     * 赋值对象的公有数据成员
     * 参数： classname 	类名称
     * fieldname  成员变量名
     * value	赋值
     * 返回     成员对象
     */
    public static void setFieldValue(Object obj, String fieldname, Object value) {

        Class<?> classz = null;
        try {
            classz = obj.getClass();
            Field field = classz.getField(fieldname);
            field.set(obj, value);
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "setFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "setFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
    }

    /**
     * 赋值对象的数据成员,包括私有
     * 参数： classname 	类名称
     * fieldname  成员变量名
     * value	赋值
     * 返回     成员对象
     */
    public static void setDeclaredFieldValue(Object obj, String fieldname, Object value) {
        Class<?> classz = null;
        try {
            classz = obj.getClass();
            Field field = classz.getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "setDeclaredFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "setDeclaredFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
    }

    public static void setDeclaredFieldValue(Object obj, String declaredclassname
            , String fieldname, Object value) {
        try {
            Class<?> classz;
            classz = Class.forName(declaredclassname);
            Field field = classz.getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(obj, value);


        } catch (ClassNotFoundException e) {
            Log.e(TAG, "setDeclaredFieldValue2 " + declaredclassname + ":" + fieldname + ",reason=" + e + " " + e.getMessage());
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "setDeclaredFieldValue2 " + declaredclassname + ":" + fieldname + ",reason=" + ex + " " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "setDeclaredFieldValue2 " + declaredclassname + ":" + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
    }

    public static Vector<String> getDeclaredFieldNames(Object obj) {
        Vector<String> fieldnames = new Vector<String>();
        Class<?> classz = obj.getClass();
        Field fields[] = classz.getFields();
        for (Field field : fields)
            fieldnames.add(field.getName());
        return fieldnames;
    }

    public static void setStaticFieldValue(Class<?> classz, String fieldname, Object value) {
        try {
            Field field = classz.getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(null, value);
        } catch (NoSuchFieldException ex) {
            Log.e(TAG, "setStaticFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "setStaticFieldValue " + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
    }

    /**
     * 给静态成员变量赋值
     *
     * @param className 类名
     * @param fieldname 静态变量名
     * @param value     赋值
     */
    public static void setStaticFieldValue(String className, String fieldname, Object value) {
        try {
            Class<?> classz = Class.forName(className);
            setStaticFieldValue(classz, fieldname, value);
        } catch (ClassNotFoundException ex) {
            Log.e(TAG, "setStaticFieldValue " + className + ":" + fieldname + ",reason=" + ex + " " + ex.getMessage());
        }
    }

    /**
     * 生成指定类的实例
     *
     * @param classz    类
     * @param argsClass 构造参数类型
     * @param objects   构造参数值
     * @return 实例
     */
    public static Object newInstance(Class<?> classz, Class<?> argsClass[], Object objects[]) {
        Object retObj = null;
        try {
            Constructor<?> constructor = classz.getDeclaredConstructor(argsClass);
            constructor.setAccessible(true);
            retObj = constructor.newInstance(objects);
        } catch (NoSuchMethodException ex) {
            Log.e(TAG, "newInstance fail, reason=" + ex + ",cause=" + ex.getCause() + ",clazz=" + classz);
        } catch (Exception ex) {
            Log.e(TAG, "newInstance fail, reason=" + ex + ",cause=" + ex.getCause() + ",clazz=" + classz);
        }
        return retObj;
    }

    /**
     * 生成类的实例
     *
     * @param classz 类的类类型
     * @return 实例
     */
    public static Object newInstance(Class<?> classz) {
        try {
            Constructor<?> constructor = classz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (IllegalAccessException e) {
            Log.e(TAG, "newInstance1, reason=" + e + ",cause=" + e.getCause() + ",clazz=" + classz);
        } catch (InstantiationException e) {
            Log.e(TAG, "newInstance2, reason=" + e + ",cause=" + e.getCause() + ",clazz=" + classz);
        } catch (Exception e) {
            Log.e(TAG, "newInstance2, reason=" + e + ",cause=" + e.getCause() + ",clazz=" + classz);
        }
        return null;
    }

    /**
     * 生成类有实例数组
     */
    public static Object newInstance(Class<?> classz, int length) {
        return Array.newInstance(classz, length);
    }

    public static Object newInstance(String className, int length) {
        Class<?> classz;
        try {
            classz = Class.forName(className);
            return newInstance(classz, length);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "newInstance3 " + e + ",cause=" + e.getCause() + ",clazz=" + className + ",length=" + length);
            return null;
        }
    }

    /**
     * 生成类实例
     *
     * @param className 类名
     * @param argsClass 构造参数类型
     * @param objects   构造参数
     * @return 类实例
     */
    public static Object newInstance(String className, Class<?> argsClass[], Object objects[]) {
        try {
            Class<?> classz = Class.forName(className);
            return newInstance(classz, argsClass, objects);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "newInstance4 " + e + ",cause=" + e.getClass());
        }
        return null;
    }

    /**
     * 构造类的实例
     *
     * @param className 类名
     * @return 类实例
     */
    public static Object newInstance(String className) {
        try {
            Class<?> classz = Class.forName(className);
            return newInstance(classz);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "newInstance5 " + e + ",cause=" + e.getClass());
        }
        return null;
    }

    /**
     * 判断类是否支持指定的方法
     *
     * @param className  类名
     * @param methodName 方法名
     * @param argClass   方法的参数类型
     * @return
     */
    public static boolean methodSupported(String className, String methodName, Class<?> argClass[]) {
        try {
            Class<?> classz = Class.forName(className);
            try {
                if (methodName.equals("<init>")) {
                    Constructor<?> constructor = classz.getDeclaredConstructor(argClass);
                    return constructor != null;
                } else {
                    classz.getDeclaredMethod(methodName, argClass);
                }
            } catch (SecurityException e) {
                return false;
            } catch (NoSuchMethodException e) {
                try {
                    classz.getMethod(methodName, argClass);
                    return true;
                } catch (Exception e1) {
                }
                return false;
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断obj是否支持指定的方法
     *
     * @param obj
     * @param methodName
     * @param argClass
     * @return
     */
    public static boolean methodSupported(Object obj, String methodName, Class<?> argClass[]) {
        Class<?> classz = obj.getClass();
        return methodSupported(classz.getName(), methodName, argClass);
    }

    /**
     * 检查指定的类是否存在
     *
     * @param className
     * @return
     */
    public static boolean classSupported(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断一个类是否从指定的类派生的
     *
     * @param className：需要检查的类
     * @param baseclazz：基类
     * @return
     */
    public static boolean classDerivedFrom(String className, Class<?> baseclazz) {
        try {
            Class<?> clazz = Class.forName(className);
            boolean flag = false;
            do {
                if (clazz.equals(baseclazz))
                    return true;
                clazz = clazz.getSuperclass();
                if (clazz == null)
                    return false;
            } while (!(flag = clazz.equals(baseclazz)));

            return flag;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断一个类是否从指定的类派生的
     *
     * @param className：需要检查的类
     * @param strbaseclazz：基类
     * @return
     */
    public static boolean classDerivedFrom(String className, String strbaseclazz) {
        try {
            Class<?> baseclazz = Class.forName(strbaseclazz);
            return classDerivedFrom(className, baseclazz);
        } catch (Exception e) {
            Log.e(TAG, "classDerivedFrom " + className + "," + strbaseclazz + ",reason=" + e + " " + e.getMessage());
        }
        return false;
    }
}
