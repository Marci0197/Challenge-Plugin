package de.marci0012.challengePlugin.challenge.allitems;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AllItemsBossBar {

    private final AllItemsChallenge challenge;
    private final BossBar bossBar;

    public AllItemsBossBar(AllItemsChallenge challenge, JavaPlugin plugin) {
        this.challenge = challenge;
        this.bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);

        // Alle Spieler hinzufügen
        for (Player p : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(p);
        }

        // Listener für neue Spieler
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent event) {
                Player player = event.getPlayer();
                bossBar.addPlayer(player);
                update(); // gleiches Update beim Join
            }
        }, plugin);
    }

    public void update() {
        int found = challenge.getFoundCount();
        int total = challenge.getTotalItems();
        double progress = (double) found / total;

        bossBar.setProgress(progress);
        bossBar.setTitle("§aAll Items: " + found + " / " + total);
    }
}
