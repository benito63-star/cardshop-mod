package com.cardshop.network;

import com.cardshop.economy.ClientEconomyCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SyncBalancePacket {
    private final long balance;

    public SyncBalancePacket(long balance) { this.balance = balance; }

    public static void encode(SyncBalancePacket pkt, FriendlyByteBuf buf) {
        buf.writeLong(pkt.balance);
    }

    public static SyncBalancePacket decode(FriendlyByteBuf buf) {
        return new SyncBalancePacket(buf.readLong());
    }

    public static void handle(SyncBalancePacket pkt,
            Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientEconomyCache.setBalance(pkt.balance)));
        ctx.get().setPacketHandled(true);
    }
}
