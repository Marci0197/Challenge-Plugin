package de.marci0012.challengePlugin.timer;

import de.marci0012.challengePlugin.Main;
import de.marci0012.challengePlugin.challenge.allitems.AllItemsChallenge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerListener implements Listener {

    private final TimerManager timerManager;

    public TimerListener(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player dead = event.getPlayer();
        Location deathLocation = dead.getLocation().clone();

        timerManager.stopTimer();

        Bukkit.broadcast(
                Component.text("Timer gestoppt! ", NamedTextColor.DARK_RED)
                        .append(Component.text(dead.getName() + " ist gestorben!", NamedTextColor.DARK_RED))
        );

        event.setDeathMessage(null);

        new BukkitRunnable() {
            @Override
            public void run() {
                dead.spigot().respawn();
                dead.teleport(deathLocation);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setGameMode(GameMode.SPECTATOR);
                }
            }
        }.runTaskLater(timerManager.getPlugin(), 1L);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        AllItemsChallenge challenge = Main.getInstance().getAllItemsChallenge();
        if (challenge.isActive()) {
            challenge.addItem(event.getItem().getItemStack().getType());
        }
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof EnderDragon)) return;

        timerManager.stopTimer();
        Bukkit.broadcast(Component.text("Timer gestoppt!", NamedTextColor.GOLD));
    }
}
