/*
 * AwA Minecraft's mod commons.
 *
 * (c) 2013 alalwww
 * https://github.com/alalwww
 *
 * This library is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 *
 * このライブラリは、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.common.util;

import static com.google.common.base.Preconditions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Throwables;

import net.awairo.mcmod.common.CommonLogger;
import net.awairo.mcmod.common.Env;
import net.awairo.mcmod.common.log.Logger;

/**
 * reflection helper.
 * 
 * @author alalwww
 */
public class ReflectionHelper
{
    private static final Logger LOG = CommonLogger.getLogger();

    private static enum Type
    {
        FIELD_BY_INDEX, FIELD_BY_NAME, METHOD_BY_INDEX, METHOD_BY_NAME
    }

    /**
     * このクラスをロードしたクラスローダーからクラスを探し、クラスが存在する場合trueを返します.
     * 
     * <p>
     * 他のModが導入済みかのチェック用。
     * </p>
     * 
     * @param className
     *            FQCN
     * @return true の場合クラスが存在する
     */
    public static boolean findClass(@Nonnull String className)
    {
        try
        {
            Class.forName(className, false, ReflectionHelper.class.getClassLoader());
            // Class.forName(className);
            return true;
        }
        catch (final ClassNotFoundException e)
        {
            final String pref = "net.minecraft.src.";
            if (pref.length() < className.length() && className.startsWith(pref))
                return findClass(className.substring(pref.length(), className.length()));

            return false;
        }
    }

    /**
     * フィールドの値を取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象クラスインスタンス または static フィールドの場合は null
     * @param fieldIndex
     *            フィールドインデックス
     * @return private value
     */
    @Nullable
    public static <T, E> T getFieldValue(Class<? extends E> clazz, E instance, @Nonnegative int fieldIndex)
    {
        return getValue(getField(clazz, Integer.valueOf(fieldIndex)), instance);
    }

    /**
     * フィールドの値を取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象クラスインスタンス または static フィールドの場合は null
     * @param fieldName
     *            フィールド名
     * @return private value or null
     */
    @Nullable
    public static <T, E> T getFieldValue(Class<? extends E> clazz, E instance, @Nonnull String fieldName)
    {
        return getValue(getField(clazz, fieldName), instance);
    }

    /**
     * フィールドの値を取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param instance
     *            対象クラスインスタンス または static フィールドの場合は null
     * @param fieldName
     *            フィールド名
     * @param srgFieldName
     *            実行時難読化解除後のフィールド名
     * @return フィールド値またはnull
     */
    @Nullable
    public static <T, E> T getFieldValue(Class<? extends E> clazz, @Nullable E instance,
            @Nonnull String fieldName, @Nonnull String srgFieldName)
    {
        return getFieldValue(clazz, instance, convertName(fieldName, srgFieldName));
    }

    /**
     * フィールドを取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param fieldIndex
     *            フィールドのインデックス
     * @return フィールド
     */
    @Nonnull
    public static <E> Field getField(Class<? extends E> clazz, @Nonnegative int fieldIndex)
    {
        checkArgument(fieldIndex >= 0);
        return get(Type.FIELD_BY_INDEX, clazz, Integer.valueOf(fieldIndex));
    }

    /**
     * フィールドを取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param fieldName
     *            フィールド名
     * @return フィールド
     */
    @Nonnull
    public static <E> Field getField(Class<? extends E> clazz, @Nonnull String fieldName)
    {
        return get(Type.FIELD_BY_NAME, clazz, fieldName);
    }

    /**
     * フィールドを取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param fieldName
     *            フィールド名
     * @param srgFieldName
     *            実行時難読化解除後のフィールド名
     * @return フィールド
     */
    @Nonnull
    public static <E> Field getField(Class<? extends E> clazz, @Nonnull String fieldName,
            @Nonnull String srgFieldName)
    {
        return getField(clazz, convertName(fieldName, srgFieldName));
    }

    /**
     * メソッドを取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param methodIndex
     *            メソッドのインデックス
     * @return method
     */
    public static Method getMethod(Class<?> clazz, @Nonnegative int methodIndex)
    {
        checkArgument(methodIndex >= 0);
        return get(Type.METHOD_BY_INDEX, clazz, Integer.valueOf(methodIndex));
    }

    /**
     * メソッドを取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param methodName
     *            メソッド名
     * @return method
     */
    public static Method getMethod(Class<?> clazz, @Nonnull String methodName)
    {
        return get(Type.METHOD_BY_NAME, clazz, methodName);
    }

    /**
     * メソッドを取得します.
     * 
     * @param clazz
     *            対象クラス
     * @param methodName
     *            メソッド名
     * @param srgMethodName
     *            実行時難読化解除後のメソッド名
     * @return method
     * 
     * @throws RuntimeException
     *             it's so bug ridden!
     */
    public static Method getMethod(Class<?> clazz, @Nonnull String methodName, @Nonnull String srgMethodName)
    {
        return getMethod(clazz, convertName(methodName, srgMethodName));
    }

    /**
     * フィールドから値を取得します.
     * 
     * @param field
     *            フィールド
     * @param instance
     *            フィールドを持つクラスインスタンス または null
     * @return フィールドの値
     */
    public static <V> V getValue(Field field, @Nullable Object instance)
    {
        checkNotNull(field, "Argument '' must not be null.");
        try
        {
            @SuppressWarnings("unchecked")
            final V value = (V) field.get(instance);
            return value;
        }
        catch (final Exception e)
        {
            LOG.severe(e, "reflection failed. (field=%s, instance=%s)", field, instance);
            throw Throwables.propagate(e);
        }
    }

    /**
     * メソッドを実行します.
     * 
     * @param method
     *            method
     * @param instance
     *            class instance or null
     * @param args
     *            arguments
     * @return result
     */
    @Nullable
    public static <R> R invoke(Method method, @Nullable Object instance, Object... args)
    {
        checkNotNull(method, "Argument 'method' must not be null.");
        try
        {
            @SuppressWarnings("unchecked")
            final R retValue = (R) method.invoke(instance, args);
            return retValue;
        }
        catch (final Exception e)
        {
            final String f = "reflection failed. (class=%s, method=%s, instance=%s, args=%s)";
            final Object argsValue = args != null ? Arrays.toString(args) : args;
            LOG.severe(e, f, method.getDeclaringClass().getName(), method.getName(), instance, argsValue);

            throw Throwables.propagate(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private static <T, V> T get(Type type, Class<?> clazz, V key)
    {
        checkNotNull(clazz, "Argument 'clazz' must not be null.");
        checkNotNull(key, "Argument 'key' must not be null.");

        try
        {
            switch (type)
            {
                case FIELD_BY_INDEX:
                    return (T) getPrivateValueInternalByIndex(clazz, (Integer) key);

                case FIELD_BY_NAME:
                    return (T) getPrivateValueInternalByName(clazz, (String) key);

                case METHOD_BY_INDEX:
                    return (T) getPrivateMethodInternalByIndex(clazz, (Integer) key);

                case METHOD_BY_NAME:
                    return (T) getPrivateMethodInternalByName(clazz, (String) key);
            }

            throw new InternalError("unexpected target value. : " + key);
        }
        catch (final Exception e)
        {
            LOG.severe(e, "reflection failed. (class=%s, key=%s)", clazz.getName(), key);
            throw Throwables.propagate(e);
        }
    }

    /**
     * インデックスからメソッド取得します.
     */
    @Nonnull
    private static <E> Method getPrivateMethodInternalByIndex(Class<?> clazz, int index)
            throws IndexOutOfBoundsException
    {
        final Method m = clazz.getDeclaredMethods()[index];
        m.setAccessible(true);
        return m;
    }

    /**
     * 名前からメソッド取得します.
     */
    @Nonnull
    private static <E> Method getPrivateMethodInternalByName(Class<?> clazz, String name)
            throws NoSuchMethodException
    {
        final Method m = clazz.getDeclaredMethod(name);
        m.setAccessible(true);
        return m;
    }

    /**
     * インデックスからフィールド取得します.
     */
    @Nonnull
    private static <E> Field getPrivateValueInternalByIndex(Class<?> clazz, int index)
            throws IllegalAccessException
    {
        final Field f = clazz.getDeclaredFields()[index];
        f.setAccessible(true);
        return f;
    }

    /**
     * 名前からフィールドを取得します.
     */
    @Nonnull
    private static <E> Field getPrivateValueInternalByName(Class<?> clazz, String name)
            throws IllegalAccessException, NoSuchFieldException
    {
        final Field f = clazz.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }

    /**
     * 環境に合わせた名前に変換.
     * 
     * @param devName
     *            開発環境用の名前
     * @param srgName
     *            実行環境用の名前
     * @return 現在の環境用の名前
     */
    @Nonnull
    private static String convertName(String devName, String srgName)
    {
        checkNotNull(devName, "Argument 'devName' must not be null.");
        checkNotNull(srgName, "Argument 'srgName' must not be null.");
        return Env.develop() ? devName : srgName;
    }
}
