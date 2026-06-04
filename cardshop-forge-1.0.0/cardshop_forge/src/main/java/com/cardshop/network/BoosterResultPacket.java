package com.cardshop.network;

import com.cardshop.card.Card;
import com.cardshop.screen.BoosterOpenScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.*;
import java.util.function.Supplier;

public class BoosterResultPacket {

    private final List<CardInfo> cards;

    public record CardInfo(String id, String rarityId,
                           String name, long value) {}

    public BoosterResultPacket(List<Card> cards) {
        this.cards = cards.stream()
            .map(c -> new CardInfo(c.getId(), c.getRarity().id,
                                   c.getDisplayName(), c.getSellValue()))
            .toList();
    }

    private BoosterResultPacket(List<CardInfo> cards, boolean raw) {
        this.cards = cards;
    }

    public static void encode(BoosterResultPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.cards.size());
        for (CardInfo c : pkt.cards) {
            buf.writeUtf(c.id());
            buf.writeUtf(c.rarityId());
            buf.writeUtf(c.name());
            buf.writeLong(c.value());
        }
    }

    public static BoosterResultPacket decode(FriendlyByteBuf buf) {
        int count = buf.readInt();
        List<CardInfo> list = new ArrayList<>();
        for (int i = 0; i < count; i++)
            list.add(new CardInfo(buf.readUtf(), buf.readUtf(),
                                  buf.readUtf(), buf.readLong()));
        return new BoosterResultPacket(list, true);
    }

    public static void handle(BoosterResultPacket pkt,
            Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                Minecraft.getInstance().setScreen(
                    new BoosterOpenScreen(pkt.cards))));
        ctx.get().setPacketHandled(true);
    }
}
