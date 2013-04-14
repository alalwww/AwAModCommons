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

import static net.awairo.minecraft.common.PreconditionUtils.*;

import javax.annotation.Nonnull;

/**
 * 環境の情報.
 * 
 * @author alalwww
 */
public class Env
{
    @Nonnull
    private static String rootPackageName = "net.awairo";
    private static boolean debug = isEnable(rootPackageName + ".develop");
    private static boolean develop = isEnable(rootPackageName + ".debug");

    @Nonnull
    public static String rootPackageName()
    {
        return toNonnull(rootPackageName);
    }

    /** net.awairo global debug mode. */
    public static boolean debug()
    {
        return debug;
    }

    /** development workspace. */
    public static boolean develop()
    {
        return develop;
    }

    private Env()
    {
    }

    /**
     * システムプロパティの値が true に設定されていたら true を返す.
     * 
     * @param key
     *            プロパティキー
     * @return プロパティの値が "true" の場合 true
     */
    public static boolean isEnable(@Nonnull String key)
    {
        return getProperty(key).toLowerCase().equals(Boolean.TRUE.toString());
    }

    /**
     * システムプロパティの値を取得.
     * 
     * @param key
     *            プロパティキー
     * @return 紐づく値か空文字列
     */
    @Nonnull
    public static String getProperty(@Nonnull String key)
    {
        String value = System.getProperty(checkArgNotNull(key));
        return value != null ? value : "";
    }
}
