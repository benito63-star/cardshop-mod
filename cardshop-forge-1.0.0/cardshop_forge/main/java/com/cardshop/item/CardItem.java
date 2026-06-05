package com.cardshop.item;

import com.cardshop.card.Card;
import com.cardshop.card.CardRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;

public class CardItem extends Item {

    public static final String NBT_ROOT    = "cardshop";
    public static final String NBT_CARD_ID = "card_id";

    public CardItem() {
        super(new Properties().stacksTo(1).fireResistant());
    }

    public static ItemStack create(Card card) {
        ItemStack stack = new ItemStack(ModItems.CARD.get());
        CompoundTag nbt = new CompoundTag();
        nbt.putString(NBT_CARD_ID, card.getId());
        stack.addTagElement(NBT_ROOT, nbt);
        stack.setHoverName(Component.literal(
            card.getRarity().getDisplayName() + " \u00a7r" + card.getDisplayName()));
        return stack;
    }

    public static Optional<Card> getCard(ItemStack stack) {
        if (!(stack.getItem() instanceof CardItem)) return Optional.empty();
        CompoundTag nbt = stack.getTagElement(NBT_ROOT);
        if (nbt == null || !nbt.contains(NBT_CARD_ID)) return Optional.empty();
        return CardRegistry.get(nbt.getString(NBT_CARD_ID));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
            List<Component> lines, TooltipFlag flag) {
        getCard(stack).ifPresent(card -> {
            lines.add(Component.literal("\u00a78Theme : \u00a77" + card.getTheme()));
            lines.add(Component.literal(card.getRarity().getDisplayName()));
            lines.add(Component.literal("\u00a76Valeur : " + card.getSellValue() + " Coins"));
            if (!card.getDescription().isEmpty())
                lines.add(Component.literal("\u00a7o" + card.getDescription()));
        });
    }
}
