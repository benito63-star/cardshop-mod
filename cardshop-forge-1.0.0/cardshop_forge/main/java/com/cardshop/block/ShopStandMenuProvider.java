package com.cardshop.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ShopStandMenuProvider implements MenuProvider {

    private final BlockPos            pos;
    private final ShopStandBlockEntity be;

    public ShopStandMenuProvider(BlockPos pos, ShopStandBlockEntity be) {
        this.pos = pos; this.be = be;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("\u00a76 " + be.getOwnerName() + "\'s Shop");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId,
            Inventory inv, Player player) {
        return new ShopStandMenu(syncId, inv, be);
    }
}
