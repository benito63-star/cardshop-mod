package com.cardshop.network;

import com.cardshop.block.ShopStandMenu;
import com.cardshop.network.SecurityManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SellCardsPacket {

    public static void encode(SellCardsPacket pkt, FriendlyByteBuf buf) {}
    public static SellCardsPacket decode(FriendlyByteBuf buf) { return new SellCardsPacket(); }

    public static void handle(SellCardsPacket pkt,
            Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            if (!SecurityManager.checkRateLimit(player)) return;
            if (!(player.containerMenu instanceof ShopStandMenu menu)) return;
            menu.sellAllCards(player);
        });
        ctx.get().setPacketHandled(true);
    }
}
