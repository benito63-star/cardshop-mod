package com.cardshop.augment;

import com.cardshop.CardShopMod;
import com.cardshop.data.PlayerData;
import com.cardshop.data.PlayerDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

public class AugmentManager {

    private static int tick = 0;

    public static void onServerTick(MinecraftServer server) {
        tick++;
        if (tick % 100 != 0) return;
        for (ServerPlayer player : server.getPlayerList().getPlayers())
            applyAugments(player);
    }

    public static void applyAugments(ServerPlayer player) {
        PlayerData data = PlayerDataManager.get(player.server)
            .getOrCreate(player.getUUID());
        for (String id : data.getEquippedAugments()) {
            if (id == null || id.isEmpty()) continue;
            AugmentRegistry.get(id).ifPresent(aug -> {
                if (aug.hasEffect() && aug.getMobEffect() != null)
                    player.addEffect(new MobEffectInstance(
                        aug.getMobEffect(), 200,
                        aug.getEffectAmplifier(), true, false, true));
            });
        }
    }

    public static boolean equip(ServerPlayer player,
                                  String augId, int slot) {
        if (!AugmentRegistry.get(augId).isPresent()) return false;
        PlayerDataManager mgr  = PlayerDataManager.get(player.server);
        PlayerData        data = mgr.getOrCreate(player.getUUID());
        data.setEquippedAugment(slot, augId);
        mgr.setDirty();
        applyAugments(player);
        player.displayClientMessage(
            net.minecraft.network.chat.Component.literal(
                "\u00a7aAugment equipe dans le slot " + (slot+1)), true);
        return true;
    }

    public static boolean unequip(ServerPlayer player, int slot) {
        PlayerDataManager mgr  = PlayerDataManager.get(player.server);
        PlayerData        data = mgr.getOrCreate(player.getUUID());
        String removed = data.getEquippedAugments()[slot];
        if (removed == null || removed.isEmpty()) return false;
        data.setEquippedAugment(slot, "");
        mgr.setDirty();
        removeEffectIfUnequipped(player, removed);
        return true;
    }

    private static void removeEffectIfUnequipped(
            ServerPlayer player, String augId) {
        PlayerData data = PlayerDataManager.get(player.server)
            .getOrCreate(player.getUUID());
        for (String id : data.getEquippedAugments())
            if (augId.equals(id)) return;
        AugmentRegistry.get(augId).ifPresent(aug -> {
            if (aug.hasEffect() && aug.getMobEffect() != null)
                player.removeEffect(aug.getMobEffect());
        });
    }

    public static float getSellBonus(ServerPlayer player) {
        return getBonus(player, "sell_bonus");
    }

    public static float getDropLuck(ServerPlayer player) {
        return getBonus(player, "drop_luck");
    }

    private static float getBonus(ServerPlayer player, String type) {
        PlayerData data = PlayerDataManager.get(player.server)
            .getOrCreate(player.getUUID());
        float total = 0f;
        for (String id : data.getEquippedAugments()) {
            if (id == null || id.isEmpty()) continue;
            total += AugmentRegistry.get(id)
                .filter(a -> type.equals(a.getCustomType()))
                .map(Augment::getCustomBonus).orElse(0f);
        }
        return total;
    }
}
