/*
 * This file is part of Minestom, licensed under the MIT License.
 *
 * Copyright (C) Martin Coplák & Team
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.millennium.minestom

import net.minestom.server.Bootstrap

fun main(vararg arguments: String) {
    val argsWithMixins = arrayOfNulls<String>(arguments.size + 2)
    System.arraycopy(arguments, 0, argsWithMixins, 0, arguments.size)

    argsWithMixins[argsWithMixins.size - 2] = "--mixin"
    argsWithMixins[argsWithMixins.size - 1] = "mixins.vanilla.json"

    println(argsWithMixins)

    // Config.create(argsWithMixins[argsWithMixins.size - 2], MixinEnvironment.getDefaultEnvironment())
    Bootstrap.bootstrap("net.millennium.minestom.LaunchServerStartupKt", argsWithMixins)
}