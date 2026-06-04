package com.cardshop.block;

import com.cardshop.augment.AugmentManager;
import com.cardshop.economy.EconomyManager;
import com.cardshop.item.CardItem;
import com.cardshop.screen.ModMenuTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;

public class ShopStandMenu extends AbstractContainerMenu {

    private final ShopStandBlockEntity blockEntity;
    private final int rank;

    public ShopStandMenu(int syncId, Inventory playerInv,
                          ShopStandBlockEntity be) {
        super(ModMenuTypes.SHOP_STAND.get(), syncId);
        this.blockEntity = be;
        this.rank        = be != null ? be.getCurrentRank() : 0;

        // 9 slots de vente (cartes uniquement)
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++) {
                final int idx = col + row * 9;
                addSlot(new Slot(playerInv, idx, 44 + col*18, 18 + row*18) {
                    @Override public boolean mayPlace(ItemStack s) {
                        return s.getItem() instanceof CardItem;
                    }
                });
            }

        // Inventaire joueur
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(playerInv, col + row*9 + 9,
                    8 + col*18, 84 + row*18));

        // Barre rapide
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(playerInv, col, 8 + col*18, 142));
    }

    // Constructeur client
    public ShopStandMenu(int syncId, Inventory inv) {
        this(syncId, inv, null);
    }

    public long sellAllCards(ServerPlayer player) {
        if (blockEntity == null) return 0;
        float mult    = ShopRankManager.getPriceMultiplier(rank);
        float augBonus = AugmentManager.getSellBonus(player);
        long  total   = 0;
        int   sold    = 0;

        for (int i = 0; i < 9; i++) {
            Slot      slot  = getSlot(i);
            ItemStack stack = slot.getItem();
            Optional<com.cardshop.card.Card> cardOpt = CardItem.getCard(stack);
            if (cardOpt.isPresent()) {
                long value = (long)(cardOpt.get().getSellValue()
                    * mult * (1f + augBonus));
                EconomyManager.add(player, value);
                total += value;
                sold++;
                slot.set(ItemStack.EMPTY);
            }
        }
        if (sold > 0) {
            blockEntity.recordSale();
            player.displayClientMessage(Component.literal(
                "\u00a7a" + sold + " carte(s) vendue(s) pour \u00a76" +
                total + " Coins"), true);
        }
        return total;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) { return true; }

    public int getRank() { return rank; }
}
