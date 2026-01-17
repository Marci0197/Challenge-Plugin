package de.marci0012.challengePlugin.timer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerManager {

    private final JavaPlugin plugin;
    private long seconds = 0;
    private boolean running = false;

    private BukkitRunnable timeTask;
    private BukkitRunnable displayTask;

    public TimerManager(JavaPlugin plugin) {
        this.plugin = plugin;
        startDisplayTask();
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void startTimer() {
        if (running) return;

        running = true;

        timeTask = new BukkitRunnable() {
            @Override
            public void run() {
                seconds++;
            }
        };
        timeTask.runTaskTimer(plugin, 20L, 20L);
    }

    public void stopTimer() {
        if (!running) return;

        running = false;

        if (timeTask != null) {
            timeTask.cancel();
        }
    }

    public void resetTimer() {
        stopTimer();
        seconds = 0;
    }

    public boolean isRunning() {
        return running;
    }

    private void startDisplayTask() {
        displayTask = new BukkitRunnable() {
            @Override
            public void run() {
                sendActionBar();
            }
        };
        displayTask.runTaskTimer(plugin, 0L, 10L);
    }

    private void sendActionBar() {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        Component text = Component.empty();

        if (days > 0)
            text = text.append(Component.text(days + "d ", NamedTextColor.DARK_BLUE, TextDecoration.BOLD));
        if (hours > 0 || days > 0)
            text = text.append(Component.text(hours + "h ", NamedTextColor.DARK_BLUE, TextDecoration.BOLD));
        if (minutes > 0 || hours > 0 || days > 0)
            text = text.append(Component.text(minutes + "m ", NamedTextColor.DARK_BLUE, TextDecoration.BOLD));

        text = text.append(Component.text(secs + "s", NamedTextColor.DARK_BLUE, TextDecoration.BOLD));

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(text);
        }
    }
}
