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

package cn.nekocode.luaxposed.luaj.lib

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class JseSocketLib: TwoArgFunction() {

    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val socket = LuaTable()
        socket.set("tcp", tcp())
        socket.set("bind", bind())
        env.set("socket", socket)
        env.get("package").get("loaded").set("socket", socket)
        return socket
    }

    // local sock, err = socket.tcp()
    class tcp: VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            return LuaValue.varargsOf(sock(), LuaValue.NONE)
        }

        fun sock(): LuaTable {
            val table = LuaTable()
            table.set("settimeout", settimeout())
            table.set("connect", connect())
            table.set("send", send())
            table.set("receive", receive())
            table.set("close", close())
            return table
        }

        class settimeout: VarArgFunction() {
            override fun invoke(args: Varargs): Varargs {
                return LuaValue.NONE
            }
        }

        // res, err
        class connect: VarArgFunction() {
            override fun invoke(args: Varargs): Varargs {
                return LuaValue.varargsOf(LuaValue.NONE, LuaValue.NONE)
            }
        }

        class send: OneArgFunction() {
            override fun call(msg: LuaValue?): LuaValue {
                return LuaValue.NONE
            }
        }

        // line, err = server:receive([size])
        class receive: VarArgFunction() {
            override fun invoke(args: Varargs): Varargs {
                return LuaValue.varargsOf(LuaValue.NONE, LuaValue.NONE)
            }
        }

        class close: ZeroArgFunction() {
            override fun call(): LuaValue {
                return LuaValue.NONE
            }
        }
    }

    class bind: TwoArgFunction() {
        override fun call(host: LuaValue?, port: LuaValue?): LuaValue {
            return server()
        }

        fun server(): LuaTable {
            val table = LuaTable()
            table.set("accept", accept())
            return table
        }

        class accept: ZeroArgFunction() {
            override fun call(): LuaValue {
                return LuaValue.NONE
            }

            fun client(): LuaTable {
                val table = LuaTable()
                table.set("send", send())
                table.set("receive", receive())
                table.set("close", close())
                return table
            }

            class send: OneArgFunction() {
                override fun call(msg: LuaValue?): LuaValue {
                    return LuaValue.NONE
                }
            }

            class receive: VarArgFunction() {
                override fun invoke(args: Varargs): Varargs {
                    return LuaValue.varargsOf(LuaValue.NONE, LuaValue.NONE)
                }
            }

            class close: ZeroArgFunction() {
                override fun call(): LuaValue {
                    return LuaValue.NONE
                }
            }
        }
    }
}