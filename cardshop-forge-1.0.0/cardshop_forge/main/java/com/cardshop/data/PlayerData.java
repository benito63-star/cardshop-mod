package com.cardshop.data;

import com.cardshop.augment.AugmentSlot;

public class PlayerData {
    public static final long STARTING_BALANCE = 100L;
    public static final long MAX_BALANCE       = 1_000_000L;

    private long     balance          = STARTING_BALANCE;
    private final String[] equippedAugments = new String[AugmentSlot.COUNT];

    public long    getBalance()                    { return balance; }
    public void    setBalance(long v)              { balance = Math.max(0, Math.min(MAX_BALANCE, v)); }
    public boolean canAfford(long v)               { return balance >= v; }
    public String[] getEquippedAugments()          { return equippedAugments; }

    public boolean deduct(long amount) {
        if (amount <= 0 || balance < amount) return false;
        balance -= amount; return true;
    }

    public boolean add(long amount) {
        if (amount <= 0) return false;
        long next = balance + amount;
        if (next > MAX_BALANCE) return false;
        balance = next; return true;
    }

    public void setEquippedAugment(int slot, String id) {
        if (slot >= 0 && slot < AugmentSlot.COUNT)
            equippedAugments[slot] = id != null ? id : "";
    }
}
