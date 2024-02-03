package fun.sunrisemc.derivedsell;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    public final Logger LOGGER = Logger.getLogger(getName());

    private static Plugin instance;

    public void onEnable() {
        instance = this;

        for (Material material : Material.values()) {
            Double worth = MaterialWorth.getWorth(material, 1);
            if (worth != null) {
                LOGGER.info(material + " is worth " + worth);
            }
        }

        Material material = Material.IRON_SWORD;
        Double worth = MaterialWorth.getWorth(material, 1);
        if (worth != null) {
            LOGGER.info(material + " is worth " + worth);
        }
        else {
            LOGGER.info(material + " is worthless");
        }

        LOGGER.info("Plugin enabled.");
    }

    public void onDisable() {
        LOGGER.info("Plugin disabled.");
    }

    public static Plugin getInstance() {
        return instance;
    }
}
