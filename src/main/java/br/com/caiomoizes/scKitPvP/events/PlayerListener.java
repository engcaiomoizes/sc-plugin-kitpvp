package br.com.caiomoizes.scKitPvP.events;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import br.com.caiomoizes.scKitPvP.player.PlayerData;
import br.com.caiomoizes.scKitPvP.player.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
}
