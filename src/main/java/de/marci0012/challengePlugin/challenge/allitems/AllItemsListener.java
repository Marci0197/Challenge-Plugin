package de.marci0012.challengePlugin.challenge.allitems;

import org.bukkit.Material;
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
        Material material = event.getItem().getItemStack().getType();

        if (!material.isItem()) return; // nur Items
        if (!challenge.isActive()) return;

        if (challenge.addItem(material)) { // true = neu hinzugefügt
            event.getPlayer().sendMessage("§aDu hast ein neues Item gefunden: §6" + material.name());
        }
    }
}
