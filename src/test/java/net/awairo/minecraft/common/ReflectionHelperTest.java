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

package net.awairo.minecraft.common;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * xxx.
 * 
 * @author alalwww
 * 
 */
public class ReflectionHelperTest
{

    /**
     * {@link net.awairo.minecraft.common.ReflectionHelper#findClass(java.lang.String)} のためのテスト・メソッド。
     */
    @Test
    public void testFindClass()
    {
        assertThat(ReflectionHelper.findClass("java.lang.String"), is(true));
        assertThat(ReflectionHelper.findClass("java.lang.Strin"), is(false));
        assertThat(ReflectionHelper.findClass("FindClassTestTarget"), is(true));
        assertThat(ReflectionHelper.findClass("net.minecraft.src.FindClassTestTarget"), is(true));
    }

    /**
     * {@link net.awairo.minecraft.common.ReflectionHelper#getFieldValue(java.lang.Class, java.lang.Object, int)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testGetFieldValueClassOfQsuperEEInt()
    {
        TestClass test = new TestClass();

        String value = ReflectionHelper.getFieldValue(TestClass.class, test, 0);
        assertThat(value, is(test.field1));
        value = ReflectionHelper.getFieldValue(TestClass.class, test, 1);
        assertThat(value, is(test.field2));
        value = ReflectionHelper.getFieldValue(TestClass.class, test, 2);
        assertThat(value, is(test.field3));
    }

    /**
     * {@link net.awairo.minecraft.common.ReflectionHelper#getFieldValue(java.lang.Class, java.lang.Object, int)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldValueClassOfQsuperEEIntIAE()
    {
        ReflectionHelper.getFieldValue(TestClass.class, this, -1);
    }

    /**
     * {@link net.awairo.minecraft.common.ReflectionHelper#getFieldValue(java.lang.Class, java.lang.Object, int)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetFieldValueClassOfQsuperEEIntIOoBE()
    {
        ReflectionHelper.getFieldValue(TestClass.class, this, 3);
    }

    /**
     * {@link net.awairo.minecraft.common.ReflectionHelper#getMethod(java.lang.Class, java.lang.String)} のためのテスト・メソッド。
     */
    @Test
    public void testGetMethod()
    {
        Method method = ReflectionHelper.getMethod(TestClass.class, "testTargetMethodName");
        assertThat(method.getName(), is("testTargetMethodName"));
    }

    /**
     * {@link net.awairo.minecraft.common.ReflectionHelper#invoke(java.lang.reflect.Method, java.lang.Object, java.lang.Object...)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testInvoke()
    {
        TestClass test = new TestClass();
        Method method = ReflectionHelper.getMethod(TestClass.class, "testTargetMethodName");
        assertThat((String) ReflectionHelper.invoke(method, test), is(test.testTargetMethodName()));
    }

    private static class TestClass
    {

        private String field1 = "field1";
        private String field2 = "field2";
        private String field3 = "field3";

        private String testTargetMethodName()
        {
            return "test method value";
        }
    }
}
