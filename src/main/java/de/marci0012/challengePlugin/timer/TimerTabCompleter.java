package de.marci0012.challengePlugin.timer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TimerTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {
        List<String> completions = new ArrayList<>();

        // /timer <argument>
        if (args.length == 1) {
            completions.add("start");
            completions.add("stop");
            completions.add("reset");
        }

        return completions;
    }
}
