package com.cardshop.booster;

import com.cardshop.card.Card;
import com.cardshop.card.CardRegistry;
import com.cardshop.item.CardItem;
import com.cardshop.item.ModItems;
import com.cardshop.network.ModNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import java.util.*;

public class BoosterManager {

    private static final int MIN_CARDS = 3;
    private static final int MAX_CARDS = 5;
    private static final Random RAND   = new Random();

    public static void openBooster(ServerPlayer player) {
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof
                com.cardshop.item.BoosterPackItem)) return;

        // ANTI-DUPE : retirer avant de generer
        held.shrink(1);

        int count = MIN_CARDS + RAND.nextInt(MAX_CARDS - MIN_CARDS + 1);
        List<Card> drawn = new ArrayList<>();

        for (int i = 0; i < count; i++)
            drawn.add(CardRegistry.getRandomCard(RAND));

        for (Card card : drawn) {
            ItemStack stack = CardItem.create(card);
            if (!player.getInventory().add(stack))
                player.drop(stack, false);
        }

        ModNetwork.sendBoosterResult(player, drawn);
    }
}
