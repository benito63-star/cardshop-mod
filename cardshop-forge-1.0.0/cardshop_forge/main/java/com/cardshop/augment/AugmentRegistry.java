package com.cardshop.augment;

import java.util.*;

public class AugmentRegistry {
    private static final Map<String, Augment>              all       = new LinkedHashMap<>();
    private static final Map<Augment.Category, List<Augment>> byCat  = new EnumMap<>(Augment.Category.class);

    static { for (Augment.Category c : Augment.Category.values()) byCat.put(c, new ArrayList<>()); }

    public static void register(Augment a) {
        if (all.containsKey(a.getId())) return;
        all.put(a.getId(), a);
        byCat.get(a.getCategory()).add(a);
    }

    public static Optional<Augment> get(String id) { return Optional.ofNullable(all.get(id)); }
    public static Collection<Augment> getAll()      { return Collections.unmodifiableCollection(all.values()); }
    public static int getTotalCount()               { return all.size(); }
}
