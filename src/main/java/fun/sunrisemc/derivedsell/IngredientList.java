package fun.sunrisemc.derivedsell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class IngredientList extends HashMap<Material, Integer> {

    private final int resultingAmount;
    private final double worthMultiplier;

    private IngredientList(int resultingAmount, double worthMultiplier) {
        super();
        this.resultingAmount = resultingAmount;
        this.worthMultiplier = worthMultiplier;
    }

    public int resultingAmount() {
        return resultingAmount;
    }

    public double worthMultiplier() {
        return worthMultiplier;
    }

    public void add(Material material, int amount) {
        if (containsKey(material)) {
            put(material, get(material) + amount);
        }
        else {
            put(material, amount);
        }
    }

    public static IngredientList getCheapest(ItemStack item) {
        IngredientList cheapestIngredients = null;
        Double cheapestWorth = null;
        for (Recipe recipe : Bukkit.getRecipesFor(item)) {
            int resultAmount = recipe.getResult().getAmount();
            IngredientList ingredientList = new IngredientList(resultAmount, getWorthMultiplier(recipe));
            for (RecipeChoice recipeChoice : getIngredients(recipe)) {
                if (!(recipeChoice instanceof MaterialChoice)) {
                    ingredientList.clear();
                    break;
                }
    
                Material material = IngredientList.getCheapestMaterial((MaterialChoice) recipeChoice);
                ingredientList.add(material, 1);
            }
    
            if (ingredientList.isEmpty()) {
                continue;
            }
    
            Double worthPerResult = MaterialWorth.getWorthPerResult(ingredientList);
            if (worthPerResult == null) {
                continue;
            }
    
            if (cheapestWorth == null || worthPerResult < cheapestWorth) {
                cheapestIngredients = ingredientList;
                cheapestWorth = worthPerResult;
            }
        }
        return cheapestIngredients;
    }

    private static double getWorthMultiplier(Recipe recipe) {
        if (recipe instanceof CookingRecipe<?>) {
            return 2.0;
        }
        return 1.0;
    }

    private static Collection<RecipeChoice> getIngredients(Recipe recipe) {
        if (recipe instanceof ShapedRecipe) {
            return ((ShapedRecipe) recipe).getChoiceMap().values();
        }
        else if (recipe instanceof ShapelessRecipe) {
            return ((ShapelessRecipe) recipe).getChoiceList();
        }
        else if (recipe instanceof CookingRecipe<?>) {
            ArrayList<RecipeChoice> choiceList = new ArrayList<>();
            choiceList.add(((CookingRecipe<?>) recipe).getInputChoice());
            return choiceList;
        }
        return new ArrayList<RecipeChoice>();
    }

    private static Material getCheapestMaterial(MaterialChoice materialChoice) {
        Material cheapestMaterial = null;
        Double cheapestWorth = null;
        for (Material material : materialChoice.getChoices()) {
            if (cheapestMaterial == null) {
                cheapestMaterial = material;
            }

            Double worth = MaterialWorth.getWorth(material, materialChoice.getItemStack().getAmount());
            if (worth == null) {
                continue;
            }
    
            if (cheapestWorth == null || worth < cheapestWorth) {
                cheapestMaterial = material;
                cheapestWorth = worth;
            }
        }
        return cheapestMaterial;
    }
}