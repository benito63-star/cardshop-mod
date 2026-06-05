package com.cardshop;

import com.cardshop.augment.AugmentManager;
import com.cardshop.economy.EconomyManager;
import com.cardshop.entity.ShopVisitorManager;
import com.cardshop.network.SecurityManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CardShopMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        AugmentManager.onServerTick(event.getServer());
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.level instanceof ServerLevel sl)
            ShopVisitorManager.onServerTick(sl);
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof
                net.minecraft.server.level.ServerPlayer sp)
            EconomyManager.onPlayerJoin(sp);
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        SecurityManager.cleanup(event.getEntity().getUUID());
    }
}
