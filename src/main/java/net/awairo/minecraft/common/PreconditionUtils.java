/*
 * (c) 2013 alalwww
 * https://github.com/alalwww
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 *
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.minecraft.common;

import javax.annotation.Nonnull;

/**
 * 前提条件ユーティリティ.
 * 
 * @author alalwww
 */
public class PreconditionUtils
{
    private PreconditionUtils()
    {
    }

    /**
     * 引数がnullでない事を検証します.
     * 
     * @param argument
     *            引数
     * @return nullではない引数
     * @throws IllegalArgumentException
     *             null だった場合
     */
    @Nonnull
    public static <T> T checkArgNotNull(T argument) throws IllegalArgumentException
    {
        if (argument != null)
            return argument;

        throw new IllegalArgumentException("null");
    }

    /**
     * nullではない値にします.
     * 
     * @param reference
     *            nullかもしれない値
     * @return null ではない値
     * @throws NullPointerException
     *             nullだった場合
     */
    @Nonnull
    public static <T> T toNonnull(T reference) throws NullPointerException
    {
        if (reference != null)
            return reference;

        throw new NullPointerException();
    }
}
