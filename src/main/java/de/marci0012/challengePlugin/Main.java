package de.marci0012.challengePlugin;


import de.marci0012.challengePlugin.timer.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private TimerManager timerManager;

    @Override
    public void onEnable() {
        instance = this;
        timerManager = new TimerManager(this);

        getCommand("timer").setExecutor(new TimerCommand(timerManager));
        getCommand("timer").setTabCompleter(new TimerTabCompleter());

        getServer().getPluginManager().registerEvents(new TimerListener(timerManager), this);

        getLogger().info("ChallengePlugin aktiviert");
    }

    public static Main getInstance() {
        return instance;
    }
}