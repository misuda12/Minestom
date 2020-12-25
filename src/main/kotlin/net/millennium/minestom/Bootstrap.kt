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

import net.minestom.server.MinecraftServer
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.ChunkGenerator
import net.minestom.server.instance.ChunkPopulator
import net.minestom.server.instance.batch.ChunkBatch
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.Position
import net.minestom.server.world.biomes.Biome
import java.util.*

fun main(vararg arguments : String) {
    println(arguments)
    val server = MinecraftServer.init()
    val container = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
    container.setChunkGenerator(ChunkGeneratorBase()).also {
        container.enableAutoChunkLoad(true)
    }

    val handler = MinecraftServer.getGlobalEventHandler()
    handler.addEventCallback(PlayerLoginEvent::class.java) {
        it.setSpawningInstance(container)
        it.player.respawnPoint = Position(0.0F, 42.0F, 0.0F)
    }
    server.start("localhost", 25565);
}

internal class ChunkGeneratorBase : ChunkGenerator {
    override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
        for (x in 0 until Chunk.CHUNK_SIZE_X) for (z in 0 until Chunk.CHUNK_SIZE_Z) for (y in 0..39)
            batch.setBlock(x, y, z, Block.STONE)
    }

    override fun fillBiomes(biomes: Array<out Biome>, chunkX: Int, chunkZ: Int)
            = Arrays.fill(biomes, Biome.PLAINS);

    override fun getPopulators(): MutableList<ChunkPopulator>? = null

}