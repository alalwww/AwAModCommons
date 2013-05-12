/*
 * (c) 2013 alalwww <alalwww@awairo.net>
 * https://github.com/alalwww
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 *
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.common;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import net.awairo.mcmod.common.LogFormatter;

public class LogFormatterTest
{

    @Test
    public void testGetLevelString()
    {
        assertThat(LogFormatter.getLevelString("a"), is("[A      ]"));
        assertThat(LogFormatter.getLevelString("aa"), is("[AA     ]"));
        assertThat(LogFormatter.getLevelString("aaa"), is("[AAA    ]"));
        assertThat(LogFormatter.getLevelString("aaaa"), is("[AAAA   ]"));
        assertThat(LogFormatter.getLevelString("aaaaa"), is("[AAAAA  ]"));
        assertThat(LogFormatter.getLevelString("aaaaaa"), is("[AAAAAA ]"));
        assertThat(LogFormatter.getLevelString("aaaaaaa"), is("[AAAAAAA]"));
        assertThat(LogFormatter.getLevelString("aaaaaaaa"), is("[AAAAAAAA]"));
    }
}
