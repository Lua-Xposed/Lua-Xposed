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

package cn.nekocode.luaxposed.luaj.sandbox

import org.luaj.vm2.LuaError
import org.luaj.vm2.lib.jse.JseIoLib

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class XIoLib(private val pathValidator: ((String) -> String?)?): JseIoLib() {

    override fun openFile(
            filename: String, readMode: Boolean, appendMode: Boolean,
            updateMode: Boolean, binaryMode: Boolean): File {

        val validator = pathValidator
                ?:
                return super.openFile(filename, readMode, appendMode, updateMode, binaryMode)

        val absolutePath = validator.invoke(filename)
                ?:
                throw LuaError("Not a legal file path.")

        return super.openFile(absolutePath, readMode, appendMode, updateMode, binaryMode)
    }

    override fun openProgram(prog: String?, mode: String?): File {
        throw LuaError("Unsupported operation.")
    }
}