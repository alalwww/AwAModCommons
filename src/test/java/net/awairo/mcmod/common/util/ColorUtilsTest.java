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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

/**
 * ColorUtilsTest.
 * 
 * @author alalwww
 */
public class ColorUtilsTest
{
    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#toIntColor(byte)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testToIntColor()
    {
        assertThat(ColorUtils.toIntColor((byte) -1), is(255));
        assertThat(ColorUtils.toIntColor((byte) 0x00), is(0));
        assertThat(ColorUtils.toIntColor((byte) 0xFF), is(255));
        assertThat(ColorUtils.toIntColor((byte) 0x100), is(0));
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#toByteColor(int)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testToByteColor()
    {
        assertThat(ColorUtils.toByteColor(-1), is((byte) 255));
        assertThat(ColorUtils.toByteColor(0), is((byte) 0));
        assertThat(ColorUtils.toByteColor(1), is((byte) 1));
        assertThat(ColorUtils.toByteColor(255), is((byte) 255));
        assertThat(ColorUtils.toByteColor(256), is((byte) 0));
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#tryParseColor(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testTryParseColor()
    {
        assertThat(ColorUtils.tryParseColor("000000"), is(new Color(0, 0, 0)));
        assertThat(ColorUtils.tryParseColor("#000000"), is(new Color(0, 0, 0)));
        assertThat(ColorUtils.tryParseColor("#000000"), is(new Color(0, 0, 0)));
        assertThat(ColorUtils.tryParseColor("#FFAAcc"), is(new Color(0xFF, 0xAA, 0xCC)));
        assertThat(ColorUtils.tryParseColor("#FFAAcc6B"), is(new Color(0xAA, 0xCC, 0x6b, 0xFF)));
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorRGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testParseColorRGB()
    {
        assertThat(ColorUtils.parseColorRGB("aABbcC"), is(new Color(0xAA, 0xBB, 0xCC)));
        assertThat(ColorUtils.parseColorRGB("#009Ffa"), is(new Color(0, 0x9F, 0xFA)));
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorRGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseColorRGB_IAE1() throws Exception
    {
        ColorUtils.parseColorRGB("00000");
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorRGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseColorRGB_IAE2() throws Exception
    {
        ColorUtils.parseColorRGB("#00000");
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorRGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseColorRGB_IAE3() throws Exception
    {
        ColorUtils.parseColorRGB("0000000");
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorRGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseColorRGB_IAE4() throws Exception
    {
        ColorUtils.parseColorRGB("#0000000");
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorARGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testParseColorARGB()
    {
        assertThat(ColorUtils.parseColorARGB("00aABbcC"), is(new Color(0xAA, 0xBB, 0xCC, 0)));
        assertThat(ColorUtils.parseColorARGB("#fa009Ffa"), is(new Color(0, 0x9F, 0xFA, 0xFA)));
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorARGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseColorARGB_IAE1() throws Exception
    {
        ColorUtils.parseColorARGB("0000000");
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorARGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseColorARGB_IAE2() throws Exception
    {
        ColorUtils.parseColorARGB("#0000000");
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorARGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseColorARGB_IAE3() throws Exception
    {
        ColorUtils.parseColorARGB("000000000");
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#parseColorARGB(java.lang.String)}
     * のためのテスト・メソッド。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseColorARGB_IAE4() throws Exception
    {
        ColorUtils.parseColorARGB("#000000000");
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#toString(java.awt.Color)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testToStringColor()
    {
        assertThat(ColorUtils.toString(new Color(0, 255, 0)), is("00FF00"));
        assertThat(ColorUtils.toString(new Color(0, 255, 0, 127)), is("00FF00"));
    }

    /**
     * {@link net.awairo.mcmod.common.util.ColorUtils#toString(java.awt.Color, boolean)}
     * のためのテスト・メソッド。
     */
    @Test
    public void testToStringColorBoolean()
    {
        assertThat(ColorUtils.toString(new Color(0, 255, 0), true), is("FF00FF00"));
        assertThat(ColorUtils.toString(new Color(0, 255, 0, 127), false), is("00FF00"));
        assertThat(ColorUtils.toString(new Color(0, 255, 0, 127), true), is("7F00FF00"));
    }

}
