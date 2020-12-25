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
import net.minestom.server.MinecraftServer
import net.minestom.server.ping.ResponseData
import net.minestom.vanilla.PlayerInit
import net.minestom.vanilla.anvil.FileSystemStorage
import net.minestom.vanilla.blocks.VanillaBlocks
import net.minestom.vanilla.commands.VanillaCommands
import net.minestom.vanilla.gamedata.loottables.VanillaLootTables
import net.minestom.vanilla.generation.VanillaWorldgen
import net.minestom.vanilla.items.VanillaItems
import net.minestom.vanilla.system.NetherPortal
import net.minestom.vanilla.system.ServerProperties
import java.io.File


fun main(vararg arguments: String) {
    val argsWithMixins = arrayOfNulls<String>(arguments.size + 2)
    System.arraycopy(arguments, 0, argsWithMixins, 0, arguments.size)

    argsWithMixins[argsWithMixins.size - 2] = "--mixin"
    argsWithMixins[argsWithMixins.size - 2] = "mixins.vanilla.json"
    Bootstrap.bootstrap("net.millennium.minestom.LaunchServerStartup", argsWithMixins)
}

class LaunchServerStartup {
    companion object {
        @JvmStatic
        fun main(vararg arguments: String) {
            val server = MinecraftServer.init()
            val commandManager = MinecraftServer.getCommandManager()
            VanillaWorldgen.prepareFiles().also {
                VanillaWorldgen.registerAllBiomes(MinecraftServer.getBiomeManager())
            }
            VanillaCommands.registerAll(commandManager)
            VanillaItems.registerAll(MinecraftServer.getConnectionManager())
            VanillaBlocks.registerAll(MinecraftServer.getConnectionManager(), MinecraftServer.getBlockManager())
            NetherPortal.registerData(MinecraftServer.getDataManager())
            val lootTableManager = MinecraftServer.getLootTableManager()
            VanillaLootTables.register(lootTableManager)

            MinecraftServer.getStorageManager().defineDefaultStorageSystem { FileSystemStorage() }
            val properties = ServerProperties(File(".", "server.properties"))
            PlayerInit.init(properties)

            MinecraftServer.getSchedulerManager().buildShutdownTask {
                val connectionManager = MinecraftServer.getConnectionManager()
                connectionManager.onlinePlayers.forEach {
                    it.kick("[Kernel] Server is closing ")
                    connectionManager.removePlayer(it.playerConnection)
                }
            }

            server.start(properties["server-ip"], properties["server-port"].toInt()) { _, response: ResponseData ->
                response.setName(MinecraftServer.VERSION_NAME)
                response.setMaxPlayer(properties["max-players"].toInt())
                response.setOnline(MinecraftServer.getConnectionManager().onlinePlayers.size)
                response.setDescription(properties["motd"])
                response.setFavicon("data:image/png;base64,<data>")
            }
        }
    }
}