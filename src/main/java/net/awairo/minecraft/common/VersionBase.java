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
 * @version 1.0.0
 */
public abstract class VersionBase
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
    protected VersionBase(@Nonnull String modId, @Nonnull Properties prop)
    {
        checkArgNotNull(modId);
        checkArgNotNull(prop);

        major = toNonnull(prop.getProperty(modId + ".version.major"));
        minor = toNonnull(prop.getProperty(modId + ".version.minor"));
        build = toNonnull(prop.getProperty(modId + ".version.build"));
        revision = toNonnull(prop.getProperty(modId + ".version.revision"));

        githash = toNonnull(prop.getProperty(modId + ".version.githash"));

        mcClientVersion = toNonnull(prop.getProperty(modId + ".version.client"));
        mcServerVersion = toNonnull(prop.getProperty(modId + ".version.server"));

        versionString = toNonnull(String.format("%s.%s.%s #%s", major, minor, build, revision));
    }

    protected VersionBase()
    {
        versionString = "RML IS NON-SUPPORTED!!";

        major = "";
        minor = "";
        build = "";
        revision = "";
        githash = "";
        mcClientVersion = "";
        mcServerVersion = "";
    }

    @Override
    public String toString()
    {
        return versionString;
    }
}
