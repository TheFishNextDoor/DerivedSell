package fun.sunrisemc.derivedsell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialWorth {

    private static HashMap<Material, Double> worths = new HashMap<>();
    private static HashSet<Material> worthless = new HashSet<>();
    private static HashSet<Material> checking = new HashSet<>();

    static {
        worths.put(Material.OAK_LOG, 1.0);
    }

    public static Double getWorth(Material material, int amount) {
        Double worth = getWorth(material);
        if (worth == null) {
            return null;
        }
        return worth * amount;
    }

    public static Double getWorth(Material material) {
        if (checking.contains(material)) {
            return null;
        }

        checking.add(material);
        Double worth = findWorth(material);
        checking.remove(material);

        if (worth != null && !worths.containsKey(material)) {
            worths.put(material, worth);
        }
        else if (worth == null && !worthless.contains(material)) {
            worthless.add(material);
        }

        return worth;
    }

    public static Double getWorthPerResult(IngredientList ingredientList) {
        Double totalWorth = getTotalWorth(ingredientList);
        return totalWorth != null ? totalWorth / ingredientList.resultingAmount() : null;
    }


    public static Double getTotalWorth(IngredientList ingredientList) {
        if (ingredientList.isEmpty()) {
            return null;
        }

        Double totalWorth = 0.0;
        for (Entry<Material, Integer> ingredient : ingredientList.entrySet()) {
            Double materialWorth = getWorth(ingredient.getKey(), ingredient.getValue());
            if (materialWorth == null) {
                return null;
            }
            totalWorth += materialWorth;
        }

        return totalWorth * ingredientList.worthMultiplier();
    }

    private static Double findWorth(Material material) {
        if (worths.containsKey(material)) {
            return worths.get(material);
        }

        if (worthless.contains(material)) {
            return null;
        }

        IngredientList ingredients = IngredientList.getCheapest(new ItemStack(material));
        if (ingredients != null) {
            return getWorthPerResult(ingredients);
        }

        return null;
    }
}
