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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Throwables;

import net.minecraft.client.Minecraft;

/**
 * settings helper.
 * 
 * @author alalwww
 */
public class SettingsHelper
{
    private static final Logger LOG = CommonLogger.getLogger();

    private SettingsHelper()
    {
    }

    private static volatile File modConfigDirectory;

    /**
     * get "%appdata%\.minecraft\config" directory.
     * 
     * @return config directory
     */
    @Nonnull
    public static File getConfigDir()
    {
        File file = modConfigDirectory;
        if (file != null)
            return file;

        synchronized (SettingsHelper.class)
        {
            file = modConfigDirectory;
            if (file != null)
                return file;

            if (ReflectionHelper.findClass("cpw.mods.fml.common.Loader"))
            {
                // for FML
                modConfigDirectory = cpw.mods.fml.common.Loader.instance().getConfigDir();
                return modConfigDirectory;
            }

            if (ReflectionHelper.findClass("net.minecraft.src.ModLoader"))
            {
                // for original ModLoader
                modConfigDirectory = ReflectionHelper
                        .getFieldValue(net.minecraft.src.ModLoader.class, null, "cfgdir");
                return modConfigDirectory;
            }

            modConfigDirectory = new File(Minecraft.getMinecraftDir(), "config");
            return modConfigDirectory;
        }
    }

    /**
     * load configure file to properties.
     * 
     * @param properties
     *            properties
     * @param configFile
     *            configure file
     */
    public static synchronized void load(@Nonnull Properties properties, @Nonnull File configFile)
    {
        checkNotNull(properties, "Argument 'properties' must not be null.");
        checkNotNull(configFile, "Argument 'configFile' must not be null.");

        try
        {
            createNewFile(configFile);
            properties.load(new FileReader(configFile));
        }
        catch (final Exception e)
        {
            LOG.severe(e, "config load failed. (file=%s)", configFile);
            throw Throwables.propagate(e);
        }
    }

    /**
     * store properties to configure file.
     * 
     * @param properties
     *            properties
     * @param configFile
     *            configure file
     * @param comments
     *            comments
     * 
     * @throws RuntimeException
     *             It's so bug ridden.
     */
    public static synchronized void store(@Nonnull Properties properties, @Nonnull File configFile,
            @Nullable String comments)
    {
        checkNotNull(properties, "Argument 'properties' must not be null.");
        checkNotNull(configFile, "Argument 'configFile' must not be null.");

        try
        {
            createNewFile(configFile);
            checkState(configFile.canWrite(), "could not write the file." + configFile.getAbsolutePath());

            try (
                FileWriter writer = new FileWriter(configFile))
            {
                properties.store(writer, comments);
            }
        }
        catch (final Exception e)
        {
            LOG.severe(e, "failed to store the settings. (file=%s)", configFile);
            throw Throwables.propagate(e);
        }
    }

    /**
     * get value and cast.
     * 
     * @param properties
     *            properties
     * @param key
     *            property key
     * @param defaultValue
     *            default value (not null)
     * @return casted value
     */
    @Nonnull
    public static <V> V getValue(@Nonnull Properties properties, @Nonnull String key, @Nonnull V defaultValue)
    {
        checkNotNull(key, "Argument 'key' must not be null.");
        checkNotNull(defaultValue, "Argument 'defaultValue' must not be null.");

        final String value = properties.getProperty(key);

        if (value != null)
            return convert(key, value, defaultValue);

        properties.setProperty(key, defaultValue.toString());
        return defaultValue;
    }

    /**
     * デフォルト値の型に変換します.
     * 
     * @param key
     *            ログ用のプロパティキー
     * @param value
     *            変換前の値
     * @param defaultValue
     *            デフォルト値
     * @return 変換後の値
     */
    @SuppressWarnings("unchecked")
    private static <V> V convert(String key, String value, V defaultValue)
    {
        if (defaultValue instanceof String)
            return (V) value;

        if (defaultValue instanceof Boolean)
            return (V) Boolean.valueOf(value);

        if (defaultValue instanceof Integer)
            return (V) Integer.valueOf(value);

        if (defaultValue instanceof Long)
            return (V) Long.valueOf(value);

        if (defaultValue instanceof Float)
            return (V) Float.valueOf(value);

        if (defaultValue instanceof Double)
            return (V) Double.valueOf(value);

        if (defaultValue instanceof Short)
            return (V) Short.valueOf(value);

        if (defaultValue instanceof Byte)
            return (V) Byte.valueOf(value);

        final String format = "cannot cast. (key=%s, value=%s, type=%s)";
        LOG.severe(format, key, value, defaultValue.getClass());
        throw new IllegalArgumentException(String.format(format, key, value, defaultValue.getClass()));
    }

    /**
     * ファイルが無ければ新しいファイルを生成します.
     * 
     * @param file
     *            ファイル
     * @throws IOException
     *             If an I/O error occurred
     */
    private static void createNewFile(File file) throws IOException
    {
        if (file.exists())
        {
            checkArgument(file.isFile(), "not a file. :" + file.getAbsolutePath());
            return;
        }

        if (file.createNewFile())
        {
            LOG.info("config file created. (path=%s)", file.getAbsolutePath());
            return;
        }

        checkState(file.exists(), "File creation failed. :" + file.getAbsolutePath());
    }
}
