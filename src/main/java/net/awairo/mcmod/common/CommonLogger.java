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

import static com.google.common.base.Preconditions.*;

import java.util.logging.Level;

import javax.annotation.Nonnull;

import net.awairo.mcmod.common.log.Logger;

/**
 * AwAModCommons共通処理用ロガー.
 * 
 * @author alalwww
 */
public final class CommonLogger extends Logger
{
    private static final CommonLogger INSTANCE = new CommonLogger();

    /**
     * Constructor.
     */
    private CommonLogger()
    {
        super(Env.INSTANCE);

        if (Env.develop())
            setLevel(Level.FINER);
    }

    /**
     * 共通クラス用ロガー取得.
     * 
     * @return ロガー
     */
    @Nonnull
    public static Logger getLogger()
    {
        return INSTANCE;
    }

    @Override
    public void debug(String format, Object... args)
    {
        log(Level.FINER, checkNotNull(format, "Argument 'format' must not be null."), args);
    }
}
