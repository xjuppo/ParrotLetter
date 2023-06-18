package me.xjuppo.parrotletter.commands.subcommands;

import me.xjuppo.parrotletter.commands.CommandManager;
import me.xjuppo.parrotletter.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class HelpCommand extends SubCommand {

    public HelpCommand(Plugin plugin, String name, String description, String usage, String permission) {
        super(plugin, name, description, usage, permission);
    }

    @Override
    public void executeCommand(CommandSender commandSender, Command command, List<String> args) {

        if (args.size() == 0) {
            commandSender.sendMessage("Correct usage: " + this.usage);
            return;
        }

        int page;

        try {
            page = Integer.parseInt(args.get(0));
        }
        catch (NumberFormatException e) {
            commandSender.sendMessage("Correct usage: " + this.usage);
            return;
        }

        if (page > CommandManager.helpPages.size()) {
            commandSender.sendMessage("Page limit reached!");
            return;
        }

        String pageToSend = String.format(ChatColor.AQUA + "Help Page %s/%s:\n" + ChatColor.GRAY + "-----\n" + ChatColor.RESET, page, CommandManager.helpPages.size()) + CommandManager.helpPages.get(page-1);
        commandSender.sendMessage(pageToSend);
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, Command command, String label, List<String> args) {

        if (args.size() == 1) {
            List<String> pages = new ArrayList<>();
            int[] pagesNumber = IntStream.range(1, CommandManager.helpPages.size()).toArray();

            for (int i : pagesNumber) {
                pages.add(String.valueOf(i));
            }

            return pages;
        }
        return null;
    }
}
