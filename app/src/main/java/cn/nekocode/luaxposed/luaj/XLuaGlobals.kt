/*
 * Copyright (C) 2018 nekocode (nekocode.cn@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.nekocode.luaxposed.luaj

import cn.nekocode.luaxposed.luaj.sandbox.XClassLoader
import cn.nekocode.luaxposed.luaj.sandbox.XIoLib
import cn.nekocode.luaxposed.utils.PathUtils
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaError
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.LuajavaLib
import java.io.File
import java.io.FileInputStream

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class XLuaGlobals(baseDir: File) : Globals() {

    init {
        val validator = validator@ { path: String ->
            val file = File(path)

            if (file.isAbsolute) {
                return@validator file.absolutePath
            }

            return@validator File(baseDir, path).absolutePath
        }

        load(BaseLib())
        load(PackageLib())
        load(Bit32Lib())
        load(OsLib())
        load(MathLib())
        load(TableLib())
        load(StringLib())
        load(CoroutineLib())
        load(LuajavaLib(XClassLoader()))
        load(XIoLib(validator))
        load(DebugLib())
        LoadState.install(this)
        LuaC.install(this)


        this.finder = ResourceFinder { path ->
            val absolutePath = validator.invoke(path)
                    ?: // Not a legal path
                    return@ResourceFinder null

            try {
                val file = File(absolutePath)
                if (file.exists()) {
                    return@ResourceFinder FileInputStream(file)
                }

            } catch (t: Throwable) {
                throw LuaError(t)
            }

            return@ResourceFinder null
        }
    }
}