package fun.sunrisemc.derivedsell;

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

    public void addIngredient(Material material, int amount) {
        if (containsKey(material)) {
            put(material, get(material) + amount);
        }
        else {
            put(material, amount);
        }
    }

    public static IngredientList get(ItemStack item) {
        IngredientList cheapestIngredients = null;
        Double cheapestWorth = null;
        for (Recipe recipe : Bukkit.getRecipesFor(item)) {
            IngredientList ingredientList = new IngredientList(recipe.getResult().getAmount(), getWorthMultiplier(recipe));
            int amount = recipe.getResult().getAmount();
    
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                for (RecipeChoice recipeChoice : shapedRecipe.getChoiceMap().values()) {
                    if (!(recipeChoice instanceof MaterialChoice)) {
                        ingredientList.clear();
                        break;
                    }
    
                    Material material = IngredientList.getCheapestMaterial((MaterialChoice) recipeChoice);
                    ingredientList.addIngredient(material, amount);
                }
            }
            else if (recipe instanceof ShapelessRecipe) {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
                for (RecipeChoice recipeChoice : shapelessRecipe.getChoiceList()) {
                    if (!(recipeChoice instanceof MaterialChoice)) {
                        ingredientList.clear();
                        break;
                    }
    
                    Material material = IngredientList.getCheapestMaterial((MaterialChoice) recipeChoice);
                    ingredientList.addIngredient(material, amount);
                }
            }
            else if (recipe instanceof CookingRecipe<?>) {
                CookingRecipe<?> cookingRecipe = (CookingRecipe<?>) recipe;
                if (!(cookingRecipe.getInputChoice() instanceof MaterialChoice)) {
                    continue;
                }
    
                Material material = IngredientList.getCheapestMaterial((MaterialChoice) cookingRecipe.getInputChoice());
                ingredientList.addIngredient(material, amount);
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

    private static double getWorthMultiplier(Recipe recipe) {
        if (recipe instanceof CookingRecipe<?>) {
            return 2.0;
        }
        return 1.0;
    }
}