package com.cardshop.economy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientEconomyCache {
    private static long balance = 0L;
    public static long   getBalance()          { return balance; }
    public static void   setBalance(long v)    { balance = v; }
    public static String getFormatted()        { return balance + " Coins"; }
}
