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

import static net.awairo.mcmod.common.PreconditionUtils.*;

import javax.annotation.Nonnull;

/**
 * 環境の情報.
 * 
 * @author alalwww
 */
public class Env
{
    @Nonnull
    private static final Env INSTANCE;
    private static final boolean DEVELOP;

    static
    {
        INSTANCE = new Env("net.awairo");
        DEVELOP = isEnable(INSTANCE.packageName + ".develop");
    }

    protected final String packageName;
    private boolean debug;
    private boolean trace;

    /**
     * Constructor.
     * 
     * @param packageName
     *            ルートパッケージ名
     */
    public Env(@Nonnull String packageName)
    {
        this.packageName = checkArgNotNull(packageName);
        debug = isEnable(packageName + ".debug");
        trace = isEnable(packageName + ".trace");
    }

    /**
     * ルートパッケージ名取得.
     * 
     * @return ルートパッケージ名
     */
    @Nonnull
    public static String rootPackageName()
    {
        return INSTANCE.packageName;
    }

    /**
     * 開発環境判定用フラグ値取得.
     * 
     * @return 開発環境判定フラグ値
     */
    public static boolean develop()
    {
        return DEVELOP;
    }

    /**
     * 共通デバッグフラグ値取得.
     * 
     * @return 共通デバッグフラグ値
     */
    public static boolean debug()
    {
        return INSTANCE.debug;
    }

    /**
     * 共通トレースフラグ値取得.
     * 
     * @return 開発環境判定フラグ値
     */
    public static boolean trace()
    {
        return INSTANCE.trace;
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
        return Boolean.valueOf(getProperty(key));
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
        final String value = System.getProperty(checkArgNotNull(key));
        return value != null ? value : "";
    }

    /**
     * MODのパッケージ名を取得.
     * 
     * @return パッケージ名
     */
    @Nonnull
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * MODのデバッグフラグ値取得.
     * 
     * @return デバッグフラグ
     */
    public boolean isDebugEnabled()
    {
        return debug;
    }

    /**
     * MODのトレースフラグ値取得.
     * 
     * @return トレースフラグ
     */
    public boolean isTraceEnabled()
    {
        return trace;
    }
}
