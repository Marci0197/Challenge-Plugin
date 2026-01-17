package de.marci0012.challengePlugin.challenge.allitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AllItemsGUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null || !event.getView().getTitle().startsWith("Items")) return;

        Player player = (Player) event.getWhoClicked();

        // Verhindere das Verschieben/Entfernen aller Items
        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        AllItemsGUI.GUIPageInfo pageInfo = AllItemsGUI.getPageInfo(player);
        if (pageInfo == null) return;

        int maxPage = (int) Math.ceil(pageInfo.getItems().size() / 45.0) - 1;

        // Nächste Seite
        if (event.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE) {
            int nextPage = Math.min(pageInfo.getPage() + 1, maxPage);
            pageInfo.setPage(nextPage);
            openPage(player, pageInfo);
        }

        // Vorherige Seite
        if (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
            int prevPage = Math.max(pageInfo.getPage() - 1, 0);
            pageInfo.setPage(prevPage);
            openPage(player, pageInfo);
        }
    }

    private void openPage(Player player, AllItemsGUI.GUIPageInfo pageInfo) {
        if (pageInfo.isFound()) {
            AllItemsGUI.openFound(player, pageInfo.getItems(), pageInfo.getPage());
        } else {
            AllItemsGUI.openMissing(player, pageInfo.getItems(), pageInfo.getPage());
        }
    }
}
