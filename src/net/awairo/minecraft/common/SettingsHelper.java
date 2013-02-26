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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.client.Minecraft;

/**
 * settings helper.
 * 
 * @author alalwww
 * @version 1.0.0
 */
public class SettingsHelper
{
    private static File configDir;

    /**
     * load configure file to properties.
     * 
     * @param properties
     *            properties
     * @param configFile
     *            configure file
     * 
     * @throws RuntimeException
     *             It's so bug ridden.
     */
    public static synchronized void load(Properties properties, File configFile) throws RuntimeException
    {
        try
        {
            checkFile(configFile);
            properties.load(new FileReader(configFile));
        }
        catch (Exception e)
        {
            Log.severe(e, "config load failed. (file=%s)", configFile);

            if (e instanceof RuntimeException)
            {
                throw (RuntimeException) e;
            }

            throw new RuntimeException(e);
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
    public static synchronized void store(Properties properties, File configFile, String comments)
            throws RuntimeException
    {
        try
        {
            checkFile(configFile);

            if (!configFile.canWrite())
            {
                throw new IllegalStateException("could not write file." + configFile.getAbsolutePath());
            }

            FileWriter writer = null;

            try
            {
                writer = new FileWriter(configFile);
                properties.store(writer, comments);
            }
            finally
            {
                if (writer != null)
                {
                    writer.close();
                }
            }
        }
        catch (Exception e)
        {
            Log.severe(e, "config store failed. (file=%s)", configFile);

            if (e instanceof RuntimeException)
            {
                throw (RuntimeException) e;
            }

            throw new RuntimeException(e);
        }
    }

    /**
     * get "%appdata%\.minecraft\config" directory.
     * 
     * @return config directory
     */
    public static File getConfigDir()
    {
        if (configDir != null)
        {
            return configDir;
        }

        if (ReflectionHelper.findMod("cpw.mods.fml.common.Loader"))
        {
            // for FML
            configDir = cpw.mods.fml.common.Loader.instance().getConfigDir();
            return configDir;
        }

        if (ReflectionHelper.findMod("net.minecraft.src.ModLoader"))
        {
            // for original ModLoader
            configDir = ReflectionHelper.getPrivateValue(net.minecraft.src.ModLoader.class, null, "cfgdir");
            return configDir;
        }

        configDir = new File(Minecraft.getMinecraftDir(), "config");
        return configDir;
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
     * 
     * @throws RuntimeException
     *             It's so bug ridden.
     */
    public static <V> V getValue(Properties properties, String key, V defaultValue) throws RuntimeException
    {
        assert defaultValue != null;
        String value = properties.getProperty(key);

        if (value == null)
        {
            properties.setProperty(key, defaultValue.toString());
            return defaultValue;
        }

        if (defaultValue instanceof String)
        {
            return (V) value;
        }

        Object ret;

        if (defaultValue instanceof Boolean)
        {
            ret = Boolean.parseBoolean(value);
        }
        else if (defaultValue instanceof Integer)
        {
            ret = Integer.parseInt(value);
        }
        else if (defaultValue instanceof Long)
        {
            ret = Long.parseLong(value);
        }
        else if (defaultValue instanceof Float)
        {
            ret = Float.parseFloat(value);
        }
        else if (defaultValue instanceof Double)
        {
            ret = Double.parseDouble(value);
        }
        else if (defaultValue instanceof Short)
        {
            ret = Short.parseShort(value);
        }
        else if (defaultValue instanceof Byte)
        {
            ret = Byte.parseByte(value);
        }
        else
        {
            Log.severe("cannot cast. (key=%s, value=%s, type=%s)", key, value, defaultValue.getClass());
            throw new IllegalArgumentException();
        }

        return (V) ret;
    }

    private static void checkFile(File file) throws IOException
    {
        if (file.isDirectory())
        {
            throw new IllegalArgumentException("'file' is directory. :" + file.getAbsolutePath());
        }

        if (!file.exists())
        {
            if (file.createNewFile())
            {
                Log.info("config file created. (path=%s)", file.getAbsolutePath());
            }
            else
            {
                if (!file.exists())
                {
                    throw new IllegalStateException("File creation failed. :" + file.getAbsolutePath());
                }
            }
        }
    }
}
