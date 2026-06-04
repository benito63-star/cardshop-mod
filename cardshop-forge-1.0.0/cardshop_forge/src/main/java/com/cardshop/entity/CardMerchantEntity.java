package com.cardshop.entity;

import com.cardshop.economy.EconomyManager;
import com.cardshop.item.CardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.*;

public class CardMerchantEntity extends PathfinderMob {

    private long     budget     = 50L;
    private int      rankBudget = 1;
    private BlockPos targetShop = null;
    private int      timer      = 0;
    private static final int TIMEOUT = 1200;

    public CardMerchantEntity(EntityType<? extends PathfinderMob> type,
                               Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new MoveToShopGoal(this));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level().isClientSide) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer sp)) return InteractionResult.PASS;

        player.sendSystemMessage(Component.literal(
            "\u00a7e[Marchand] \u00a77Budget restant : \u00a76" + budget + " Coins"));

        long earned = tryBuy(sp);
        if (earned > 0)
            player.sendSystemMessage(Component.literal(
                "\u00a7aTransaction ! +\u00a76" + earned + " Coins"));
        else
            player.sendSystemMessage(Component.literal(
                "\u00a77Rien d interessant dans ton inventaire..."));

        return InteractionResult.CONSUME;
    }

    public long tryBuy(ServerPlayer player) {
        if (budget <= 0) return 0;
        long total = 0;

        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            if (budget <= 0) break;
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.isEmpty()) continue;

            Optional<com.cardshop.card.Card> cardOpt = CardItem.getCard(stack);
            if (cardOpt.isPresent()) {
                long pay = Math.min(cardOpt.get().getSellValue(), budget);
                EconomyManager.add(player, pay);
                budget -= pay;
                total  += pay;
                player.getInventory().removeItem(slot, 1);
                break;
            }
        }
        if (total > 0) timer = TIMEOUT;
        return total;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            timer++;
            if (timer >= TIMEOUT) discard();
        }
    }

    public void setTargetShop(BlockPos pos)  { targetShop = pos; }
    public BlockPos getTargetShop()          { return targetShop; }

    public void setRankBudget(int rank) {
        rankBudget = rank;
        budget = switch (rank) {
            case 1 -> 30  + new Random().nextInt(20);
            case 2 -> 60  + new Random().nextInt(40);
            case 3 -> 120 + new Random().nextInt(80);
            case 4 -> 250 + new Random().nextInt(150);
            case 5 -> 500 + new Random().nextInt(300);
            default -> 20;
        };
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putLong("budget", budget);
        nbt.putInt("rank",    rankBudget);
        nbt.putInt("timer",   timer);
        if (targetShop != null) {
            nbt.putInt("tx", targetShop.getX());
            nbt.putInt("ty", targetShop.getY());
            nbt.putInt("tz", targetShop.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        budget     = nbt.getLong("budget");
        rankBudget = nbt.getInt("rank");
        timer      = nbt.getInt("timer");
        if (nbt.contains("tx"))
            targetShop = new BlockPos(
                nbt.getInt("tx"), nbt.getInt("ty"), nbt.getInt("tz"));
    }
}
