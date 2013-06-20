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

package net.awairo.mcmod.common.handler;

import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.awairo.mcmod.common.CommonLogger;
import net.awairo.mcmod.common.Env;
import net.awairo.mcmod.common.IAwAMod;
import net.awairo.mcmod.common.log.Logger;

/**
 * FMLイベントの汎用ロジック.
 * 
 * @author alalwww
 */
public final class FMLEventHandlerLogic
{
    private static final Logger LOG = CommonLogger.getLogger();

    private FMLEventHandlerLogic()
    {
    }

    public static void handlePreInitializeEvent(IAwAMod mod, FMLPreInitializationEvent event)
    {
        // none
    }

    public static void handleInitializeEvent(IAwAMod mod, FMLInitializationEvent event)
    {
        // none
    }

    public static void handlePostInitializeEvent(IAwAMod mod, FMLPostInitializationEvent event)
    {
        // none
    }

    public static void handleViolationEvent(IAwAMod mod, FMLFingerprintViolationEvent event)
    {
        if (event.isDirectory && Env.develop())
            return;

        final String format = "This MOD file has been corrupted or tampered! Re-download the latest mod, please. (%s)";
        LOG.severe(format, event.source.getName());

        final String formatJa = "このMODファイルは壊れているか改ざんされています！ 最新版をダウンロードしなおしてください。 (%s)";
        LOG.severe(formatJa, event.source.getName());

        throw new VerifyError(String.format(format, event.source.getName()));
    }
}
