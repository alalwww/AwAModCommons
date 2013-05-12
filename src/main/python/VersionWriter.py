# -*- coding: utf-8 -*-

"""
version.properties ファイルにバージョン情報を書き出します.

 (c) 2013 alalwww
 https://github.com/alalwww

 This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt

 この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt

 git describe から mod のバージョン、MCP の commands から MCP やクライアントのバージョンを取得しています。

 iron chest を色々パｋ…参考にさせて頂きました。
 https://github.com/cpw/ironchest

"""
__author__  = "alalwww"
__version__ = "1.1.0"
__date__    = "05 May 2013"

import sys
import os
import commands
import fnmatch
import re
import subprocess
import shlex
import datetime

cmd_describe = "git describe --long"
pattern_gittag = "v(\d+).(\d+)-(\d+)-(g.*)"
dummy_version_string = "v1.0-0-deadbeef"

pattern_mcp_commands_fullversion = "[.\w]+ \(data: ([.\w]+), client: ([.\w.]+), server: ([.\w.]+)\)"

# args
mod_id = sys.argv[1]
mod_rev_number = sys.argv[2]
fml_build_number = sys.argv[3]
mcp_home = sys.argv[4]
version_properties_filename = sys.argv[5]

print("args: %s, %s, %s, %s, %s" % (mod_id, mod_rev_number, fml_build_number, mcp_home, version_properties_filename))

mcp_dir = os.path.abspath(mcp_home)
sys.path.append(mcp_dir)

from runtime.commands import Commands
Commands._version_config = os.path.join(mcp_dir, Commands._version_config)

def main():

    print("Obtaining version information from git")

    # get mod version
    try:
        process = subprocess.Popen(cmd_describe, bufsize= -1, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        git_tagstring, _ = process.communicate()
    except OSError as e:
        print("Git not found.")
        print("  type: " + str(type(e)))
        print("  " + str(e).decode("cp932").encode(encoding='utf-8'))
        print("----------")
        git_tagstring = dummy_version_string
    print("tag    : " + git_tagstring)
    print("pattern: " + pattern_gittag)
    (major, minor, build, githash) = re.match(pattern_gittag, git_tagstring).groups()

    # get mcp/minecraft version
    fullversion = Commands.fullversion()
    print("fullver: " + fullversion)
    print("pattern: " + pattern_mcp_commands_fullversion)
    (mcpversion, mcversion, mcserverversion) = re.match(pattern_mcp_commands_fullversion, fullversion).groups()

    # output
    with open(version_properties_filename, "w") as f:
        f.write("###################################################\n#\n")
        f.write("# version.properties\n")
        f.write("#\n")
        f.write("# create: " + str(datetime.datetime.now()) + "\n")
        f.write("#\n")
        f.write("###################################################\n")
        f.write("%s=%s\n" % (mod_id + ".version", "%s.%s.%s.%s" % (major, minor, build, mod_rev_number)))
        f.write("%s=%s\n" % (mod_id + ".version.major", major))
        f.write("%s=%s\n" % (mod_id + ".version.minor", minor))
        f.write("%s=%s\n" % (mod_id + ".version.build", build))
        f.write("%s=%s\n" % (mod_id + ".version.revision", mod_rev_number))
        f.write("%s=%s\n" % (mod_id + ".version.githash", githash))
        f.write("\n")
        f.write("%s=%s\n" % ("mod.version", "%s.%s.%s.%s" % (major, minor, build, mod_rev_number)))
        f.write("%s=%s\n" % ("mcmod.info.version", "%s.%s.%s #%s" % (major, minor, build, mod_rev_number)))
        f.write("%s=%s\n" % ("minecraft.version", mcversion))
        f.write("%s=%s\n" % ("mcp.version", mcpversion))
        f.write("%s=%s\n" % ("fml.build.number", fml_build_number))

        f.write("#[EOF]")

    print(" \n")
    print("Version information: " + mod_id + " %s.%s.%s #%s using MCP %s for Minecraft %s" % (major, minor, build, mod_rev_number, mcpversion, mcversion))

if __name__ == '__main__':
    main()
