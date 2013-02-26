/*
 * AwA Minecraft's mod commons.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 alalwww
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * 以下に定める条件に従い、本ソフトウェアおよび関連文書のファイル（以下「ソフトウェア」）の複製
 * を取得するすべての人に対し、ソフトウェアを無制限に扱うことを無償で許可します。これには、ソフ
 * トウェアの複製を使用、複写、変更、結合、掲載、頒布、サブライセンス、および/または販売する権
 * 利、およびソフトウェアを提供する相手に同じことを許可する権利も無制限に含まれます。
 *
 * 上記の著作権表示および本許諾表示を、ソフトウェアのすべての複製または重要な部分に記載するもの
 * とします。
 *
 * ソフトウェアは「現状のまま」で、明示であるか暗黙であるかを問わず、何らの保証もなく提供されま
 * す。ここでいう保証とは、商品性、特定の目的への適合性、および権利非侵害についての保証も含みま
 * すが、それに限定されるものではありません。 作者または著作権者は、契約行為、不法行為、または
 * それ以外であろうと、ソフトウェアに起因または関連し、あるいはソフトウェアの使用またはその他の
 * 扱いによって生じる一切の請求、損害、その他の義務について何らの責任も負わないものとします。
 */

package net.awairo.minecraft.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import net.minecraft.world.World;

/**
 * reflection helper.
 * 
 * @author alalwww
 * @version 1.0.0
 */
public class ReflectionHelper
{
    // 難読化されてるかチェック クラスの無意味な依存関係ができる点は目を瞑る方向
    private static final boolean OBFUSCATED = World.class.getPackage() != null;

    private static enum Type
    {
        FIELD_BY_INDEX, FIELD_BY_NAME, METHOD_BY_INDEX, METHOD_BY_NAME,
    }

    /**
     * check for installed of other mod.
     * 
     * <p>難読化されている場合、引数の完全名の先頭が net.minecraft.src. だった場合、その部分を削除します。</p>
     * 
     * @param modClassName
     *            mod full name.
     * @return true is installed.
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static boolean findMod(String modClassName) throws RuntimeException
    {
        if (OBFUSCATED && modClassName.startsWith("net.minecraft.src."))
        {
            modClassName = modClassName.substring(18, modClassName.length());
        }

        try
        {
            Class.forName(modClassName);
            return true;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }

    /**
     * get private value by prop index.
     * 
     * @param clazz
     *            target class
     * @param obj
     *            target class instance or null
     * @param fieldIndex
     *            target class field index
     * @return private value
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static <T, E> T getPrivateValue(Class<? super E> clazz, E instance, int fieldIndex) throws RuntimeException
    {
        return get(Type.FIELD_BY_INDEX, clazz, instance, Integer.valueOf(fieldIndex));
    }

    /**
     * get private value by prop name.
     * 
     * @param clazz
     *            target class
     * @param obj
     *            target class instance or null
     * @param fieldName
     *            target class field name
     * @return private value or null
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static <T, E> T getPrivateValue(Class<? super E> clazz, E instance, String fieldName)
            throws RuntimeException
    {
        return get(Type.FIELD_BY_NAME, clazz, instance, fieldName);
    }

    /**
     * get private value by prop name.
     * 
     * @param clazz
     *            target class
     * @param obj
     *            target class instance or null
     * @param fieldName
     *            target class field name
     * @param obfuscatedFieldName
     *            target class obfuscated field name
     * @return private value or null
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static <T, E> T getPrivateValue(Class<? super E> clazz, E instance, String fieldName,
            String obfuscatedFieldName)
            throws RuntimeException
    {
        return getPrivateValue(clazz, instance, (OBFUSCATED ? obfuscatedFieldName : fieldName));
    }

    /**
     * get method by index.
     * 
     * @param clazz
     *            target class
     * @param methodIndex
     *            target class method index
     * @return method
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static <E> Method getMethod(Class<? super E> clazz, int methodIndex) throws RuntimeException
    {
        return get(Type.METHOD_BY_INDEX, clazz, null, Integer.valueOf(methodIndex));
    }

    /**
     * get method by name.
     * 
     * @param clazz
     *            target class
     * @param methodName
     *            target class method name
     * @return method
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static <E> Method getMethod(Class<? super E> clazz, String methodName) throws RuntimeException
    {
        return get(Type.METHOD_BY_NAME, clazz, null, methodName);
    }

    /**
     * get method by name.
     * 
     * @param clazz
     *            target class
     * @param methodName
     *            target class method name
     * @param obfuscatedMethodName
     *            target class obfuscated method name
     * @return method
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static <E> Method getMethod(Class<? super E> clazz, String methodName, String obfuscatedMethodName)
            throws RuntimeException
    {
        return getMethod(clazz, null, (OBFUSCATED ? obfuscatedMethodName : methodName));
    }

    /**
     * invoke method.
     * 
     * @param m
     *            method
     * @param instance
     *            class instance or null
     * @param args
     *            arguments
     * @return result
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static <E, R> R invoke(Method m, Object instance, Object... args) throws RuntimeException
    {
        try
        {
            return (R) m.invoke(instance, args);
        }
        catch (Exception e)
        {
            Object argsValue;

            if (args == null || !args.getClass().isArray())
            {
                argsValue = args;
            }
            else
            {
                argsValue = Arrays.toString(args);
            }

            String f = "reflection failed. (class=%s, method=%s, instance=%s, args=%s)";
            Log.severe(e, f, m.getDeclaringClass().getName(), m.getName(), instance, argsValue);

            if (e instanceof RuntimeException)
            {
                throw (RuntimeException) e;
            }

            throw new RuntimeException(e);
        }
    }

    private static <T, E, V> T get(Type type, Class<? super E> clazz, E instance, V key)
    {
        try
        {
            switch (type)
            {
                case FIELD_BY_INDEX:
                    return (T) getPrivateValueInternalByIndex(clazz, instance, (Integer) key);

                case FIELD_BY_NAME:
                    return (T) getPrivateValueInternalByName(clazz, instance, (String) key);

                case METHOD_BY_INDEX:
                    return (T) getPrivateMethodInternalByIndex(clazz, (Integer) key);

                case METHOD_BY_NAME:
                    return (T) getPrivateMethodInternalByName(clazz, (String) key);
            }

            throw new InternalError("unexpected target value. : " + key);
        }
        catch (Exception e)
        {
            Log.severe(e, "reflection failed. (class=%s, key=%s)", clazz.getName(), key);

            if (e instanceof RuntimeException)
            {
                throw (RuntimeException) e;
            }

            throw new RuntimeException(e);
        }
    }

    private static <E> Method getPrivateMethodInternalByIndex(Class<? super E> clazz, int index)
    {
        Method m = clazz.getDeclaredMethods()[index];
        m.setAccessible(true);
        return m;
    }

    private static <E> Method getPrivateMethodInternalByName(Class<? super E> clazz, String name)
            throws NoSuchMethodException
    {
        Method m = clazz.getDeclaredMethod(name);
        m.setAccessible(true);
        return m;
    }

    private static <T, E> T getPrivateValueInternalByIndex(Class<? super E> clazz, E instance, int index)
            throws IllegalAccessException
    {
        Field f = clazz.getDeclaredFields()[index];
        f.setAccessible(true);
        return (T) f.get(instance);
    }

    private static <T, E> T getPrivateValueInternalByName(Class<? super E> clazz, E instance, String name)
            throws IllegalAccessException, NoSuchFieldException
    {
        Field f = clazz.getDeclaredField(name);
        f.setAccessible(true);
        return (T) f.get(instance);
    }
}
