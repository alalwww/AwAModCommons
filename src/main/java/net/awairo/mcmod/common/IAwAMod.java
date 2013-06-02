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

/**
 * AwAModインターフェイス.
 * 
 * @author alalwww
 */
public interface IAwAMod
{
    /** certificateFingerprint. */
    public static final String FINGERPRINT = "26e70d58815b73cd7c6c865fe091672f79070a35";

    /**
     * modの環境設定情報を取得します.
     * 
     * @return modの環境設定情報
     */
    Env getEnv();
}
