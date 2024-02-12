package fun.sunrisemc.derivedsell;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryTools {

    public static boolean take(Player player, Material material, int amount) {
        return take(player, new ItemStack(material), amount);
    }

    public static boolean take(Player player, ItemStack item, int amount) {
        item = item.clone();
        if (!has(player, item, amount)) {
            return false;
        }

        int remaining = amount;
        for (ItemStack inventoryItem : player.getInventory().getContents()) {
            if (!item.isSimilar(inventoryItem)) {
                continue;
            }

            if (inventoryItem.getAmount() > remaining) {
                inventoryItem.setAmount(inventoryItem.getAmount() - remaining);
                return true;
            }
            else {
                remaining -= inventoryItem.getAmount();
                inventoryItem.setAmount(0);
            }
        }
        if (remaining > 0) {
            throw new IllegalStateException("Failed to take " + amount + " of " + item.getType() + " from " + player.getName());
        }
        return true;
    }

    public static boolean has(Player player, Material material, int amount) {
        return has(player, new ItemStack(material), amount);
    }

    public static boolean has(Player player, ItemStack item, int amount) {
        return countSimilar(player, item) >= amount;
    }

    public static int countSimilar(Player player, Material material) {
        return countSimilar(player, new ItemStack(material));
    }

    public static int countSimilar(Player player, ItemStack item) {
        int count = 0;
        for (ItemStack inventoryItem : player.getInventory().getContents()) {
            if (item.isSimilar(inventoryItem)) {
                count += inventoryItem.getAmount();
            }
        }
        return count;
    }
}
