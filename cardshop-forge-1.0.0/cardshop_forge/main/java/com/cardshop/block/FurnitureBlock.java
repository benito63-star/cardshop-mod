package com.cardshop.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FurnitureBlock extends Block {

    private final String furnitureName;
    private final int    rankPoints;

    public FurnitureBlock(String name, int pts) {
        super(Properties.of().strength(1.5f, 3f).sound(SoundType.WOOD));
        this.furnitureName = name;
        this.rankPoints    = pts;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            player.displayClientMessage(Component.literal(
                "\u00a77[Meuble] \u00a7e+" + rankPoints + " pts de rang"), true);
        return InteractionResult.SUCCESS;
    }

    public int getRankPoints() { return rankPoints; }
}
