package com.cardshop.card;

import com.cardshop.CardShopMod;
import java.util.*;

public class CardRegistry {

    private static final Map<String, Card>              cards     = new LinkedHashMap<>();
    private static final Map<CardRarity, List<Card>>    byRarity  = new EnumMap<>(CardRarity.class);

    static {
        for (CardRarity r : CardRarity.values()) byRarity.put(r, new ArrayList<>());
    }

    public static void register(Card card) {
        if (cards.containsKey(card.getId())) return;
        cards.put(card.getId(), card);
        byRarity.get(card.getRarity()).add(card);
    }

    public static Optional<Card> get(String id) {
        return Optional.ofNullable(cards.get(id));
    }

    public static List<Card> getByRarity(CardRarity r) {
        return Collections.unmodifiableList(byRarity.get(r));
    }

    public static Card getRandomCard(Random rand) {
        float roll = rand.nextFloat();
        CardRarity rarity;
        if      (roll < 0.60f) rarity = CardRarity.COMMON;
        else if (roll < 0.85f) rarity = CardRarity.RARE;
        else if (roll < 0.97f) rarity = CardRarity.EPIC;
        else                   rarity = CardRarity.LEGENDARY;
        List<Card> pool = byRarity.get(rarity);
        if (pool.isEmpty()) pool = byRarity.get(CardRarity.COMMON);
        return pool.get(rand.nextInt(pool.size()));
    }

    public static int getTotalCount() { return cards.size(); }
}
