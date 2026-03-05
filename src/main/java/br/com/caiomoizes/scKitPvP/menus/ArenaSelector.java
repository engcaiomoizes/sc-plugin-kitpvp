package br.com.caiomoizes.scKitPvP.menus;

import br.com.caiomoizes.scKitPvP.arenas.Arena;
import br.com.caiomoizes.scKitPvP.arenas.ArenaManager;
import br.com.caiomoizes.scKitPvP.kits.Kit;
import br.com.caiomoizes.scKitPvP.kits.KitManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaSelector implements InventoryHolder {
    private final String inventoryName = "Arena Selector";
    private final Inventory inventory;

    private final int inventorySize = 54;

    public ArenaSelector(@NotNull Player p)  {
        this.inventory = Bukkit.createInventory(this, inventorySize, Component.text(inventoryName, NamedTextColor.GOLD, TextDecoration.BOLD));

        populateInventory(p);
        p.openInventory(inventory);
    }

    private void populateInventory(Player p) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.displayName(Component.empty());
        glass.setItemMeta(glassMeta);

        // Preencher as bordas
        for (int i : new int[]{0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53})
            inventory.setItem(i, glass);

        // 2. Lógica de paginação
        List<Arena> allArenas = new ArrayList<>(ArenaManager.getArenas());

        int[] slots = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        };

        for (int i = 0; i < allArenas.size(); i++) {
            if (i >= slots.length) break;

            Arena arena = allArenas.get(i);
            inventory.setItem(slots[i], new ItemStack(arena.getIcon()));
        }

        inventory.setItem(49, createNavigationItem(Material.BARRIER, "&cFechar Menu"));
    }

    private ItemStack createNavigationItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(name));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public @NonNull Inventory getInventory() {
        return this.inventory;
    }
}
