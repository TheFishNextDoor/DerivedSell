package fun.sunrisemc.derivedsell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialWorth {

    private static HashMap<Material, Double> worthCache = new HashMap<>();
    private static HashSet<Material> worthlessCache = new HashSet<>();

    private static HashSet<Material> checking = new HashSet<>();

    // Massively speeds up checking and fixes some loop conditions but is only valid for the current recursive search
    private static HashMap<Material, Double> tempWorthCache = new HashMap<>();
    private static HashSet<Material> tempWorthlessCache = new HashSet<>();

    public static Double getWorth(Material material, int amount) {
        Double worth = getWorth(material);
        return worth != null ? worth * amount : null;
    }

    public static Double getWorth(Material material) {
        if (checking.contains(material)) {
            return null;
        }

        if (checking.isEmpty()) {
            tempWorthCache.clear();
            tempWorthlessCache.clear();
        }

        checking.add(material);
        Double worth = findWorth(material);
        checking.remove(material);

        if (checking.isEmpty()) {
            if (worth != null && !worthCache.containsKey(material)) {
                DerivedSell.getInstance().getLogger().info("Caching worth of " + material + " as " + worth);
                worthCache.put(material, worth);
            }
            else if (worth == null && !worthlessCache.contains(material)) {
                worthlessCache.add(material);
            }
        }
        else {
            if (worth != null && !tempWorthCache.containsKey(material)) {
                tempWorthCache.put(material, worth);
            }
            else if (worth == null && !tempWorthlessCache.contains(material)) {
                tempWorthlessCache.add(material);
            }
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

    public static void setWorth(Material material, Double worth) {
        if (worth != null) {
            worthCache.put(material, worth);
            worthlessCache.remove(material);
        }
        else {
            worthCache.remove(material);
            worthlessCache.add(material);
        }
    }

    public static HashMap<Material, Double> copyWorthCache() {
        return new HashMap<>(worthCache);
    }

    public static void preCacheWorths() {
        DerivedSell.getInstance().getLogger().info("Pre-caching worths...");
    
        for (Material material : Material.values()) {
            getWorth(material, 1);
        }
    }

    private static Double findWorth(Material material) {
        if (worthCache.containsKey(material)) {
            return worthCache.get(material);
        }

        if (worthlessCache.contains(material)) {
            return null;
        }

        if (tempWorthCache.containsKey(material)) {
            return tempWorthCache.get(material);
        }

        if (tempWorthlessCache.contains(material)) {
            return null;
        }

        IngredientList ingredients = IngredientList.getCheapest(new ItemStack(material));
        if (ingredients != null) {
            return getWorthPerResult(ingredients);
        }

        return null;
    }
}
