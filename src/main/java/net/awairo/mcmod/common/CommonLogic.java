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

import javax.annotation.Nonnull;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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

    public static void handlePreInitializeEvent(IAwAMod mod, FMLPreInitializationEvent event)
    {
        // none
    }

    public static void handleViolationEvent(IAwAMod mod, FMLFingerprintViolationEvent event)
    {
        if (event.isDirectory && Env.develop())
            return;

        throw new VerifyError(String.format(
                "This MOD file has been corrupted or tampered! Re-download the latest mod, please. (%s)",
                event.source.getName()));
    }

    @Nonnull
    public static String getModId(@Nonnull Object modInstance)
    {
        final Mod mod = modInstance.getClass().getAnnotation(Mod.class);

        if (isNull(mod) || Strings.isNullOrEmpty(mod.modid()))
            throw new IllegalArgumentException();

        return mod.modid();
    }

    public static boolean isNull(Object object)
    {
        return object == null;
    }

    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    public static boolean equal(Object a, Object b)
    {
        return Objects.equal(a, b);
    }
}
