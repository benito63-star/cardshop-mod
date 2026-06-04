package com.cardshop.network;

import com.cardshop.CardShopMod;
import com.cardshop.economy.ClientEconomyCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModNetwork {

    private static final String VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(CardShopMod.MOD_ID, "main"),
        () -> VERSION,
        VERSION::equals,
        VERSION::equals
    );

    private static int id = 0;

    public static void register() {
        // S->C : sync balance
        registerMessage(SyncBalancePacket.class,
            SyncBalancePacket::encode,
            SyncBalancePacket::decode,
            SyncBalancePacket::handle,
            NetworkDirection.PLAY_TO_CLIENT);

        // C->S : vendre cartes
        registerMessage(SellCardsPacket.class,
            SellCardsPacket::encode,
            SellCardsPacket::decode,
            SellCardsPacket::handle,
            NetworkDirection.PLAY_TO_SERVER);

        // S->C : résultat booster
        registerMessage(BoosterResultPacket.class,
            BoosterResultPacket::encode,
            BoosterResultPacket::decode,
            BoosterResultPacket::handle,
            NetworkDirection.PLAY_TO_CLIENT);

        // C->S : équiper augment
        registerMessage(EquipAugmentPacket.class,
            EquipAugmentPacket::encode,
            EquipAugmentPacket::decode,
            EquipAugmentPacket::handle,
            NetworkDirection.PLAY_TO_SERVER);
    }

    private static <T> void registerMessage(
            Class<T> cls,
            BiConsumer<T, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, T> decoder,
            BiConsumer<T, Supplier<net.minecraftforge.network.NetworkEvent.Context>> handler,
            NetworkDirection dir) {
        CHANNEL.messageBuilder(cls, id++, dir)
            .encoder(encoder).decoder(decoder).consumerMainThread(handler).add();
    }

    // ── Helpers envoi ────────────────────────────────────────────────

    public static void sendBalanceSync(ServerPlayer player, long balance) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
            new SyncBalancePacket(balance));
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendBoosterResult(ServerPlayer player,
            java.util.List<com.cardshop.card.Card> cards) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
            new BoosterResultPacket(cards));
    }
}
