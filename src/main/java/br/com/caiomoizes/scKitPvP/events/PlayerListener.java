package br.com.caiomoizes.scKitPvP.events;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import br.com.caiomoizes.scKitPvP.player.PlayerData;
import br.com.caiomoizes.scKitPvP.player.PlayerDataManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {
    private final SCKitPvP plugin;

    public PlayerListener(SCKitPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerData data = PlayerDataManager.getPlayerData(e.getPlayer());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getDbManager().loadPlayerData(data);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PlayerData data = PlayerDataManager.getPlayerData(e.getPlayer());
        SCKitPvP.getInstance().getDbManager().savePlayerData(data);
        PlayerDataManager.removeData(e.getPlayer()); // Limpa da memória RAM
    }

    @EventHandler
    public void onSponge(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() &&
                e.getFrom().getBlockY() == e.getTo().getBlockY() &&
                e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }

        Block blockBelow = e.getTo().getBlock().getRelative(BlockFace.DOWN);
        if (blockBelow.getType() == Material.SPONGE) {
            Player p = e.getPlayer();
            PlayerData data = PlayerDataManager.getPlayerData(p);

            if (data != null) {
                p.setVelocity(new Vector(0, 1.5, 0));
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 0.8f);
                data.setIsSpongeJumping(true);
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;

        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            PlayerData data = PlayerDataManager.getPlayerData(p);

            if (data != null && data.getIsSpongeJumping()) {
                e.setCancelled(true);
                data.setIsSpongeJumping(false);
            }
        }
    }
}
