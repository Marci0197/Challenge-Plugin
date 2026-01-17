package de.marci0012.challengePlugin.challenge.allitems;

import de.marci0012.challengePlugin.Main;
import de.marci0012.challengePlugin.timer.TimerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AllItemsChallenge {

    private final TimerManager timerManager;
    private final Set<Material> foundItems = EnumSet.noneOf(Material.class);
    private final Set<Material> allValidItems;
    private final AllItemsBossBar bossBar;

    public AllItemsChallenge(TimerManager timerManager, Main plugin) {
        this.timerManager = timerManager;

        // Alle Items, die gesammelt werden können
        Set<Material> blockedItems = Set.of(
                Material.LIGHT,
                Material.PLAYER_HEAD,
                Material.STRUCTURE_VOID,
                Material.SUSPICIOUS_GRAVEL,
                Material.SUSPICIOUS_SAND,
                Material.SPAWNER,
                Material.JIGSAW,
                Material.KNOWLEDGE_BOOK,
                Material.FROGSPAWN,
                Material.DEBUG_STICK,
                Material.BEDROCK
        );

        // Alle gültigen Items für Survival, ohne blockierte
        this.allValidItems = EnumSet.allOf(Material.class).stream()
                .filter(Material::isItem)
                .filter(m -> !blockedItems.contains(m))
                .collect(Collectors.toSet());

        // Optional: Items laden, die vorher gespeichert wurden
        this.foundItems.addAll(plugin.getAllItemsChallenge() == null ? Set.of() : plugin.getAllItemsChallenge().getFoundItems());

        this.bossBar = new AllItemsBossBar(this, plugin);
        bossBar.update();
    }

    public boolean isActive() {
        return timerManager.isRunning();
    }

    /**
     * Fügt ein Item hinzu, wenn es gültig ist.
     * Gibt direkt im Chat aus, welches Item gesammelt wurde.
     */
    public boolean addItem(Player player, Material material) {
        if (!isActive() || !allValidItems.contains(material)) return false;

        if (foundItems.add(material)) {
            Bukkit.broadcastMessage("§aAll Items: §e" + player.getName() + " hat §6" + material.name() + " §agesammelt!");
            bossBar.update();

            // Challenge abgeschlossen prüfen
            if (foundItems.size() >= allValidItems.size()) {
                Bukkit.broadcastMessage("§6§lAlle Items gefunden! Challenge abgeschlossen!");
                timerManager.stopTimer();
            }

            return true;
        }

        return false;
    }

    public Set<Material> getFoundItems() {
        return foundItems;
    }

    public Set<Material> getMissingItems() {
        Set<Material> missing = EnumSet.noneOf(Material.class);
        for (Material m : allValidItems) {
            if (!foundItems.contains(m)) missing.add(m);
        }
        return missing;
    }

    public int getTotalItems() {
        return allValidItems.size();
    }

    public int getFoundCount() {
        return foundItems.size();
    }

    public void resetAllItems() {
        foundItems.clear();
        bossBar.update();
        Bukkit.broadcastMessage("§cAll Items Challenge wurde zurückgesetzt!");
    }

    public Set<Material> getAllValidItems() {
        return allValidItems;
    }
}