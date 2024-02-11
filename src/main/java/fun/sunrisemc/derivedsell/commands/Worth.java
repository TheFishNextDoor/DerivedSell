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
import fun.sunrisemc.derivedsell.MaterialWorth;

public class Worth implements CommandExecutor, TabCompleter {

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
        Material material = null;
        int quantity = 1;

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                return false;
            }

            Player player = (Player) sender;
            ItemStack handItem = player.getInventory().getItemInMainHand();
            material = handItem.getType();
            quantity = handItem.getAmount();
        }

        if (args.length >= 1) {
            material = Material.getMaterial(args[0].toUpperCase());
        }

        if (args.length >= 2) {
            quantity = Math.max(Commands.number(args[1]), 1);
        }

        if (material == null) {
            sender.sendMessage(ChatColor.RED + "Invalid material.");
            return true;
        }

        Double worth = MaterialWorth.getWorth(material);
        if (worth == null) {
            sender.sendMessage(ChatColor.YELLOW + Commands.titleCase(material.name()) + " cannot be sold to the server.");
            return true;
        }

        if (quantity > 1) {
            sender.sendMessage(ChatColor.GREEN + "Stack of " + quantity + " " + Commands.titleCase(material.name()) + " is worth " + (worth * quantity) + " (" + worth + " per item).");
        }
        else {
            sender.sendMessage(ChatColor.GREEN + Commands.titleCase(material.name()) + " is worth " + worth + " per item.");
        }
        return true;
    }
}