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
import static net.awairo.mcmod.common.CommonLogic.*;

import java.awt.Color;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Strings;

import net.awairo.mcmod.common.CommonLogger;
import net.awairo.mcmod.common.log.Logger;

/**
 * RGB形式またはRGBA形式の文字列を Color にパースします.
 * 
 * R、G、B、A は それぞれ 1または2桁の 0～F の16進数文字列です。
 * 
 * RGB形式の例) #RGB, 0xRGB, #RRGGBB, 0xRRGGBB, RGB, RRGGBB
 * 
 * RGBA形式の例) #ARGB, 0xARGB, #AARRGGBB, 0xAARRGGBB, ARGB, AARRGGBB
 * 
 * @author alalwww
 */
public class ColorUtils
{
    private static final Logger LOG = CommonLogger.getLogger();

    private static final Pattern COLOR_PATTERN;
    private static final Pattern HEX_PATTERN;

    private static final int LENGTH_OF_RGB = 3;
    private static final int LENGTH_OF_ARGB = 4;
    private static final int LENGTH_OF_RRGGBB = 6;
    private static final int LENGTH_OF_AARRGGBB = 8;

    private static final int HEX = 16;

    static
    {
        COLOR_PATTERN = Pattern.compile("^#?(([0-9a-fA-F]){3,4}|([0-9a-fA-F]{2}){3,4})$");
        HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,2}$");
    }

    /**
     * merge int value of primary colors.
     * 
     * @param r
     *            red
     * @param g
     *            green
     * @param b
     *            blue
     * @return 0xFFRRGGBB
     */
    public static int toIntColor(int r, int g, int b)
    {
        return toIntColor(r, g, b, 0xFF);
    }

    /**
     * merge int value of primary colors and alpha.
     * 
     * @param r
     *            red
     * @param g
     *            green
     * @param b
     *            blue
     * @param a
     *            alpha
     * @return 0xAARRGGBB
     */
    public static int toIntColor(int r, int g, int b, int a)
    {
        int color = b;
        color += g << 8;
        color += r << 16;
        color += a << 24;
        return color;
    }

    /**
     * byte color to integer color.
     * 
     * @param b
     *            byte color
     * @return integer color
     */
    @Nonnull
    public static Integer toIntColor(byte b)
    {
        return Integer.valueOf(b & 0xFF);
    }

    /**
     * integer color to byte color.
     * 
     * @param i
     *            integer color
     * @return byte color
     */
    public static byte toByteColor(int i)
    {
        return (byte) ((i >>> 0) & 0xFF);
    }

    /**
     * to color.
     * 
     * @param colorString
     *            RGB or ARGB string
     * @return color or null
     */
    @Nullable
    public static Color tryParseColor(String colorString)
    {
        if (isNull(colorString) || !isColorString(colorString))
            return null;

        colorString = removeHeaderChar(colorString);
        return createNewColor(colorString, hasAlpha(colorString));
    }

    /**
     * to color.
     * 
     * @param rgb
     *            RGB string
     * @return color or null
     */
    @Nonnull
    public static Color parseColorRGB(@Nonnull String rgb)
    {
        checkArgument(isColorString(rgb));
        rgb = removeHeaderChar(checkNotNull(rgb, "Argument 'rgb' must not be null."));
        final int l = rgb.length();
        checkArgument(l == LENGTH_OF_RGB || l == LENGTH_OF_RRGGBB);

        return createNewColor(rgb, false);
    }

    /**
     * to color.
     * 
     * @param argb
     *            ARGB string
     * @return color or null
     */
    @Nullable
    public static Color parseColorARGB(@Nonnull String argb)
    {
        argb = removeHeaderChar(checkNotNull(argb, "Argument 'argb' must not be null."));
        final int l = argb.length();
        checkArgument(l == LENGTH_OF_ARGB || l == LENGTH_OF_AARRGGBB);

        return createNewColor(argb, true);
    }

    @Nonnull
    private static String removeHeaderChar(@Nonnull String s)
    {
        if (s.startsWith("#"))
            return s.substring("#".length(), s.length());

        if (s.startsWith("0x"))
            return s.substring("0x".length(), s.length());

        return s;
    }

    @Nonnull
    private static Color createNewColor(@Nonnull String colorString, boolean hasalpha)
    {
        final int[] colorValueStack = parseColorString(colorString, hasalpha ? LENGTH_OF_ARGB : LENGTH_OF_RGB);
        final Color ret = newColor(colorValueStack, hasalpha);
        LOG.debug("color parsed. (%s)", ret);
        return ret;
    }

    /**
     * RGB Color to String.
     * 
     * @param color
     * @return color string
     */
    @Nonnull
    public static String toString(Color color)
    {
        return toString(color, false);
    }

    /**
     * Color to String.
     * 
     * @param color
     *            color
     * @param hasalpha
     *            true if the RGBA color
     * @return color string AARRGGBB or RRGGBB
     */
    @Nonnull
    public static String toString(Color color, boolean hasalpha)
    {
        final StringBuilder sb = new StringBuilder(hasalpha ? LENGTH_OF_AARRGGBB : LENGTH_OF_RRGGBB);

        if (hasalpha)
            sb.append(hexToString(color.getAlpha()));

        sb.append(hexToString(color.getRed()));
        sb.append(hexToString(color.getGreen()));
        sb.append(hexToString(color.getBlue()));

        return sb.toString().toUpperCase();
    }

    private static boolean isColorString(String colorString)
    {
        if (isNull(colorString))
            return false;

        return COLOR_PATTERN.matcher(colorString).matches();
    }

    private static String hexToString(int hexInt)
    {
        return Strings.padStart(Integer.toHexString(hexInt), 2, '0');
    }

    private static boolean hasAlpha(String colorString)
    {
        final int length = colorString.length();
        return (length == LENGTH_OF_ARGB || length == LENGTH_OF_AARRGGBB);
    }

    private static int[] parseColorString(String colorString, int loopCount)
    {
        final int length = colorString.length(); // 3, 4, 6 or 8
        final int delta = computeDelta(length, loopCount);

        final int[] colorValueStack = new int[loopCount];

        int cursor = 0;

        for (int i = 0; i < loopCount; i++)
        {
            final int endIndex = cursor + delta;
            String s = colorString.substring(cursor, endIndex);
            cursor = endIndex;

            if (s.length() == 1)
                s = s.concat(s); // avoid using StringBuilder

            if (HEX_PATTERN.matcher(s).matches())
            {
                colorValueStack[i] = Integer.parseInt(s, HEX);
                continue;
            }

            LOG.warning("illegal color value. (%s) replace to FF", s);
            colorValueStack[i] = 0xFF;
        }

        return colorValueStack;
    }

    private static int computeDelta(int length, int loopCount)
    {
        if (length == loopCount)
            return 1;

        if (length == loopCount + loopCount)
            return 2;

        throw new InternalError("unreachable code.");
    }

    private static Color newColor(int[] colorValues, boolean hasalpha)
    {
        if (hasalpha)
            return new Color(colorValues[1], colorValues[2], colorValues[3], colorValues[0]);

        return new Color(colorValues[0], colorValues[1], colorValues[2]);
    }
}
