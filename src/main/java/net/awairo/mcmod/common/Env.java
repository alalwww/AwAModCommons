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
 * このModに紐づく環境の情報.
 * 
 * @author alalwww
 */
public class Env
{
    @Nonnull
    static final Env INSTANCE;
    private static final boolean DEVELOP;

    static
    {
        // 共通設定用の名称で、common を含めると意味がダブるので common は省略している
        INSTANCE = new Env("net.awairo", "awairo.common");
        DEVELOP = isEnabled(INSTANCE.packageName + ".develop");
    }

    protected final String packageName;
    protected final String modid;
    protected boolean debug;
    protected boolean trace;

    /**
     * Constructor.
     * 
     * @param modInstance
     *            mod instance
     */
    public Env(@Nonnull IAwAMod modInstance)
    {
        this(modInstance.getClass().getPackage().getName(), CommonLogic.getModId(modInstance));
    }

    /**
     * Constructor.
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @param modid
     *            mod id
     */
    private Env(@Nonnull String rootPackageName, @Nonnull String modid)
    {
        this.packageName = checkArgNotNull(rootPackageName);
        this.modid = checkArgNotNull(modid);
        debug = isEnabled(rootPackageName + ".debug");
        trace = isEnabled(rootPackageName + ".trace");
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
     * システムプロパティの値が "true" に設定されていたら true を返す.
     * 
     * @param key
     *            プロパティキー
     * @return プロパティの値が "true" の場合 true
     */
    public static boolean isEnabled(@Nonnull String key)
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
     * MODのルートパッケージ名を取得.
     * 
     * @return ルートパッケージ名
     */
    public String getPackageName()
    {
        return packageName;
    }

    /**
     * modid を取得
     * 
     * @return modid
     */
    @Nonnull
    public String getModId()
    {
        return modid;
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
     * MODのデバッグフラグを設定.
     * 
     * @param debug
     *            the debug to set
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
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

    /**
     * MODのトレースフラグを設定.
     * 
     * @param trace
     *            the trace to set
     */
    public void setTrace(boolean trace)
    {
        this.trace = trace;
    }

    /**
     * このMod用のシステムプロパティの値を取得します.
     * 
     * @param property
     * @return 値(String)
     */
    public String getModProperty(@Nonnull String property)
    {
        return getProperty(getPropertyKey(property));
    }

    /**
     * このMod用のシステムプロパティの値が "true" に設定されていたら true を返す.
     * 
     * @param property
     *            プロパティ名
     * @return 値(boolean)
     */
    public boolean isModPropertyEnabled(@Nonnull String property)
    {
        return isEnabled(getPropertyKey(property));
    }

    /**
     * このMod用のシステムプロパティのキーを取得します.
     * 
     * @param property
     *            プロパティ
     * @return キー
     */
    public String getPropertyKey(@Nonnull String property)
    {
        return packageName + "." + checkArgNotNull(property);
    }
}
