package br.com.caiomoizes.scKitPvP.kits;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
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

    ItemStack getIcon();

    List<ItemStack> getItems();

    void apply(Player p);
}
