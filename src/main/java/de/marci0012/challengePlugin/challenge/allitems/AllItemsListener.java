package de.marci0012.challengePlugin.challenge.allitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class AllItemsListener implements Listener {

    private final AllItemsChallenge challenge;

    public AllItemsListener(AllItemsChallenge challenge) {
        this.challenge = challenge;
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Material material = event.getItem().getItemStack().getType();

        if (!material.isItem()) return; // nur echte Items
        if (!challenge.isActive()) return; // nur wenn Challenge läuft

        // Item hinzufügen
        if (challenge.addItem(player, material)) { // true = neu hinzugefügt
            // Nachricht für alle Spieler
            String message = "§a" + player.getName() + " hat das Item §6" + material.name() + " §agefunden!";
            for (Player p : player.getServer().getOnlinePlayers()) {
                p.sendMessage(message);
            }
        }
    }
}