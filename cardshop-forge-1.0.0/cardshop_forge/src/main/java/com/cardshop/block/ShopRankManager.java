package com.cardshop.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

public class ShopRankManager {

    public static final int   SCAN_RADIUS   = 8;
    public static final int[] THRESHOLDS    = {0, 5, 15, 30, 50};
    public static final String[] RANK_NAMES =
        {"Ferme","Echoppe","Boutique","Magasin","Galerie","Empire"};

    public static int calculateRank(ServerLevel world, BlockPos pos) {
        int pts = 0;
        for (int dx = -SCAN_RADIUS; dx <= SCAN_RADIUS; dx++)
            for (int dy = -3; dy <= 3; dy++)
                for (int dz = -SCAN_RADIUS; dz <= SCAN_RADIUS; dz++)
                    pts += getPoints(world.getBlockState(
                        pos.offset(dx,dy,dz)).getBlock());
        return rankFromPoints(pts);
    }

    private static int getPoints(Block b) {
        if (b == ModBlocks.FURNITURE_COUNTER.get())  return 1;
        if (b == ModBlocks.FURNITURE_SHELF.get())    return 2;
        if (b == ModBlocks.FURNITURE_REGISTER.get()) return 3;
        if (b == ModBlocks.FURNITURE_DISPLAY.get())  return 2;
        if (b == ModBlocks.FURNITURE_SIGN.get())     return 1;
        if (b == ModBlocks.FURNITURE_FOUNTAIN.get()) return 1;
        return 0;
    }

    private static int rankFromPoints(int pts) {
        int r = 0;
        for (int i = 0; i < THRESHOLDS.length; i++)
            if (pts >= THRESHOLDS[i]) r = i;
        return r;
    }

    public static float getPriceMultiplier(int rank) {
        return switch (rank) {
            case 1 -> 1.0f; case 2 -> 1.15f; case 3 -> 1.30f;
            case 4 -> 1.50f; case 5 -> 2.0f; default -> 0f;
        };
    }

    public static String getRankName(int r) {
        return (r >= 0 && r < RANK_NAMES.length) ? RANK_NAMES[r] : "?";
    }

    public static int pointsForNextRank(int r) {
        return (r < THRESHOLDS.length - 1) ? THRESHOLDS[r + 1] : -1;
    }
}
