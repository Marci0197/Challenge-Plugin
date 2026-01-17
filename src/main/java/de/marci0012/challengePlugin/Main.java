package de.marci0012.challengePlugin;

import de.marci0012.challengePlugin.challenge.ChallengeTabCompleter;
import de.marci0012.challengePlugin.challenge.allitems.*;
import de.marci0012.challengePlugin.timer.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private TimerManager timerManager;
    private AllItemsChallenge allItemsChallenge;

    @Override
    public void onEnable() {
        instance = this;

        // Timer Setup
        timerManager = new TimerManager(this);
        getCommand("timer").setExecutor(new TimerCommand(timerManager));
        getCommand("timer").setTabCompleter(new TimerTabCompleter());

        // Challenge Setup
        getCommand("challenge").setTabCompleter(new ChallengeTabCompleter());

        // AllItems Challenge Setup
        allItemsChallenge = new AllItemsChallenge(timerManager, this);
        getCommand("challenge").setExecutor(new AllItemsCommand(allItemsChallenge));

        getServer().getPluginManager().registerEvents(
                new AllItemsListener(allItemsChallenge), this
        );
        getServer().getPluginManager().registerEvents(
                new TimerListener(timerManager), this
        );
        getServer().getPluginManager().registerEvents(
                new AllItemsGUIListener(), this
        );

        // End laden
        World end = Bukkit.getWorlds().stream()
                .filter(w -> w.getEnvironment() == World.Environment.THE_END)
                .findFirst()
                .orElse(null);

        getLogger().info("ChallengePlugin aktiviert");
    }

    public static Main getInstance() {
        return instance;
    }

    public AllItemsChallenge getAllItemsChallenge() {
        return allItemsChallenge;
    }
}
