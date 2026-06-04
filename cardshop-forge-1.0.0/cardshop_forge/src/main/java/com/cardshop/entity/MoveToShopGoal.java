package com.cardshop.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public class MoveToShopGoal extends Goal {

    private final CardMerchantEntity entity;
    private int stuck = 0;

    public MoveToShopGoal(CardMerchantEntity e) {
        this.entity = e;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos t = entity.getTargetShop();
        if (t == null) return false;
        return entity.position()
            .distanceTo(Vec3.atCenterOf(t)) > 2.5;
    }

    @Override
    public void tick() {
        BlockPos t = entity.getTargetShop();
        if (t == null) return;
        entity.getNavigation().moveTo(
            t.getX()+0.5, t.getY(), t.getZ()+0.5, 0.7);
        if (++stuck > 200) entity.discard();
    }

    @Override
    public boolean shouldKeepRunning() { return canUse(); }
}
