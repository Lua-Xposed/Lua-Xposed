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

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
class XClassLoader: ClassLoader() {
    companion object {
        private val WHITE_LIST = arrayOf<String>(
        )

        private val BLACK_LIST = arrayOf<String>(
        )

        private val REPLACEMENT_LIST = arrayOf<Pair<String, String>>(
        )
    }

    override fun loadClass(name: String): Class<*> {
        // Check black-list
        BLACK_LIST.forEach {
            if (name.matches(it.toRegex())) {
                throw ClassNotFoundException(name)
            }
        }

        // Check white-list
        WHITE_LIST.forEach {
            if (name.matches(it.toRegex())) {
                return Class.forName(name)
            }
        }

        // Check replacement-list
        REPLACEMENT_LIST.forEach {
            if (name.matches(it.first.toRegex())) {
                return Class.forName(it.second)
            }
        }

        // Use system default class loader
        return ClassLoader.getSystemClassLoader().loadClass(name)
    }
}