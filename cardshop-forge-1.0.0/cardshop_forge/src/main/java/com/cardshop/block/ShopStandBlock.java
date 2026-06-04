package com.cardshop.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ShopStandBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING =
        BlockStateProperties.HORIZONTAL_FACING;

    public ShopStandBlock() {
        super(Properties.of()
            .strength(2f, 6f)
            .sound(SoundType.WOOD));
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState()
            .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
            Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof ShopStandBlockEntity be))
            return InteractionResult.FAIL;

        ServerPlayer sp = (ServerPlayer) player;
        be.refreshRank((ServerLevel) level);

        if (be.getCurrentRank() == 0) {
            player.displayClientMessage(Component.literal(
                "\u00a7cBoutique fermee ! Ajoutez des meubles."), true);
            return InteractionResult.FAIL;
        }

        sp.openMenu(new ShopStandMenuProvider(pos, be));
        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
            @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide && placer instanceof ServerPlayer sp) {
            if (level.getBlockEntity(pos) instanceof ShopStandBlockEntity be) {
                be.setOwner(sp.getUUID().toString(), sp.getName().getString());
                sp.displayClientMessage(Component.literal(
                    "\u00a7aBoutique creee ! Placez des meubles pour monter en rang."), true);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShopStandBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }
}
