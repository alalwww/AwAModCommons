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

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.FingerprintWarning;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * 共通処理のmodクラス.
 * 
 * <p>
 * 共通処理のjarファイルを、FMLにロードし検証させるためのModです。そのため、これ単体での追加要素などはなにもありません。
 * </p>
 * 
 * @author alalwww
 */
@Mod(modid = "awairo.commons", certificateFingerprint = "26e70d58815b73cd7c6c865fe091672f79070a35")
public class AwAModCommons implements IAwAMod
{
    private final Logger log = CommonLogger.getLogger();

    @PreInit
    protected void preInit(FMLPreInitializationEvent event)
    {
        log.debug("pre initialize");
    }

    @Init
    protected void init(FMLInitializationEvent event)
    {
        log.debug("initialize");
    }

    @PostInit
    protected void preInit(FMLPostInitializationEvent event)
    {
        log.debug("post initialize");
    }

    @FingerprintWarning
    protected void handleViolationEvent(FMLFingerprintViolationEvent event)
    {
        CommonLogic.handleViolationEvent(this, event);
    }

    @Override
    public Env getEnv()
    {
        return Env.INSTANCE;
    }
}
