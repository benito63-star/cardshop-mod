package com.cardshop.network;

import com.cardshop.block.ShopStandMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.handling.PlayPayloadContext;

public class SellCardsPacket {

    public static void encode(SellCardsPacket pkt, FriendlyByteBuf buf) {}
    public static SellCardsPacket decode(FriendlyByteBuf buf) {
        return new SellCardsPacket();
    }

    public static void handle(SellCardsPacket pkt, PlayPayloadContext ctx) {
        ctx.workHandler().execute(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player().orElse(null);
            if (player == null) return;
            if (!SecurityManager.checkRateLimit(player)) return;
            if (!(player.containerMenu instanceof ShopStandMenu menu)) return;
            menu.sellAllCards(player);
        });
    }
}
