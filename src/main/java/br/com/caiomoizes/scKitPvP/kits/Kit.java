package br.com.caiomoizes.scKitPvP.kits;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public interface Kit {
    default SCKitPvP getPlugin() {
        return SCKitPvP.getInstance();
    }

    default String getName() {
        return "Default";
    }

    default double getMaxHealth() {
        return 20;
    }

    default String getConfigPath() {
        return "kitpvp.kits." + getName().toLowerCase(Locale.ROOT);
    }

    default ItemStack getDisplayItem() {
        String kitName = getName();
        String configPath = getConfigPath();
        String itemName = getPlugin().getConfig().getString(configPath + ".display-item");
        Material material = Material.getMaterial(itemName);

        if (material == null) {

        }
    }
}
