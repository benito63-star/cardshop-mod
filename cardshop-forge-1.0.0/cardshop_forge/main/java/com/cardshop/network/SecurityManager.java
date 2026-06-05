package com.cardshop.network;

import com.cardshop.CardShopMod;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityManager {
    private static final int  MAX_REQ = 5;
    private static final long WIN_MS  = 1000L;
    private static final Map<UUID, long[]> trackers = new ConcurrentHashMap<>();

    public static boolean checkRateLimit(ServerPlayer player) {
        UUID uuid = player.getUUID();
        long now  = System.currentTimeMillis();
        long[] t  = trackers.computeIfAbsent(uuid, k -> new long[]{now, 0});
        synchronized (t) {
            if (now - t[0] > WIN_MS) { t[0] = now; t[1] = 0; }
            t[1]++;
            if (t[1] > MAX_REQ) {
                CardShopMod.LOGGER.warn("[Security] Rate limit: {}",
                    player.getName().getString());
                return false;
            }
        }
        return true;
    }

    public static boolean isValidAmount(long v)     { return v > 0 && v <= 1_000_000L; }
    public static boolean isValidSlot(int s, int m) { return s >= 0 && s < m; }
    public static boolean isValidId(String id) {
        return id != null && !id.isEmpty() && id.length() <= 64
            && id.matches("[a-z0-9_]+");
    }
    public static void cleanup(UUID uuid) { trackers.remove(uuid); }
}
