package fun.sunrisemc.derivedsell.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fun.sunrisemc.derivedsell.Commands;
import fun.sunrisemc.derivedsell.InventoryTools;
import fun.sunrisemc.derivedsell.MaterialWorth;
import fun.sunrisemc.derivedsell.Plugin;

public class Sell implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Commands.materialNames();
        }
        else if(args.length == 2) {
            ArrayList<String> numbers = new ArrayList<>();
            numbers.add("1");
            numbers.add("16");
            numbers.add("64");
            return numbers;
        }
        return null;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        Player player = (Player) sender;

        ItemStack item;
        boolean useHand = args.length == 0 || args[0].equals("hand");
        if (useHand) {
            item = player.getInventory().getItemInMainHand().clone();
        } 
        else {
            Material material = Material.matchMaterial(args[0]);
            if (material == null) {
                sender.sendMessage(ChatColor.RED + "Invalid material.");
                return true;
            }
            item = new ItemStack(material);
        }
        
        Material material = item.getType();

        int quantity = Integer.MAX_VALUE;
        if (args.length >= 2) {
            quantity = Math.max(Commands.number(args[1]), 1);
        }

        Double worth = MaterialWorth.getWorth(material);
        if (worth == null) {
            sender.sendMessage(ChatColor.YELLOW + Commands.titleCase(material.toString()) + " cannot be sold to the server.");
            return true;
        }

        int inInventory = InventoryTools.countSimilar(player, item);
        quantity = Math.min(quantity, inInventory);
        if (quantity == 0) {
            sender.sendMessage(ChatColor.YELLOW + "You do not have any " + Commands.titleCase(material.toString()) + " in your inventory.");
            return true;
        }

        if (!InventoryTools.take(player, item, quantity)) {
            sender.sendMessage(ChatColor.DARK_RED + "Failed to sell " + quantity + " " + Commands.titleCase(material.toString()) + ".");
            return true;
        }

        double money = worth * quantity;
        Plugin.getEconomy().depositPlayer(player, money);
        sender.sendMessage(ChatColor.GREEN + "Sold " + quantity + " " + Commands.titleCase(material.toString()) + " for " + Commands.displayMoney(money) + ".");
        return true;
    }
}
