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
    private final Set<Material> validItems; // alle erlaubten Items
    private final AllItemsBossBar bossBar;
    private final AllItemsStorage storage;

    public AllItemsChallenge(TimerManager timerManager, Main plugin) {
        this.timerManager = timerManager;

        // Alle Items ermitteln, die in der Challenge erlaubt sind
        validItems = EnumSet.allOf(Material.class).stream()
                .filter(Material::isItem)
                .filter(this::isAllowed)
                .collect(Collectors.toSet());

        storage = new AllItemsStorage(plugin);
        foundItems.addAll(storage.load().stream()
                .filter(validItems::contains)
                .collect(Collectors.toSet()));

        bossBar = new AllItemsBossBar(this, plugin);
        bossBar.update();
    }

    // Prüft, ob ein Item erlaubt ist
    private boolean isAllowed(Material m) {
        // Unerwünschte Items
        Set<Material> blocked = Set.of(
                Material.LIGHT,
                Material.PLAYER_HEAD,
                // Material.VAULT,           // falls eigenes Item, sonst weglassen
                // Material.TRIAL_SPAWNER,   // eigenes Test-Item
                // Material.TEST_BLOCK,      // eigenes Test-Item
                Material.STRUCTURE_VOID,
                Material.SUSPICIOUS_GRAVEL,
                Material.SUSPICIOUS_SAND,
                Material.SPAWNER,           // korrekt für Monster Spawner
                Material.JIGSAW,
                Material.KNOWLEDGE_BOOK,
                Material.FROGSPAWN,
                Material.DEBUG_STICK,
                Material.BEDROCK
        );
        return !blocked.contains(m);
    }

    // Prüfen, ob Challenge läuft
    public boolean isActive() {
        return timerManager.isRunning();
    }

    // Item hinzufügen, falls noch nicht gefunden
    public boolean addItem(Material material) {
        if (!isActive() || !validItems.contains(material)) return false;

        if (foundItems.add(material)) {
            storage.save(foundItems);
            bossBar.update();

            if (foundItems.size() >= validItems.size()) {
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
        return validItems.stream()
                .filter(m -> !foundItems.contains(m))
                .collect(Collectors.toSet());
    }

    public int getTotalItems() {
        return validItems.size();
    }

    public int getFoundCount() {
        return foundItems.size();
    }

    // Alle Items zurücksetzen
    public void resetAllItems() {
        foundItems.clear();
        storage.save(foundItems);
        bossBar.update();
    }
}
