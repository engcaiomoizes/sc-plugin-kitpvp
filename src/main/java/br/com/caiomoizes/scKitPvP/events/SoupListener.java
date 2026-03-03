package br.com.caiomoizes.scKitPvP.events;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupListener implements Listener {
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!e.hasItem()) return;

        ItemStack item = e.getItem();

        if (item != null && item.getType() == Material.MUSHROOM_STEW) {
            if (e.getAction().name().contains("RIGHT_CLICK")) {
                if (p.getHealth() < p.getAttribute(Attribute.MAX_HEALTH).getValue()) {
                    double healing = 7.0;
                    double newHealth = Math.min(p.getHealth() + healing, p.getAttribute(Attribute.MAX_HEALTH).getValue());

                    p.setHealth(newHealth);

                    item.setType(Material.BOWL);

                    e.setCancelled(true);
                }
            }
        }
    }
}
