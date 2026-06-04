package com.cardshop.data;

import com.cardshop.CardShopMod;
import com.cardshop.augment.AugmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import java.util.*;

/**
 * Equivalent Forge de PersistentState.
 * Sauvegarde dans world/data/cardshop_players.dat
 */
public class PlayerDataManager extends SavedData {

    private static final String KEY = "cardshop_players";
    private final Map<UUID, PlayerData> map = new HashMap<>();

    // ── Acces singleton ─────────────────────────────────────────────

    public static PlayerDataManager get(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        DimensionDataStorage storage = overworld.getDataStorage();
        return storage.computeIfAbsent(
            PlayerDataManager::load,
            PlayerDataManager::new,
            KEY);
    }

    public PlayerData getOrCreate(UUID uuid) {
        return map.computeIfAbsent(uuid, id -> {
            setDirty();
            return new PlayerData();
        });
    }

    // ── Serialisation NBT ────────────────────────────────────────────

    @Override
    public CompoundTag save(CompoundTag nbt) {
        CompoundTag players = new CompoundTag();
        map.forEach((uuid, data) -> {
            CompoundTag p = new CompoundTag();
            p.putLong("balance", data.getBalance());
            CompoundTag aug = new CompoundTag();
            String[] slots = data.getEquippedAugments();
            for (int i = 0; i < AugmentSlot.COUNT; i++)
                aug.putString("s" + i, slots[i] != null ? slots[i] : "");
            p.put("augments", aug);
            players.put(uuid.toString(), p);
        });
        nbt.put("players", players);
        return nbt;
    }

    public static PlayerDataManager load(CompoundTag nbt) {
        PlayerDataManager mgr = new PlayerDataManager();
        CompoundTag players = nbt.getCompound("players");
        for (String key : players.getAllKeys()) {
            try {
                UUID uuid = UUID.fromString(key);
                CompoundTag p = players.getCompound(key);
                PlayerData data = new PlayerData();
                data.setBalance(p.getLong("balance"));
                CompoundTag aug = p.getCompound("augments");
                for (int i = 0; i < AugmentSlot.COUNT; i++)
                    data.setEquippedAugment(i, aug.getString("s" + i));
                mgr.map.put(uuid, data);
            } catch (Exception e) {
                CardShopMod.LOGGER.warn("[PlayerData] UUID invalide: {}", key);
            }
        }
        CardShopMod.LOGGER.info("[PlayerData] {} joueurs charges.", mgr.map.size());
        return mgr;
    }
}
