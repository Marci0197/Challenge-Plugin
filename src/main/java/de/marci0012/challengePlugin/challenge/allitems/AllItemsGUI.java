package de.marci0012.challengePlugin.challenge.allitems;

import de.marci0012.challengePlugin.Main;
import de.marci0012.challengePlugin.timer.TimerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AllItemsChallenge {

    private final TimerManager timerManager;
    private final Set<Material> foundItems = EnumSet.noneOf(Material.class);
    private final AllItemsBossBar bossBar;
    private final AllItemsStorage storage;

    // Unerwünschte Items
    private static final Set<Material> excludedItems = Set.of(
            Material.LIGHT,
            Material.PLAYER_HEAD,
            Material.VAULT,
            Material.TRIAL_SPAWNER,
            Material.TEST_BLOCK,
            Material.TEST_INSTANCES_BLOCK,
            Material.STRUCTURE_VOID,
            Material.SUSPICIOUS_GRAVEL,
            Material.SUSPICIOUS_SAND,
            Material.MONSTER_SPAWNER,
            Material.JIGSAW_BLOCK,
            Material.KNOWLEDGE_BOOK,
            Material.FROGSPAWN,
            Material.DEBUG_STICK,
            Material.BEDROCK
    );

    private final Set<Material> allValidItems;

    public AllItemsChallenge(TimerManager timerManager, Main plugin) {
        this.timerManager = timerManager;

        // Alle gültigen Items
        allValidItems = EnumSet.allOf(Material.class).stream()
                .filter(Material::isItem)
                .filter(m -> !excludedItems.contains(m))
                .collect(Collectors.toSet());

        this.storage = new AllItemsStorage(plugin);
        foundItems.addAll(storage.load().stream()
                .filter(allValidItems::contains)
                .collect(Collectors.toSet()));

        this.bossBar = new AllItemsBossBar(this, plugin);
        bossBar.update();
    }

    public boolean isActive() {
        return timerManager.isRunning();
    }

    public boolean addItem(Material material) {
        if (!isActive() || !allValidItems.contains(material)) return false;

        if (foundItems.add(material)) {
            storage.save(foundItems);
            bossBar.update();

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
        Set<Material> missing = EnumSet.copyOf(allValidItems);
        missing.removeAll(foundItems);
        return missing;
    }

    public Set<Material> getAllValidItems() {
        return allValidItems;
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
    }
}
