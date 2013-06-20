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

package net.awairo.mcmod.common.log;

import java.io.PrintStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * デバッグログのコンソール出力ハンドラ.
 * 
 * @author alalwww
 */
final class DebugConsoleHandler extends StreamHandler
{
    protected DebugConsoleHandler(PrintStream err)
    {
        super(err, new LogFormatter());
    }

    @Override
    public synchronized void publish(LogRecord record)
    {
        super.publish(record);
        flush();
    }

    @Override
    public synchronized void close()
    {
        flush();
    }
}
