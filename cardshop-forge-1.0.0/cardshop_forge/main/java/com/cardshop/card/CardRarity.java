package com.cardshop.card;

public enum CardRarity {

    COMMON   ("common",    0xAAAAAA, 10L),
    RARE     ("rare",      0x5555FF, 35L),
    EPIC     ("epic",      0xAA00AA, 100L),
    LEGENDARY("legendary", 0xFFAA00, 500L);

    public final String id;
    public final int    color;
    public final long   baseValue;

    CardRarity(String id, int color, long baseValue) {
        this.id        = id;
        this.color     = color;
        this.baseValue = baseValue;
    }

    public static CardRarity fromString(String s) {
        for (CardRarity r : values())
            if (r.id.equalsIgnoreCase(s)) return r;
        return COMMON;
    }

    public String getDisplayName() {
        return switch (this) {
            case COMMON    -> "\u00a77Commune";
            case RARE      -> "\u00a79Rare";
            case EPIC      -> "\u00a75Epique";
            case LEGENDARY -> "\u00a76Legendaire";
        };
    }

    public long computeValue(String theme) {
        return switch (theme) {
            case "alcohol","cats" -> (long)(baseValue * 1.5);
            case "monsters"       -> (long)(baseValue * 1.2);
            default               -> baseValue;
        };
    }
}
