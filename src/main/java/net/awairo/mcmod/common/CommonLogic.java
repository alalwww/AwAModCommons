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

package net.awairo.mcmod.common;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nonnull;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import cpw.mods.fml.common.Mod;

/**
 * 汎用ロジックユーティリティ.
 * 
 * @author alalwww
 */
public final class CommonLogic
{
    private CommonLogic()
    {
    }

    @Nonnull
    public static String getModId(@Nonnull Object modInstance)
    {
        final Mod mod = modInstance.getClass().getAnnotation(Mod.class);
        checkArgument(isNotNull(mod), "modInstance must be annotated class for the Mod annotation.");
        checkState(!Strings.isNullOrEmpty(mod.modid()), "the modid must not be null or empty strings.");

        return mod.modid();
    }

    public static boolean isNull(Object object)
    {
        return object == null;
    }

    public static boolean isNotNull(Object object)
    {
        return object != null;
    }

    public static boolean equal(Object a, Object b)
    {
        return Objects.equal(a, b);
    }
}
