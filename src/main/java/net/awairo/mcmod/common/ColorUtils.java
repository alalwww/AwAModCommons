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

package net.awairo.mcmod.common;

import static com.google.common.base.Preconditions.*;
import static net.awairo.mcmod.common.PreconditionUtils.*;

import java.awt.Color;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Strings;

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

    static
    {
        COLOR_PATTERN = Pattern.compile("^#?(([0-9a-fA-F]){3,4}|([0-9a-fA-F]{2}){3,4})$");
        HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,2}$");
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
        return toNonnull(Integer.valueOf(b & 0xFF));
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
        if (colorString == null || !isColorString(colorString))
            return null;

        colorString = removeHeaderChar(colorString);
        final int length = colorString.length();
        return parseColorInternal(colorString, (length == 4 || length == 8));
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
        rgb = removeHeaderChar(checkArgNotNull(rgb));
        final int l = rgb.length();
        checkArgument(l == 3 || l == 6);

        return parseColorInternal(rgb, false);
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
        argb = removeHeaderChar(checkArgNotNull(argb));
        final int l = argb.length();
        checkArgument(l == 4 || l == 8);

        return parseColorInternal(argb, true);
    }

    @Nonnull
    private static String removeHeaderChar(@Nonnull String s)
    {
        if (s.startsWith("#"))
        {
            return toNonnull(s.substring(1, s.length()));
        }

        if (s.startsWith("0x"))
        {
            return toNonnull(s.substring(2, s.length()));
        }

        return s;
    }

    @Nonnull
    private static Color parseColorInternal(@Nonnull String colorString, boolean hasalpha)
    {
        final int loopCount = hasalpha ? 4 : 3;
        final int length = colorString.length(); // 3, 4, 6 or 8
        int delta;

        if (length == loopCount)
            delta = 1;
        else if (length == loopCount * 2)
            delta = 2;
        else
            throw new InternalError("unreachable code.");

        final int[] color = new int[loopCount];
        int cursor = 0;

        for (int i = 0; i < loopCount; i++)
        {
            final int endIndex = cursor + delta;
            String s = colorString.substring(cursor, endIndex);
            cursor = endIndex;

            if (s.length() == 1)
            {
                s = s + s;
            }

            if (HEX_PATTERN.matcher(s).matches())
            {
                color[i] = Integer.parseInt(s, 16);
            }
            else
            {
                LOG.warning("illegal color value. (%s) replace to FF", s);
                color[i] = 255;
            }
        }

        Color ret;

        if (hasalpha)
        {
            ret = new Color(color[1], color[2], color[3], color[0]);
        }
        else
        {
            ret = new Color(color[0], color[1], color[2]);
        }

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
        final StringBuilder sb = new StringBuilder(hasalpha ? 8 : 6);

        if (hasalpha)
            sb.append(hexToString(color.getAlpha()));

        sb.append(hexToString(color.getRed()));
        sb.append(hexToString(color.getGreen()));
        sb.append(hexToString(color.getBlue()));

        return toNonnull(sb.toString().toUpperCase());
    }

    private static boolean isColorString(String colorString)
    {
        if (colorString == null)
            return false;

        return COLOR_PATTERN.matcher(colorString).matches();
    }

    private static String hexToString(int hexInt)
    {
        return Strings.padStart(Integer.toHexString(hexInt), 2, '0');
    }
}
