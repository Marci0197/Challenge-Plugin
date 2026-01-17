package de.marci0012.challengePlugin.challenge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class ChallengeTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

        if (args.length == 1) return List.of("allitems");
        if (args.length == 2 && args[0].equalsIgnoreCase("allitems"))
            return List.of("missing", "found", "stats"); // search -> missing, stats hinzugefügt

        return List.of();
    }
}
