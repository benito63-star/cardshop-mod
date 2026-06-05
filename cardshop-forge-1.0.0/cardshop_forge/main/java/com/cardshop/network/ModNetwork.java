package com.cardshop.network;

import com.cardshop.CardShopMod;
import com.cardshop.card.Card;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ModNetwork {

    private static SimpleChannel CHANNEL;

    public static void register() {
        CHANNEL = ChannelBuilder
            .named(new ResourceLocation(CardShopMod.MOD_ID, "main"))
            .networkProtocolVersion(1)
            .simpleChannel();

        CHANNEL.messageBuilder(SyncBalancePacket.class)
            .encoder(SyncBalancePacket::encode)
            .decoder(SyncBalancePacket::decode)
            .consumerMainThread(SyncBalancePacket::handle)
            .add();

        CHANNEL.messageBuilder(SellCardsPacket.class)
            .encoder(SellCardsPacket::encode)
            .decoder(SellCardsPacket::decode)
            .consumerMainThread(SellCardsPacket::handle)
            .add();

        CHANNEL.messageBuilder(BoosterResultPacket.class)
            .encoder(BoosterResultPacket::encode)
            .decoder(BoosterResultPacket::decode)
            .consumerMainThread(BoosterResultPacket::handle)
            .add();

        CHANNEL.messageBuilder(EquipAugmentPacket.class)
            .encoder(EquipAugmentPacket::encode)
            .decoder(EquipAugmentPacket::decode)
            .consumerMainThread(EquipAugmentPacket::handle)
            .add();

        CardShopMod.LOGGER.info("[CardShop] Network OK.");
    }

    public static void sendBalanceSync(ServerPlayer player, long balance) {
        CHANNEL.send(new SyncBalancePacket(balance),
            PacketDistributor.PLAYER.with(player));
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendBoosterResult(ServerPlayer player,
            java.util.List<Card> cards) {
        CHANNEL.send(new BoosterResultPacket(cards),
            PacketDistributor.PLAYER.with(player));
    }
}
