package me.xjuppo.parrotletter.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseTabCompleter implements TabCompleter {

    CommandManager commandManager;

    public BaseTabCompleter(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {

        if (args.length == 1) {
            List<String> commands = new ArrayList<>();
            for (String subCommand : commandManager.getSubCommands().keySet()) {
                if (subCommand.contains(args[0])) {
                    commands.add(subCommand);
                }
            }
            return commands;
        }

        if (args.length > 1) {
            if (commandManager.getSubCommands().keySet().contains(args[0])) {

                List<String> subArgs = Arrays.asList(args).subList(1, args.length);

                return commandManager.getSubCommands().get(args[0]).tabComplete(commandSender, command, label, subArgs);
            }
        }
        return null;
    }
}
