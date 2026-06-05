package com.cardshop.item;

import com.cardshop.augment.Augment;
import com.cardshop.augment.AugmentRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Optional;

public class AugmentItem extends Item {

    public static final String NBT_ROOT       = "cardshop_augment";
    public static final String NBT_AUGMENT_ID = "augment_id";

    public AugmentItem() {
        super(new Properties().stacksTo(1).fireResistant());
    }

    public static ItemStack create(Augment aug) {
        ItemStack stack = new ItemStack(ModItems.AUGMENT.get());
        CompoundTag nbt = new CompoundTag();
        nbt.putString(NBT_AUGMENT_ID, aug.getId());
        stack.addTagElement(NBT_ROOT, nbt);
        stack.setHoverName(Component.literal(
            aug.getTierDisplay() + " \u00a7r" + aug.getDisplayName()));
        return stack;
    }

    public static Optional<Augment> getAugment(ItemStack stack) {
        if (!(stack.getItem() instanceof AugmentItem)) return Optional.empty();
        CompoundTag nbt = stack.getTagElement(NBT_ROOT);
        if (nbt == null) return Optional.empty();
        return AugmentRegistry.get(nbt.getString(NBT_AUGMENT_ID));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
            List<Component> lines, TooltipFlag flag) {
        getAugment(stack).ifPresent(aug -> {
            lines.add(Component.literal("\u00a78" + aug.getDescription()));
            lines.add(Component.literal(aug.getTierDisplay()));
            if (aug.hasEffect())
                lines.add(Component.literal("\u00a7aEffet : " + aug.getEffectId()));
            if (aug.hasCustomBonus())
                lines.add(Component.literal("\u00a7aBonus : +" +
                    (int)(aug.getCustomBonus()*100) + "% " + aug.getCustomType()));
        });
    }
}
