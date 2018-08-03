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

package cn.nekocode.luaxposed

import cn.nekocode.luaxposed.luaj.XLuaGlobals
import org.junit.Assert
import org.junit.Test
import org.luaj.vm2.LuaInteger
import java.io.File

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class LuaTest {

    @Test
    fun testLoad() {
        val globals = XLuaGlobals(File("."))
        globals.load("rlt=10").call()
        val rlt = (globals.get("rlt") as LuaInteger).toint()
        Assert.assertEquals(rlt, 10)
    }
}