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

import java.util.logging.Level;

import javax.annotation.Nonnull;

/**
 * 共通処理用ロガー.
 *
 * @author alalwww
 */
final class CommonLogger extends Logger
{
    private static final CommonLogger INSTANCE;

    static
    {
        INSTANCE = new CommonLogger();
    }

    /**
     * Constructor.
     */
    private CommonLogger()
    {
        super("net.awairo.common");
    }

    /**
     * 共通クラス用ロガー取得.
     * 
     * @return ロガー
     */
    @Nonnull
    static Logger getLogger()
    {
        return toNonnull(INSTANCE);
    }

    @Override
    public void debug(@Nonnull String format, Object... args)
    {
        log(Level.FINER, format, args);
    }
}
