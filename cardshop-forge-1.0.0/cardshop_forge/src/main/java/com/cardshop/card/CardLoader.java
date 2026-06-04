package com.cardshop.card;

import com.cardshop.CardShopMod;
import com.google.gson.*;
import java.io.*;

public class CardLoader {

    private static final Gson GSON = new Gson();
    private static final String[] FILES =
        {"monsters","animals","blocks","tools","cats","alcohol"};

    public static void loadAll() {
        for (String f : FILES)
            load("/data/cardshop/cards/" + f + ".json", f);
        CardShopMod.LOGGER.info("[CardLoader] {} cartes chargees.",
            CardRegistry.getTotalCount());
    }

    private static void load(String path, String theme) {
        try (InputStream is = CardLoader.class.getResourceAsStream(path)) {
            if (is == null) { CardShopMod.LOGGER.warn("[CardLoader] Manquant: {}", path); return; }
            JsonArray arr = GSON.fromJson(new InputStreamReader(is), JsonArray.class);
            for (JsonElement el : arr) {
                try {
                    JsonObject o   = el.getAsJsonObject();
                    String    tid  = o.has("theme") ? o.get("theme").getAsString() : theme;
                    CardRegistry.register(new Card(
                        o.get("id").getAsString(),
                        o.get("name").getAsString(),
                        tid,
                        CardRarity.fromString(o.get("rarity").getAsString()),
                        o.has("description") ? o.get("description").getAsString() : ""));
                } catch (Exception e) {
                    CardShopMod.LOGGER.warn("[CardLoader] Carte invalide: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            CardShopMod.LOGGER.error("[CardLoader] Erreur {}: {}", path, e.getMessage());
        }
    }
}
