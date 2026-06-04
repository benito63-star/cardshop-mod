package com.cardshop.card;

public class Card {
    private final String     id;
    private final String     displayName;
    private final String     theme;
    private final CardRarity rarity;
    private final long       sellValue;
    private final String     description;

    public Card(String id, String displayName, String theme,
                CardRarity rarity, String description) {
        this.id          = id;
        this.displayName = displayName;
        this.theme       = theme;
        this.rarity      = rarity;
        this.sellValue   = rarity.computeValue(theme);
        this.description = description;
    }

    public String     getId()          { return id; }
    public String     getDisplayName() { return displayName; }
    public String     getTheme()       { return theme; }
    public CardRarity getRarity()      { return rarity; }
    public long       getSellValue()   { return sellValue; }
    public String     getDescription() { return description; }
}
