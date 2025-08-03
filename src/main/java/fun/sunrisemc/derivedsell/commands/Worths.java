package fun.sunrisemc.derivedsell.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import fun.sunrisemc.derivedsell.Commands;
import fun.sunrisemc.derivedsell.MaterialWorth;
import fun.sunrisemc.derivedsell.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;

public class Worths implements CommandExecutor, TabCompleter {

    private final int PAGE_SIZE = 8;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> pageNumbers = new ArrayList<>();
        int lastPage = MaterialWorth.copyWorthCache().size() / PAGE_SIZE + 1;
        for (int i = 1; i <= lastPage; i++) {
            pageNumbers.add(Integer.toString(i));
        }
        return pageNumbers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        HashMap<Material, Double> worths = MaterialWorth.copyWorthCache();

        int page;
        int maxPage = worths.size() / PAGE_SIZE + 1;
        if (args.length >= 1) {
            page = StringUtils.toInt(args[0]);
            page = Math.max(page, 1);
            page = Math.min(page, maxPage);
        }
        else {
            page = 1;
        }

        int start = (page - 1) * PAGE_SIZE;
        int end = page * PAGE_SIZE;

        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Worths " + ChatColor.GREEN + "(page " + page + "/" + maxPage + ")");
        Integer i = 0;
        for (Entry<Material, Double> entry : worths.entrySet()) {
            i++;
            if (i > start && i <= end) {
                sender.sendMessage(i.toString() + ". " + StringUtils.titleCase(entry.getKey().toString()) + ": " + Commands.displayMoney(entry.getValue()));
            }
        }
        return true;
    }
}