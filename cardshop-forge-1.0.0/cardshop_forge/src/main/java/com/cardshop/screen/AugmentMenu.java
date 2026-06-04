package com.cardshop.screen;

import com.cardshop.item.AugmentItem;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AugmentMenu extends AbstractContainerMenu {

    public AugmentMenu(int syncId, Inventory inv) {
        super(ModMenuTypes.AUGMENT.get(), syncId);

        // 5 slots augments
        for (int i = 0; i < 5; i++) {
            final int idx = i;
            addSlot(new Slot(inv, idx, 8 + i*22, 18) {
                @Override public boolean mayPlace(ItemStack s) {
                    return s.getItem() instanceof AugmentItem;
                }
            });
        }

        // Inventaire
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inv, col + row*9 + 9, 8 + col*18, 60 + row*18));

        // Barre rapide
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inv, col, 8 + col*18, 118));
    }

    @Override
    public ItemStack quickMoveStack(Player p, int i) { return ItemStack.EMPTY; }

    @Override
    public boolean stillValid(Player p) { return true; }
}
