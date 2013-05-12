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

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Handler;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.Mod;

/**
 * ロガー.
 *
 * @author alalwww
 */
public class Logger
{
    private static final ConcurrentMap<String, Logger> PUBLISHED_LOGGERS;

    /** 標準エラー. */
    @Nullable
    protected static final PrintStream SYS_ERR;

    /** logger. */
    @Nonnull
    protected final java.util.logging.Logger logger;

    /** handler. */
    @Nullable
    protected Handler handler;

    /** mod id. */
    @Nonnull
    protected final String modId;

    /** mod trace enabled. */
    public boolean modTraceEnabled;

    /** mod debug enabled. */
    public boolean modDebugEnabled;

    static
    {
        PUBLISHED_LOGGERS = Maps.newConcurrentMap();
        SYS_ERR = getSysErr();
    }

    private static PrintStream getSysErr()
    {
        try
        {
            Class.forName("cpw.mods.fml.relauncher.FMLRelaunchLog");
            final Field errCache = cpw.mods.fml.relauncher.FMLRelaunchLog.class.getDeclaredField("errCache");
            errCache.setAccessible(true);
            return (PrintStream) errCache.get(null);
        }
        catch (final Exception ignore)
        {
            // ignore.printStackTrace();
            return System.err;
        }
    }

    /**
     * ロガー取得.
     *
     * @param modClass
     *            {@link Mod} アノテーションで修飾されたMODクラス
     * @return ロガー
     */
    public static Logger getLogger(@Nonnull Class<?> modClass)
    {
        final Mod mod = modClass.getAnnotation(Mod.class);

        if (mod == null || Strings.isNullOrEmpty(mod.modid()))
            throw new IllegalArgumentException();

        return getLogger(mod.modid());
    }

    /**
     * ロガー取得.
     *
     * @param modId
     *            MOD ID
     * @return ロガー
     */
    public static Logger getLogger(@Nonnull String modId)
    {
        Logger logger = PUBLISHED_LOGGERS.get(modId);

        if (logger != null)
            return logger;

        synchronized (Logger.class)
        {
            logger = PUBLISHED_LOGGERS.get(modId);

            if (logger != null)
                return logger;

            return new Logger(modId);
        }
    }

    private static Logger addPublishedLogger(Logger logger)
    {
        synchronized (Logger.class)
        {
            final Logger old = PUBLISHED_LOGGERS.put(logger.modId, logger);

            if (old == null)
                return logger;

            return logger;
        }
    }

    /**
     * Constructor.
     *
     * @param modId
     *            mod id
     * @param modName
     *            mod name
     */
    protected Logger(@Nonnull String modId)
    {
        this.modId = modId;
        modTraceEnabled = Env.isEnable(modId + ".trace");
        modDebugEnabled = Env.isEnable(modId + ".debug");
        logger = createNewLogger();
        initLogLevel();
        addPublishedLogger(this);
        info("create logger. LEVEL=%s", modId, getLogLevel(logger));
    }

    /**
     * 新しいログハンドラー生成.
     *
     * @return ハンドラー
     */
    @Nonnull
    protected Handler createNewConsoleHandler()
    {
        final Handler handler = new DebugConsoleHandler(SYS_ERR);
        handler.setFormatter(new LogFormatter());
        return handler;
    }

    /**
     * 新しいロガー生成.
     *
     * @return ロガー
     */
    @Nonnull
    protected java.util.logging.Logger createNewLogger()
    {
        final java.util.logging.Logger newLogger = java.util.logging.Logger.getLogger(modId);
        newLogger.setUseParentHandlers(false);

        if (!Env.develop())
            return newLogger;

        handler = createNewConsoleHandler();
        newLogger.addHandler(handler);

        return newLogger;
    }

    /**
     * ログレベル初期化.
     *
     * @param logger
     *            ロガー
     * @param handler
     *            ハンドラー
     */
    protected final void initLogLevel()
    {
        if (isTraceEnabled())
        {
            setLevel(Level.FINEST);
            return;
        }

        if (isDebugEnabled())
        {
            setLevel(Level.FINE);
            return;
        }

        setLevel(Level.INFO);
    }

    /**
     * ログレベルを設定.
     *
     * @param level
     *            ログレベル
     */
    public final void setLevel(Level level)
    {
        checkArgNotNull(level);

        logger.setLevel(level);

        if (handler != null)
            handler.setLevel(level);
    }

    /**
     * 親ログハンドラー(FMLのログハンドラー)の有効/無効を切り替えます.
     *
     * <p>
     * FMLのFileHandlerのログレベルは {@link Level#ALL} であり、全てのレベルのログが出力されています。
     * デバッグ用やトレース用など、通常は必要とならないような詳細情報が全て出力されてしまうことを避けるため、
     * 親ログハンドラーの使用可否を制御することで、出力されるログを制限します。
     *
     * プライベートメソッド {@link #log(Level, String, Throwable)} で {@link Level#INFO} 以上のみ
     * 親ログハンドラーを有効化していますが、変更の度にログマネージャーのパーミッションチェックが走るため、
     * 複数のログを纏めて出力する際に、事前に変更ができるように、このメソッドを公開しています。
     * </p>
     * <p>
     * 開発環境では常に専用のコンソールハンドラーを使用しているため、このメソッドは何も行いません。
     * </p>
     *
     * @param useParentHandlers
     *            親ログハンドラーを有効にする場合 true
     */
    public final void setUseParentHandlers(boolean useParentHandlers)
    {
        if (!Env.develop() && logger.getUseParentHandlers() != useParentHandlers)
            logger.setUseParentHandlers(useParentHandlers);
    }

    /**
     * トレースログを出力するか判定.
     *
     * @return デバッグとトレースが有効の場合 true
     */
    public final boolean isTraceEnabled()
    {
        return isDebugEnabled() && modTraceEnabled;
    }

    /**
     * デバッグログを出力するか判定.
     *
     * @return デバッグが有効の場合 true
     */
    public final boolean isDebugEnabled()
    {
        return Env.debug() || modDebugEnabled;
    }

    /**
     * 指定のログレベルが INFO 未満か判定します.
     *
     * @param level
     *            ログレベル
     * @return INFO未満のログレベルの場合true
     */
    public final boolean isLesserThanInfo(Level level)
    {
        checkArgNotNull(level);
        return level.intValue() < Level.INFO.intValue();
    }

    /**
     * エラーログ.
     *
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void severe(@Nonnull String format, Object... args)
    {
        log(Level.SEVERE, format, args);
    }

    /**
     * エラーログ.
     *
     * @param e
     *            例外またはエラー
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void severe(Throwable e, @Nonnull String format, Object... args)
    {
        log(Level.SEVERE, e, format, args);
    }

    /**
     * 警告ログ.
     *
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void warning(@Nonnull String format, Object... args)
    {
        log(Level.WARNING, format, args);
    }

    /**
     * 警告ログ.
     *
     * @param e
     *            例外またはエラー
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void warning(Throwable e, @Nonnull String format, Object... args)
    {
        log(Level.WARNING, e, format, args);
    }

    /**
     * 情報ログ.
     *
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void info(@Nonnull String format, Object... args)
    {
        log(Level.INFO, format, args);
    }

    /**
     * 情報ログ.
     *
     * @param e
     *            例外またはエラー
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void info(Throwable e, @Nonnull String format, Object... args)
    {
        log(Level.INFO, format, args);
    }

    /**
     * デバッグログ用.
     *
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void debug(@Nonnull String format, Object... args)
    {
        log(Level.FINE, format, args);
    }

    /**
     * デバッグログ用.
     *
     * @param e
     *            例外またはエラー
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void debug(Throwable e, @Nonnull String format, Object... args)
    {
        log(Level.FINE, format, args);
    }

    /**
     * 頻度が高いログ出力用.
     *
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void trace(@Nonnull String format, Object... args)
    {
        log(Level.FINEST, format, args);
    }

    /**
     * 頻度が高いログ出力用.
     *
     * @param e
     *            例外またはエラー
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void trace(Throwable e, @Nonnull String format, Object... args)
    {
        log(Level.FINEST, format, args);
    }

    /**
     * ログレベルを指定してログ出力.
     *
     * @param level
     *            ログレベル
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void log(Level level, @Nonnull String format, Object... args)
    {
        log(level, null, format, args);
    }

    /**
     * ログレベルを指定してログ出力.
     *
     * @param level
     *            ログレベル
     * @param throwable
     *            例外またはエラー
     * @param format
     *            メッセージフォーマット
     * @param args
     *            メッセージ引数
     */
    public void log(Level level, Throwable e, @Nonnull String format, Object... args)
    {
        checkArgNotNull(level);
        checkArgNotNull(format);

        if (!Env.develop() && !canLogging(level))
            return;

        final String message = (args != null && args.length > 0) ? String.format(format, args) : format;

        if (Env.develop() || isLesserThanInfo(level) || logger.getUseParentHandlers())
        {
            log(level, message, e);
        }
        else
        {
            logger.setUseParentHandlers(true);
            log(level, message, e);
            logger.setUseParentHandlers(false);
        }
    }

    private void log(Level level, String message, Throwable e)
    {
        if (e == null)
            logger.log(level, message);

        else
            logger.log(level, message, e);
    }

    /**
     * ロギング可能なレベルか判定します.
     *
     * @param level
     *            レベル
     * @return 現在のロガーのレベルよりも高い場合 true
     */
    private boolean canLogging(Level level)
    {
        return getLogLevel(logger).intValue() <= level.intValue();
    }

    /**
     * 親ロガーを再帰的に遡り、ログレベルを取得します.
     *
     * @param logger
     *            ロガー
     * @return ログレベル
     */
    private static Level getLogLevel(java.util.logging.Logger logger)
    {
        if (logger == null)
            return Level.INFO;

        final Level level = logger.getLevel();

        if (level == null)
            return getLogLevel(logger.getParent());

        return level;
    }
}
