package com.cardshop.entity;

import com.cardshop.block.ModBlocks;
import com.cardshop.block.ShopStandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import java.util.Random;

public class ShopVisitorManager {

    private static int tick = 0;
    private static final int INTERVAL  = 600;
    private static final int MAX_VISIT = 3;
    private static final int RADIUS    = 12;
    private static final Random RAND   = new Random();

    public static void onServerTick(ServerLevel world) {
        tick++;
        if (tick % INTERVAL != 0) return;

        // Forge 1.20.4 : itérer via les block entities chargés
        world.getBlockEntities().forEach(be -> {
            if (be instanceof ShopStandBlockEntity shopBE)
                trySpawn(world, be.getBlockPos(), shopBE);
        });
    }

    private static void trySpawn(ServerLevel world,
            BlockPos pos, ShopStandBlockEntity be) {

        int rank = be.getCurrentRank();
        if (rank == 0) return;

        AABB area = AABB.ofSize(
            net.minecraft.world.phys.Vec3.atCenterOf(pos),
            RADIUS*2, 6, RADIUS*2);

        long count = world.getEntitiesOfClass(
            CardMerchantEntity.class, area).size();
        if (count >= MAX_VISIT) return;

        float chance = switch (rank) {
            case 1 -> 0.30f; case 2 -> 0.50f; case 3 -> 0.65f;
            case 4 -> 0.80f; case 5 -> 1.00f; default -> 0f;
        };
        if (RAND.nextFloat() > chance) return;

        BlockPos spawn = findGround(world, pos);
        if (spawn == null) return;

        CardMerchantEntity merchant = new CardMerchantEntity(
            ModEntities.CARD_MERCHANT.get(), world);
        merchant.setTargetShop(pos);
        merchant.setRankBudget(rank);
        merchant.moveTo(spawn.getX()+0.5, spawn.getY(),
            spawn.getZ()+0.5, RAND.nextFloat()*360f, 0f);
        world.addFreshEntity(merchant);
    }

    private static BlockPos findGround(ServerLevel world, BlockPos origin) {
        for (int a = 0; a < 10; a++) {
            int dx = RAND.nextInt(RADIUS*2) - RADIUS;
            int dz = RAND.nextInt(RADIUS*2) - RADIUS;
            BlockPos c = origin.offset(dx, 0, dz);
            for (int dy = 3; dy >= -3; dy--) {
                BlockPos g = c.offset(0, dy, 0);
                if (world.getBlockState(g).isSolidRender(world, g)
                        && world.isEmptyBlock(g.above())
                        && world.isEmptyBlock(g.above(2)))
                    return g.above();
            }
        }
        return null;
    }
}
