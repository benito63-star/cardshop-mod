package com.cardshop.economy;

import com.cardshop.CardShopMod;
import com.cardshop.data.PlayerData;
import com.cardshop.data.PlayerDataManager;
import com.cardshop.network.ModNetwork;
import net.minecraft.server.level.ServerPlayer;

public class EconomyManager {

    public static long getBalance(ServerPlayer player) {
        return PlayerDataManager.get(player.server)
            .getOrCreate(player.getUUID()).getBalance();
    }

    public static boolean deduct(ServerPlayer player, long amount) {
        PlayerDataManager mgr  = PlayerDataManager.get(player.server);
        PlayerData        data = mgr.getOrCreate(player.getUUID());
        boolean ok = data.deduct(amount);
        if (ok) { mgr.setDirty(); syncBalance(player); }
        return ok;
    }

    public static boolean add(ServerPlayer player, long amount) {
        PlayerDataManager mgr  = PlayerDataManager.get(player.server);
        PlayerData        data = mgr.getOrCreate(player.getUUID());
        boolean ok = data.add(amount);
        if (ok) { mgr.setDirty(); syncBalance(player); }
        return ok;
    }

    public static boolean canAfford(ServerPlayer player, long amount) {
        return PlayerDataManager.get(player.server)
            .getOrCreate(player.getUUID()).canAfford(amount);
    }

    public static void syncBalance(ServerPlayer player) {
        ModNetwork.sendBalanceSync(player, getBalance(player));
    }

    public static void onPlayerJoin(ServerPlayer player) {
        PlayerDataManager.get(player.server).getOrCreate(player.getUUID());
        syncBalance(player);
        CardShopMod.LOGGER.info("[Economy] {} connecte | Solde: {}",
            player.getName().getString(), getBalance(player));
    }
}
