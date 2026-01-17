package de.marci0012.challengePlugin.challenge.allitems;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class AllItemsGUI implements Listener {

    private static final int ITEMS_PER_PAGE = 45;
    private static final Map<Player, GUIPageInfo> pageMap = new HashMap<>();

    // Liste unerwünschter Items
    private static final Set<Material> excludedItems = Set.of(
            Material.LIGHT,
            Material.PLAYER_HEAD,
            Material.VAULT,
            Material.TRAPPED_CHEST,
            // Material.MONSTER_SPAWNER,
            // Material.JIGSAW_BLOCK,
            Material.KNOWLEDGE_BOOK,
            Material.FROGSPAWN,
            Material.DEBUG_STICK,
            Material.BEDROCK,
            Material.STRUCTURE_VOID,
            Material.SUSPICIOUS_GRAVEL,
            Material.SUSPICIOUS_SAND,
            Material.TEST_BLOCK
            // Material.TEST_INSTANCES_BLOCK
    );

    // Öffnet die "gefunden"-Items GUI
    public static void openFound(Player player, Set<Material> items, int page) {
        List<Material> sorted = filterValidItems(items);
        open(player, sorted, page, true);
    }

    // Öffnet die "fehlenden"-Items GUI
    public static void openMissing(Player player, Set<Material> items, int page) {
        List<Material> sorted = filterValidItems(items);
        open(player, sorted, page, false);
    }

    // Filtert Items für Survival und entfernt unerwünschte
    private static List<Material> filterValidItems(Collection<Material> items) {
        return items.stream()
                .filter(Material::isItem)
                .filter(m -> !m.name().contains("AIR"))
                .filter(m -> !excludedItems.contains(m))
                .sorted(Comparator.comparing(Material::name))
                .collect(Collectors.toList());
    }

    // Allgemeine Öffnungs-Methode
    private static void open(Player player, List<Material> items, int page, boolean found) {
        Inventory inv = Bukkit.createInventory(null, 54,
                found ? "Items gefunden Seite " + (page + 1)
                        : "Items noch nicht gefunden Seite " + (page + 1));

        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, items.size());

        // Items setzen
        for (int i = start; i < end; i++) {
            inv.setItem(i - start, new ItemStack(items.get(i)));
        }

        // Navigation
        if (page > 0) inv.setItem(45, createNavItem(Material.RED_STAINED_GLASS_PANE, "Vorherige Seite"));
        if (end < items.size()) inv.setItem(53, createNavItem(Material.GREEN_STAINED_GLASS_PANE, "Nächste Seite"));

        pageMap.put(player, new GUIPageInfo(new HashSet<>(items), page, found));
        player.openInventory(inv);
    }

    // Klickbare Glas-Panes
    private static ItemStack createNavItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.setUnbreakable(true);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static GUIPageInfo getPageInfo(Player player) {
        return pageMap.get(player);
    }

    // PageInfo-Klasse pro Spieler
    public static class GUIPageInfo {
        private final Set<Material> items;
        private int page;
        private final boolean found;

        public GUIPageInfo(Set<Material> items, int page, boolean found) {
            this.items = items;
            this.page = page;
            this.found = found;
        }

        public Set<Material> getItems() { return items; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public boolean isFound() { return found; }
    }
}
