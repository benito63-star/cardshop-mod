package com.cardshop.augment;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;

public class Augment {

    public enum Category { COMBAT, DROPS, TRADE, EXPLORATION, SPECIAL }

    private final String   id, displayName, description;
    private final Category category;
    private final int      tier, effectAmplifier;
    private final long     craftCost;
    private final String   effectId, customType;
    private final float    customBonus;

    public Augment(String id, String displayName, Category category,
                   int tier, long craftCost, String description,
                   String effectId, int effectAmplifier,
                   float customBonus, String customType) {
        this.id              = id;
        this.displayName     = displayName;
        this.category        = category;
        this.tier            = tier;
        this.craftCost       = craftCost;
        this.description     = description;
        this.effectId        = effectId != null ? effectId : "";
        this.effectAmplifier = effectAmplifier;
        this.customBonus     = customBonus;
        this.customType      = customType != null ? customType : "";
    }

    public String   getId()              { return id; }
    public String   getDisplayName()     { return displayName; }
    public Category getCategory()        { return category; }
    public int      getTier()            { return tier; }
    public long     getCraftCost()       { return craftCost; }
    public String   getDescription()     { return description; }
    public String   getEffectId()        { return effectId; }
    public int      getEffectAmplifier() { return effectAmplifier; }
    public float    getCustomBonus()     { return customBonus; }
    public String   getCustomType()      { return customType; }
    public boolean  hasEffect()          { return !effectId.isEmpty(); }
    public boolean  hasCustomBonus()     { return !customType.isEmpty(); }

    public MobEffect getMobEffect() {
        if (!hasEffect()) return null;
        return ForgeRegistries.MOB_EFFECTS.getValue(
            new ResourceLocation(effectId));
    }

    public String getTierDisplay() {
        return switch (tier) {
            case 1 -> "\u00a76Bronze";
            case 2 -> "\u00a77Argent";
            case 3 -> "\u00a7eOr";
            default -> "\u00a7fBasique";
        };
    }
}
