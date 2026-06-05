package com.cardshop.network;

import com.cardshop.augment.AugmentManager;
import com.cardshop.augment.AugmentSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.handling.PlayPayloadContext;

public class EquipAugmentPacket {
    private final int    slot;
    private final String augmentId;
    private final boolean unequip;

    public EquipAugmentPacket(int slot, String augmentId, boolean unequip) {
        this.slot = slot; this.augmentId = augmentId; this.unequip = unequip;
    }

    public static void encode(EquipAugmentPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.slot);
        buf.writeUtf(pkt.augmentId);
        buf.writeBoolean(pkt.unequip);
    }

    public static EquipAugmentPacket decode(FriendlyByteBuf buf) {
        return new EquipAugmentPacket(buf.readInt(), buf.readUtf(), buf.readBoolean());
    }

    public static void handle(EquipAugmentPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player().orElse(null);
            if (player == null) return;
            if (!SecurityManager.checkRateLimit(player)) return;
            if (!SecurityManager.isValidSlot(pkt.slot, AugmentSlot.COUNT)) return;
            if (!SecurityManager.isValidId(pkt.augmentId)) return;
            if (pkt.unequip) AugmentManager.unequip(player, pkt.slot);
            else             AugmentManager.equip(player, pkt.augmentId, pkt.slot);
        });
    }
}
