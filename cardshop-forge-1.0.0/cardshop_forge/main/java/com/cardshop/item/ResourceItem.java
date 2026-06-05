package com.cardshop.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class ResourceItem extends Item {

    public enum ResourceRarity {
        BASIC("\u00a77","Basique"), UNCOMMON("\u00a7a","Peu commun"),
        RARE("\u00a79","Rare"),     EXOTIC("\u00a75","Exotique"),
        MYTHIC("\u00a76","Mythique");
        public final String color, label;
        ResourceRarity(String c, String l) { color = c; label = l; }
    }

    private final String         resourceName;
    private final ResourceRarity rarity;
    private final String         obtainHint;
    private final long           baseValue;

    public ResourceItem(String name, ResourceRarity rarity,
                         String hint, long value) {
        super(new Properties().stacksTo(64));
        this.resourceName = name;
        this.rarity       = rarity;
        this.obtainHint   = hint;
        this.baseValue    = value;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
            List<Component> lines, TooltipFlag flag) {
        lines.add(Component.literal(rarity.color + rarity.label));
        lines.add(Component.literal("\u00a78Obtention : \u00a77" + obtainHint));
        lines.add(Component.literal("\u00a76Valeur : " + baseValue + " Coins"));
    }
}
