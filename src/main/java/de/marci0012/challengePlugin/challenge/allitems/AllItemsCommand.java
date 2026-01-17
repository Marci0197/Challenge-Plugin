package de.marci0012.challengePlugin.challenge.allitems;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class AllItemsCommand implements CommandExecutor {

    private final AllItemsChallenge challenge;

    public AllItemsCommand(AllItemsChallenge challenge) {
        this.challenge = challenge;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;
        if (args.length != 2) return false;
        if (!args[0].equalsIgnoreCase("allitems")) return false;

        switch (args[1].toLowerCase()) {
            case "missing" -> {
                Set<Material> missing = challenge.getMissingItems();
                if (missing.isEmpty()) {
                    player.sendMessage("§aDu hast bereits alle Items gefunden!");
                } else {
                    player.sendMessage("§eItems, die du noch sammeln musst: §7" + missing.size());
                    AllItemsGUI.openMissing(player, missing, 0);
                }
            }
            case "found" -> {
                Set<Material> found = challenge.getFoundItems();
                if (found.isEmpty()) {
                    player.sendMessage("§cDu hast noch keine Items gefunden.");
                } else {
                    player.sendMessage("§aGefundene Items: §7" + found.size());
                    AllItemsGUI.openFound(player, found, 0);
                }
            }
            case "stats" -> {
                int found = challenge.getFoundCount();
                int total = challenge.getTotalItems();
                double percent = (found * 100.0) / total;

                player.sendMessage("§b§lAll Items – Status");
                player.sendMessage("§7Fortschritt: §a" + found + " §7/ §f" + total + " Items");
                player.sendMessage("§7Prozent: §a" + String.format("%.2f", percent) + " %");
                player.sendMessage("§7Status: " + (challenge.isActive() ? "§aLäuft" : "§cGestoppt"));
            }
        }

        return true;
    }
}