package com.cardshop.screen;

import com.cardshop.card.CardRarity;
import com.cardshop.network.BoosterResultPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BoosterOpenScreen extends Screen {

    private final List<BoosterResultPacket.CardInfo> cards;
    private int     current = 0;
    private int     timer   = 0;
    private boolean done    = false;

    public BoosterOpenScreen(List<BoosterResultPacket.CardInfo> cards) {
        super(Component.literal("Booster Pack"));
        this.cards = cards;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(
            Component.literal("Suivant"),
            btn -> next())
            .bounds(width/2 - 50, height - 50, 100, 20)
            .build());
    }

    private void next() {
        if (done) { onClose(); return; }
        if (current < cards.size() - 1) current++;
        else done = true;
    }

    @Override
    public void tick() {
        timer++;
        if (timer % 20 == 0 && !done) next();
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        // Forge 1.20.4 : renderBackground prend 5 args
        renderBackground(g, mx, my, delta);
        super.render(g, mx, my, delta);

        int cx = width / 2;
        g.drawCenteredString(font, "Booster Pack Ouvert !", cx, 20, 0xFFFFFF);
        g.drawCenteredString(font,
            "Carte " + (current+1) + " / " + cards.size(), cx, 35, 0xAAAAAA);

        if (!cards.isEmpty()) {
            var card   = cards.get(current);
            var rarity = CardRarity.fromString(card.rarityId());
            int cw = 120, ch = 160, cx2 = cx - cw/2, cy = height/2 - ch/2;
            g.fill(cx2-2, cy-2, cx2+cw+2, cy+ch+2, 0xFF000000 | rarity.color);
            g.fill(cx2, cy, cx2+cw, cy+ch, 0xFFEEE8D0);
            g.drawCenteredString(font, card.name(),    cx, cy+20, 0x222222);
            g.drawCenteredString(font, rarity.getDisplayName(), cx, cy+35, rarity.color);
            g.drawCenteredString(font, card.value()+" Coins", cx, cy+ch-20, 0xFFAA00);
        }
    }

    @Override public boolean isPauseScreen() { return false; }
}
