package net.minestom.vanilla.blocks;

import net.minestom.server.instance.block.Block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a block state
 */
public class BlockState {

    private final BlockStates parent;
    private final Map<String, String> properties;
    private final short blockId;

    public BlockState(short blockId, BlockStates parent, String... propertyList) {
        this.blockId = blockId;
        Map<String, String> properties = new HashMap<>();
        this.parent = parent;
        for(String property : propertyList) {
            String[] parts = property.split("=");
            String key = parts[0];
            String value = parts[1];
            properties.put(key, value);
        }

        this.properties = Collections.unmodifiableMap(properties);
    }

    public short getBlockId() {
        return blockId;
    }

    /**
     * Return the value of the given property key
     * @param key the property key
     * @return the value of the property
     * @throws IllegalArgumentException if the key does not correspond to an existing property
     */
    public String get(String key) {
        String result = properties.get(key);
        if(result == null) {
            throw new IllegalArgumentException("Property '" + key + "' does not exist in blockstate "+this);
        }
        return result;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Returns the block state corresponding to this state with a single property changed
     * @param key the key of the property to change
     * @param value the value of the property
     * @return the corresponding blockstate (they are pooled inside this blockstate's parent BlockStates)
     */
    public BlockState with(String key, String value) {
        return parent.getStateWithChange(properties, key, value);
    }

    @Override
    public String toString() {
        String props = getProperties().entrySet().stream().map(e -> e.getKey()+"="+e.getValue()).collect(Collectors.joining(","));
        return Block.fromStateId(blockId)+"{"+props+"}";
    }
}
