/*
 * AwA Minecraft's mod commons.
 *
 * (c) 2013 alalwww <alalwww@awairo.net>
 * https://github.com/alalwww
 *
 * This library is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 *
 * このライブラリは、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.minecraft.common;

import static net.awairo.minecraft.common.PreconditionUtils.*;

import java.util.Properties;

import javax.annotation.Nonnull;

/**
 * Version class.
 * 
 * @author alalwww
 * @version 2.0
 */
public final class Version
{
    /** majro version. */
    @Nonnull
    public final String major;
    /** minor version. */
    @Nonnull
    public final String minor;
    /** build number. */
    @Nonnull
    public final String build;
    /** revision. */
    @Nonnull
    public final String revision;

    /** commit sha-1. */
    @Nonnull
    public final String githash;

    /** Minecraft client version. */
    @Nonnull
    public final String mcClientVersion;
    /** Minecraft server version. */
    @Nonnull
    public final String mcServerVersion;

    /** version. */
    @Nonnull
    public final String versionString;

    /**
     * set mod id and version.properties.
     */
    public Version(@Nonnull String modId, Properties versionProperties)
    {
        checkArgNotNull(modId);

        if (Env.develop())
        {
            major = "";
            minor = "";
            build = "";
            revision = "";
            githash = "";
            mcClientVersion = "";
            mcServerVersion = "";
            versionString = "develop";
            return;
        }

        major = versionProperties.getProperty(modId + ".version.major");
        minor = versionProperties.getProperty(modId + ".version.minor");
        build = versionProperties.getProperty(modId + ".version.build");
        revision = versionProperties.getProperty(modId + ".version.revision");

        githash = versionProperties.getProperty(modId + ".version.githash");

        mcClientVersion = versionProperties.getProperty(modId + ".version.client");
        mcServerVersion = versionProperties.getProperty(modId + ".version.server");

        versionString = String.format("%s.%s.%s #%s", major, minor, build, revision);
    }

    @Override
    public String toString()
    {
        return versionString;
    }
}
