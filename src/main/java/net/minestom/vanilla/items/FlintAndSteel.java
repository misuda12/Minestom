package net.minestom.vanilla.items;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.CustomBlock;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.Direction;

public class FlintAndSteel extends VanillaItem {
    public FlintAndSteel() {
        super(Material.FLINT_AND_STEEL);
    }

    @Override
    public void onUseInAir(Player player, ItemStack itemStack, Player.Hand hand) {

    }

    @Override
    public boolean onUseOnBlock(Player player, ItemStack itemStack, Player.Hand hand, BlockPosition position, Direction blockFace) {
        // TODO: check if flammable
        BlockPosition firePosition = new BlockPosition(position.getX(), position.getY(), position.getZ());

        Instance instance = player.getInstance();
        Block atFirePosition = Block.fromStateId(instance.getBlockStateId(firePosition));
        if(atFirePosition.isAir()) {
            CustomBlock fireBlock = MinecraftServer.getBlockManager().getCustomBlock(Block.FIRE.getBlockId());
            if(!player.isCreative()) {
                VanillaItem.damageItem(player, hand, itemStack);
            }
            if(fireBlock != null) {
                instance.setCustomBlock(firePosition.getX(), firePosition.getY(), firePosition.getZ(), fireBlock.getCustomBlockId());
            } else {
                instance.setBlock(firePosition, Block.FIRE);
            }
        }
        return false;
    }

}
