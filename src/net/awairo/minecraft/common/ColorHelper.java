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

import java.awt.Color;
import java.util.regex.Pattern;

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
 * @version 1.0.0
 */
public class ColorHelper
{
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
    public static Integer toIntColor(byte b)
    {
        return Integer.valueOf((int) b & 0xFF);
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
     *            RGB or RGBA string
     * @return color or null
     */
    public static Color parseColor(String colorString)
    {
        if (colorString == null)
        {
            return null;
        }

        colorString = removeHeaderChar(colorString);
        int length = colorString.length();
        return parseColorInternal(colorString, (length == 4 || length == 8));
    }

    /**
     * to color.
     * 
     * @param rgbString
     *            RGB string
     * @return color or null
     */
    public static Color parseColorRGB(String rgbString)
    {
        return parseColorInternal(rgbString, false);
    }

    /**
     * to color.
     * 
     * @param rgbaString
     *            RGBA string
     * @return color or null
     */
    public static Color parseColorRGBA(String rgbaString)
    {
        return parseColorInternal(rgbaString, true);
    }

    private static String removeHeaderChar(String s)
    {
        if (s.startsWith("#"))
        {
            return s.substring(1, s.length());
        }

        if (s.startsWith("0x"))
        {
            return s.substring(2, s.length());
        }

        return s;
    }

    private static Color parseColorInternal(String colorString, boolean hasalpha)
    {
        if (colorString == null || !COLOR_PATTERN.matcher(colorString).matches())
        {
            return null;
        }

        colorString = removeHeaderChar(colorString);
        int loopCount = hasalpha ? 4 : 3;
        int length = colorString.length();
        int delta;

        if (length == loopCount)
        {
            delta = 1;
        }
        else if (length == loopCount * 2)
        {
            delta = 2;
        }
        else
        {
            return null;
        }

        int[] color = new int[loopCount];
        int cursor = 0;

        for (int i = 0; i < loopCount; i++)
        {
            int endIndex = cursor + delta;
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
                Log.warning("illegal color value. (%s) replace to FF", s);
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

        Log.finer("color parsed. (%s)", ret);
        return ret;
    }

    /**
     * RGB Color to String.
     * 
     * @param color
     * @return
     */
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
     * @return color string
     */
    public static String toString(Color color, boolean hasalpha)
    {
        StringBuilder sb = new StringBuilder(hasalpha ? 8 : 6);

        if (hasalpha)
        {
            sb.append(Integer.toString(color.getAlpha(), 16).toUpperCase());
        }

        sb.append(Integer.toString(color.getRed(), 16).toUpperCase());
        sb.append(Integer.toString(color.getGreen(), 16).toUpperCase());
        sb.append(Integer.toString(color.getBlue(), 16).toUpperCase());
        return sb.toString();
    }
}
