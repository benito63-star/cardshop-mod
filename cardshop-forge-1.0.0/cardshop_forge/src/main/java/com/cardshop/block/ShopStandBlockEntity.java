package com.cardshop.block;

import com.cardshop.CardShopMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShopStandBlockEntity extends BlockEntity {

    private String ownerUuid  = "";
    private String ownerName  = "Inconnu";
    private int    currentRank = 0;
    private long   totalSales  = 0L;

    public ShopStandBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.SHOP_STAND_BE.get(), pos, state);
    }

    public void refreshRank(ServerLevel world) {
        int r = ShopRankManager.calculateRank(world, worldPosition);
        if (r != currentRank) { currentRank = r; setChanged(); }
    }

    public void recordSale()  { totalSales++; setChanged(); }

    public String getOwnerUuid()   { return ownerUuid; }
    public String getOwnerName()   { return ownerName; }
    public int    getCurrentRank() { return currentRank; }
    public long   getTotalSales()  { return totalSales; }

    public void setOwner(String uuid, String name) {
        ownerUuid = uuid; ownerName = name; setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putString("ownerUuid",   ownerUuid);
        nbt.putString("ownerName",   ownerName);
        nbt.putInt   ("currentRank", currentRank);
        nbt.putLong  ("totalSales",  totalSales);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ownerUuid   = nbt.getString("ownerUuid");
        ownerName   = nbt.getString("ownerName");
        currentRank = nbt.getInt   ("currentRank");
        totalSales  = nbt.getLong  ("totalSales");
    }
}
