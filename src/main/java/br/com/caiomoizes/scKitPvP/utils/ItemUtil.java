package br.com.caiomoizes.scKitPvP.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    public static void fillInventoryWithSoup(Player p) {
        ItemStack soup = new ItemStack(Material.MUSHROOM_STEW);

        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);

            if (item == null || item.getType() == Material.AIR)
                p.getInventory().setItem(i, soup);
        }
    }
}
