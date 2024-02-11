package fun.sunrisemc.derivedsell;

import java.util.ArrayList;

import org.bukkit.Material;

public class CommandTools {

    public static ArrayList<String> materialNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Material material : Material.values()) {
            names.add(material.name());
        }
        return names;
    }

    public static String titleCase(String str) {
        str = str.replace("_", " ");
        String[] words = str.split(" ");
        String titleCase = "";
        for (String word : words) {
            titleCase += word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        return titleCase.trim();
    }
}
