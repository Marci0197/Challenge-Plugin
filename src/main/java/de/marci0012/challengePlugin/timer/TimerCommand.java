package de.marci0012.challengePlugin.timer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;

public class TimerCommand implements CommandExecutor {

    private final TimerManager timerManager;

    public TimerCommand(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("/timer <start|stop|reset>");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "start" -> {
                if (timerManager.isRunning()) return true;

                if (!isEnderDragonAlive()) {
                    sender.sendMessage(
                            Component.text("Der Enderdrache lebt nicht! Timer kann nicht gestartet werden.", NamedTextColor.DARK_RED)
                    );
                    return true;
                }

                timerManager.startTimer();

                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setGameMode(GameMode.SURVIVAL);
                }
            }

            case "stop" -> timerManager.stopTimer();

            case "reset" -> timerManager.resetTimer();

            default -> sender.sendMessage("/timer <start|stop|reset>");
        }

        return true;
    }


    private boolean isEnderDragonAlive() {
        World end = Bukkit.getWorlds().stream()
                .filter(w -> w.getEnvironment() == World.Environment.THE_END)
                .findFirst()
                .orElse(null);

        if (end == null) return false;

        return end.getEntitiesByClass(EnderDragon.class)
                .stream()
                .anyMatch(dragon -> !dragon.isDead());
    }
}
