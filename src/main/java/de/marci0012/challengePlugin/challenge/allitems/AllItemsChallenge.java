package de.marci0012.challengePlugin.challenge.allitems;

import de.marci0012.challengePlugin.Main;
import de.marci0012.challengePlugin.timer.TimerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

public class AllItemsChallenge {

    private final TimerManager timerManager;
    private final Set<Material> foundItems = EnumSet.noneOf(Material.class);
    private final int totalItems;

    private final AllItemsBossBar bossBar;
    private final AllItemsStorage storage;

    public AllItemsChallenge(TimerManager timerManager, Main plugin) {
        this.timerManager = timerManager;
        this.totalItems = (int) EnumSet.allOf(Material.class)
                .stream().filter(Material::isItem).count();

        this.storage = new AllItemsStorage(plugin);
        foundItems.addAll(storage.load());

        this.bossBar = new AllItemsBossBar(this, plugin);
        bossBar.update();
    }

    public boolean isActive() {
        return timerManager.isRunning();
    }

    public boolean addItem(Material material) {
        if (!isActive() || !material.isItem()) return false;

        if (foundItems.add(material)) {
            storage.save(foundItems);
            bossBar.update();

            if (foundItems.size() >= totalItems) {
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
        for (Material m : Material.values()) {
            if (m.isItem() && !foundItems.contains(m)) {
                missing.add(m);
            }
        }
        return missing;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getFoundCount() {
        return foundItems.size();
    }

    public void resetAllItems() {
        foundItems.clear(); // Alle gefundenen Items zurücksetzen

        // Speicher aktualisieren
        storage.save(foundItems);

        // BossBar für alle Spieler aktualisieren
        bossBar.update();
    }


}
