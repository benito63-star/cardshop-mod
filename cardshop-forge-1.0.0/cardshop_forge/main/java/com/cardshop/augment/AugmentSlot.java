package com.cardshop.augment;

public enum AugmentSlot {
    SLOT_1(0), SLOT_2(1), SLOT_3(2), SLOT_4(3), SLOT_5(4);
    public final int index;
    AugmentSlot(int i) { this.index = i; }
    public static final int COUNT = 5;
}
