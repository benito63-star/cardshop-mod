package com.cardshop.item;

import com.cardshop.booster.BoosterManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BoosterPackItem extends Item {

    public BoosterPackItem() {
        super(new Properties().stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof
                net.minecraft.server.level.ServerPlayer sp) {
            BoosterManager.openBooster(sp);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
