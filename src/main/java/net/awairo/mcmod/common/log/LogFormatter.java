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

package net.awairo.mcmod.common.log;

import static net.awairo.mcmod.common.CommonLogic.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;

/**
 * ログフォーマッター.
 * 
 * @author alalwww
 */
@NotThreadSafe
final class LogFormatter extends Formatter
{
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");

    /**
     * Constructor.
     */
    protected LogFormatter()
    {
    }

    @Override
    public String format(LogRecord record)
    {
        final StringBuilder msg = new StringBuilder();

        msg.append(dateFormat.format(record.getMillis()));
        msg.append(getLevelString(record.getLevel()));

        appendLoggerName(msg, record.getLoggerName());

        msg.append(record.getMessage());
        msg.append(LINE_SEPARATOR);

        appendError(msg, record.getThrown());

        return msg.toString();
    }

    private static String getLevelString(Level lvl)
    {
        if (lvl == Level.FINEST)
            return getLevelString("TRACE");

        if (lvl == Level.FINER)
            return getLevelString("C DEBUG");

        if (lvl == Level.FINE)
            return getLevelString("DEBUG");

        return getLevelString(lvl.getName());
    }

    @VisibleForTesting
    static String getLevelString(String levelName)
    {
        return "[" + Strings.padEnd(levelName.toUpperCase(), 7, ' ') + "]";
    }

    private static void appendLoggerName(StringBuilder msg, String loggerName)
    {
        if (isNull(loggerName) || loggerName.length() <= 0)
            return;

        final int index = loggerName.lastIndexOf(".");
        if (index >= 0)
            loggerName = loggerName.substring(index + 1);

        msg.append("(").append(loggerName).append(") ");
    }

    private static void appendError(StringBuilder msg, Throwable throwable)
    {
        if (isNull(throwable))
            return;

        final StringWriter output = new StringWriter();
        throwable.printStackTrace(new PrintWriter(output));
        msg.append(output.toString());
    }
}
