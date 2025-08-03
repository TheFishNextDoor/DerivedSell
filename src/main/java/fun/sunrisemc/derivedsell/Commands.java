package fun.sunrisemc.derivedsell;

import java.util.ArrayList;

import org.bukkit.Material;
public class Commands {

    private static final String MONEY_PREFIX = "$";
    private static final String MONEY_SUFFIX = "";
    private static final int ROUND_TO = 2;

    public static ArrayList<String> materialNames() {
        ArrayList<String> names = new ArrayList<>(Material.values().length);
        for (Material material : Material.values()) {
            names.add(material.toString().toLowerCase());
        }
        return names;
    }

    public static String displayMoney(double value) {
        return MONEY_PREFIX + String.format("%." + ROUND_TO + "f", value) + MONEY_SUFFIX;
    }
}