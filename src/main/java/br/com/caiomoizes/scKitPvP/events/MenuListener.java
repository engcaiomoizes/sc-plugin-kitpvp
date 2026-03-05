package br.com.caiomoizes.scKitPvP.events;

import br.com.caiomoizes.scKitPvP.kits.Kit;
import br.com.caiomoizes.scKitPvP.kits.KitManager;
import br.com.caiomoizes.scKitPvP.menus.KitSelector;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof KitSelector))
            return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p))
            return;

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR)
            return;

        if (item.getType() == Material.BARRIER) {
            p.closeInventory();
            return;
        }

        if (item.getType() == Material.ARROW) {
            int currentPage = KitSelector.getPage(p);
            String displayName = LegacyComponentSerializer.legacyAmpersand()
                    .serialize(item.getItemMeta().displayName());

            if (displayName.contains("Próxima"))
                new KitSelector(p, currentPage + 1);
            else if (displayName.contains("Anterior"))
                new KitSelector(p, currentPage - 1);
            return;
        }

        String displayName = LegacyComponentSerializer.legacySection()
                .serialize(item.getItemMeta().displayName());
        if (displayName.contains("Kit: ")) {
            String kitName = displayName.split("Kit: ")[1];
            Kit kit = KitManager.getKit(kitName.toLowerCase());

            if (kit != null) {
                kit.apply(p);
                p.closeInventory();
            }
        }
    }
}
