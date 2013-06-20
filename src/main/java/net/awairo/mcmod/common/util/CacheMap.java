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

package net.awairo.mcmod.common.util;

import static com.google.common.base.Preconditions.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Supplier;
import com.google.common.primitives.Ints;

/**
 * 保持上限数を持った {@link LinkedHashMap} です。
 * 
 * @author alalwww
 */
public class CacheMap<K, V> extends LinkedHashMap<K, V>
{
    private static final long serialVersionUID = 1L;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final boolean ACCESS_ORDER = true;
    private final int maxSize;

    /**
     * Constructor.
     * 
     * @param maxSize
     */
    public CacheMap(int maxSize)
    {
        super(initialSize(maxSize), DEFAULT_LOAD_FACTOR, ACCESS_ORDER);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest)
    {
        return size() >= maxSize;
    }

    private static int initialSize(int maxSize)
    {
        checkArgument(maxSize >= 0);

        if (maxSize < 3)
            return maxSize + 1;

        if (maxSize < Ints.MAX_POWER_OF_TWO)
            return maxSize + maxSize / 3;

        return Integer.MAX_VALUE;
    }

    /**
     * この Map の新しい Supplier を生成します.
     * 
     * @param maxSize
     *            マップに格納する最大サイズ
     * @return factory
     */
    public static <K, V> Supplier<Map<K, V>> newMapSupplier(final int maxSize)
    {
        checkArgument(maxSize > 0);

        return new Supplier<Map<K, V>>()
        {
            @Override
            public Map<K, V> get()
            {
                return new CacheMap<>(maxSize);
            }
        };

    }
}
