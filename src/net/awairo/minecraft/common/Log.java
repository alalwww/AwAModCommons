/*
 * AwA Minecraft's mod commons.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 alalwww
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * 以下に定める条件に従い、本ソフトウェアおよび関連文書のファイル（以下「ソフトウェア」）の複製
 * を取得するすべての人に対し、ソフトウェアを無制限に扱うことを無償で許可します。これには、ソフ
 * トウェアの複製を使用、複写、変更、結合、掲載、頒布、サブライセンス、および/または販売する権
 * 利、およびソフトウェアを提供する相手に同じことを許可する権利も無制限に含まれます。
 *
 * 上記の著作権表示および本許諾表示を、ソフトウェアのすべての複製または重要な部分に記載するもの
 * とします。
 *
 * ソフトウェアは「現状のまま」で、明示であるか暗黙であるかを問わず、何らの保証もなく提供されま
 * す。ここでいう保証とは、商品性、特定の目的への適合性、および権利非侵害についての保証も含みま
 * すが、それに限定されるものではありません。 作者または著作権者は、契約行為、不法行為、または
 * それ以外であろうと、ソフトウェアに起因または関連し、あるいはソフトウェアの使用またはその他の
 * 扱いによって生じる一切の請求、損害、その他の義務について何らの責任も負わないものとします。
 */

package net.awairo.minecraft.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * my mod logger.
 * 
 * <ul>
 * <li>severe: error</li>
 * <li>warning: illegal settings</li>
 * <li>info: information</li>
 * <li>fine: debug</li>
 * <li>finer: common debug</li>
 * <li>finest: trace</li>
 * </ul>
 * 
 * @author alalwww
 * @version 1.0.0
 */
public class Log
{
    /** always logging. */
    public static boolean DEBUG = false;

    /** logger. */
    private static Logger LOG;

    private static Handler handler;

    public static void initialize(String modName, boolean debugMode)
    {
        DEBUG = debugMode;
        LOG = Logger.getLogger(modName);

        if (ReflectionHelper.findMod("net.minecraft.src.ModLoader"))
        {
            // FML or ModLoader logger.
            LOG.setParent(net.minecraft.src.ModLoader.getLogger());
        }

        if (DEBUG)
        {
            LOG.setUseParentHandlers(false);
            LOG.setLevel(Level.FINER);

            if (handler == null)
            {
                handler = new ConsoleHandler();
                handler.setFormatter(new LogFormatter());
                handler.setLevel(Level.FINER);
            }

            LOG.addHandler(handler);
        }
        else
        {
            LOG.setUseParentHandlers(true);

            if (handler != null)
            {
                LOG.removeHandler(handler);
            }
        }
    }

    public static Logger getLogger()
    {
        return LOG;
    }

    public static void log(Level level, String format, Object... data)
    {
        if (DEBUG || canLogging(level))
        {
            LOG.log(level, String.format(format, data));
        }
    }

    public static void log(Level level, Throwable e, String format, Object... data)
    {
        if (DEBUG || canLogging(level))
        {
            LOG.log(level, String.format(format, data), e);
        }
    }

    public static void severe(String format, Object... data)
    {
        log(Level.SEVERE, format, data);
    }

    public static void severe(Throwable e, String format, Object... data)
    {
        log(Level.SEVERE, e, format, data);
    }

    public static void warning(String format, Object... data)
    {
        log(Level.WARNING, format, data);
    }

    public static void info(String format, Object... data)
    {
        log(Level.INFO, format, data);
    }

    // FMLのロガーがログレベルの出力で config に対応してないので
    // public static void config(String format, Object... data)
    // {
    // log(Level.CONFIG, format, data);
    // }

    public static void fine(String format, Object... data)
    {
        log(Level.FINE, format, data);
    }

    public static void finer(String format, Object... data)
    {
        log(Level.FINER, format, data);
    }

    public static void finest(String format, Object... data)
    {
        log(Level.FINEST, format, data);
    }

    private static boolean canLogging(Level level)
    {
        return getLogLevel(LOG).intValue() <= level.intValue();
    }

    private static Level getLogLevel(Logger logger)
    {
        if (logger == null)
        {
            return Level.INFO;
        }

        Level level = logger.getLevel();

        if (level == null)
        {
            return getLogLevel(logger.getParent());
        }

        return level;
    }

    private static final class LogFormatter extends Formatter
    {
        static final String LINE_SEPARATOR = System.getProperty("line.separator");

        @Override
        public String format(LogRecord record)
        {
            StringBuilder msg = new StringBuilder();
            Level lvl = record.getLevel();

            if (lvl == Level.FINEST)
            {
                msg.append("[FINEST ]");
            }
            else if (lvl == Level.FINER)
            {
                msg.append("[FINER  ]");
            }
            else if (lvl == Level.FINE)
            {
                msg.append("[FINE   ]");
            }
            else if (lvl == Level.CONFIG)
            {
                msg.append("[CONFIG ]");
            }
            else if (lvl == Level.INFO)
            {
                msg.append("[INFO   ]");
            }
            else if (lvl == Level.WARNING)
            {
                msg.append("[WARNING]");
            }
            else if (lvl == Level.SEVERE)
            {
                msg.append("[SEVERE ]");
            }
            else
            {
                msg.append("[" + lvl.getLocalizedName() + "]");
            }

            if (record.getLoggerName() != null)
            {
                msg.append("(" + record.getLoggerName() + ") ");
            }

            msg.append(record.getMessage());
            msg.append(LINE_SEPARATOR);
            Throwable t = record.getThrown();

            if (t != null)
            {
                StringWriter thrDump = new StringWriter();
                t.printStackTrace(new PrintWriter(thrDump));
                msg.append(thrDump.toString());
            }

            return msg.toString();
        }
    }
}
