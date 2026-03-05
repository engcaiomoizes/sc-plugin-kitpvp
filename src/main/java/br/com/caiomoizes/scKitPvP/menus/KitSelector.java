package br.com.caiomoizes.scKitPvP.menus;

import br.com.caiomoizes.scKitPvP.kits.Kit;
import br.com.caiomoizes.scKitPvP.kits.KitManager;
import net.kyori.adventure.text.Component;
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

public class KitSelector implements InventoryHolder {
    private final String inventoryName = "Kit Selector";
    private static final Map<Player, Integer> pages = new HashMap<>();
    private final Inventory inventory;

    private final int inventorySize = 54;
    private final int kitsPerPage = 21;

    public KitSelector(@NotNull Player p, int page)  {
        this.inventory = Bukkit.createInventory(this, inventorySize, Component.text(inventoryName + " - Pág.: " + (page + 1)));

        pages.put(p, page);
        populateInventory(p, page);
        p.openInventory(inventory);
    }

    private static int ensureSize(int size) {
        int maxSize = 36;
        int halfMaxSize = 18;
        int rowSize = 9;

        if (size >= maxSize)
            return maxSize;

        if ((size + halfMaxSize) % rowSize == 0)
            return size;

        ++size;
        return ensureSize(size);
    }

    private void populateInventory(Player p, int page) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.displayName(Component.empty());
        glass.setItemMeta(glassMeta);

        // Preencher as bordas
        for (int i : new int[]{0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53})
            inventory.setItem(i, glass);

        // 2. Lógica de paginação
        List<Kit> allKits = new ArrayList<>(KitManager.getKits());
        int start = page * kitsPerPage;
        int end = Math.min(start + kitsPerPage, allKits.size());

        int[] slots = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34
        };

        int slotIndex = 0;
        for (int i = start; i < end; i++) {
            if (slotIndex >= slots.length) break;

            Kit kit = allKits.get(i);
            inventory.setItem(slots[slotIndex], kit.getIcon());
            slotIndex++;
        }

        // 3. Botões de navegação
        if (page > 0)
            inventory.setItem(48, createNavigationItem(Material.ARROW, "&aPágina Anterior"));
        if (end < allKits.size())
            inventory.setItem(50, createNavigationItem(Material.ARROW, "&aPróxima Página"));

        inventory.setItem(49, createNavigationItem(Material.BARRIER, "&cFechar Menu"));
    }

    public static int getPage(Player p) {
        return pages.getOrDefault(p, 0);
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
