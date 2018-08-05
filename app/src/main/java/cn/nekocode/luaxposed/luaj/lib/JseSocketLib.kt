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

import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.ZeroArgFunction
import java.net.*

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class JseSocketLib: TwoArgFunction() {

    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val socket = LuaTable()
        socket.set("tcp", object: VarArgFunction() {
            override fun invoke(args: Varargs): Varargs {
                return LuaValue.varargsOf(Tcp(), LuaValue.NONE)
            }
        })
        socket.set("bind", BindFunc())

        env.set("socket", socket)
        env.get("package").get("loaded").set("socket", socket)
        return socket
    }

    open class Client(val socket: Socket): LuaTable() {
        init {
            this.set("receive", object: VarArgFunction() {
                override fun invoke(args: Varargs): Varargs {
                    return when {
                        args.narg() == 1 -> return try {
                            var txt = socket.getInputStream().bufferedReader().readLine()
                            LuaValue.varargsOf(LuaValue.valueOf(txt), LuaValue.NIL)
                        } catch (e: SocketTimeoutException) {
                            LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf("timeout"))
                        } catch (e: Exception) {
                            LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf(e.message))
                        }

                        args.narg() == 2 -> return try {
                            val bytes = kotlin.ByteArray(args.arg(2).checkint())
                            socket.getInputStream().read(bytes, 0, bytes.size)
                            LuaValue.varargsOf(LuaValue.valueOf(String(bytes)), LuaValue.NIL)
                        } catch (e: SocketTimeoutException) {
                            LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf("timeout"))
                        } catch (e: Exception) {
                            LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf(e.message))
                        }

                        else -> throw LuaError("Unsupported operation.")
                    }
                }
            })
            this.set("send", object: VarArgFunction() {
                override fun invoke(args: Varargs): Varargs {
                    return try {
                        val txt = args.arg(2).checkjstring()
                        socket.getOutputStream().writer().apply {
                            this.write(txt)
                            this.flush()
                        }
                        val index = LuaValue.listOf(
                                arrayOf(LuaValue.valueOf(1), LuaValue.valueOf(txt.length)))
                        LuaValue.varargsOf(index, LuaValue.NIL)
                    } catch (e: Exception) {
                        LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf(e.message))
                    }
                }
            })
            this.set("close", object: ZeroArgFunction() {
                override fun call(): LuaValue {
                    socket.close()
                    return LuaValue.NONE
                }
            })
        }
    }

    class Server(val socket: ServerSocket): LuaTable() {
        init {
            this.set("accept", object: VarArgFunction() {
                override fun invoke(args: Varargs): Varargs {
                    return try {
                        LuaValue.varargsOf(Client(socket.accept()), LuaValue.NIL)
                    } catch (e: Exception) {
                        LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf(e.message))
                    }
                }
            })
            this.set("close", object: ZeroArgFunction() {
                override fun call(): LuaValue {
                    socket.close()
                    return LuaValue.NONE
                }
            })
        }
    }

    class Tcp(socket: Socket = Socket()): Client(socket) {
        init {
            this.set("settimeout", object: VarArgFunction() {
                override fun invoke(args: Varargs): Varargs {
                    socket.soTimeout = if (args.narg() > 1) {
                        args.arg(2).checkint() * 1000
                    } else {
                        20 * 1000
                    }
                    return LuaValue.NONE
                }
            })
            this.set("connect", object: VarArgFunction() {
                override fun invoke(args: Varargs): Varargs {
                    return try {
                        socket.connect(InetSocketAddress(
                                args.arg(2).checkjstring(), args.arg(3).checkint()))
                        LuaValue.varargsOf(Client(socket), LuaValue.NIL)
                    } catch (e: Exception) {
                        LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf(e.message))
                    }
                }
            })
            this.set("bind", BindFunc())
        }
    }

    class BindFunc: VarArgFunction() {
        override fun invoke(args: Varargs): Varargs {
            return try {
                var address = if (args.narg() == 2) {
                    args.arg(1).checkjstring()
                } else {
                    args.arg(2).checkjstring()
                }
                if (address == "*") {
                    address = "0.0.0.0"
                }
                val port = if (args.narg() == 2) {
                    args.arg(2).checkint()
                } else {
                    args.arg(3).checkint()
                }

                val serverSocket = ServerSocket()
                serverSocket.bind(InetSocketAddress(address, port))
                LuaValue.varargsOf(Server(serverSocket), LuaValue.NIL)
            } catch (e: Exception) {
                LuaValue.varargsOf(LuaValue.NIL, LuaValue.valueOf(e.message))
            }
        }
    }
}