package br.com.caiomoizes.scKitPvP.events;

import br.com.caiomoizes.scKitPvP.player.PlayerData;
import br.com.caiomoizes.scKitPvP.player.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCombat(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player victim) || !(e.getDamager() instanceof Player attacker))
            return;

        // Ativa o combate para ambos (ex.: 12 segundos)
        PlayerDataManager.getPlayerData(victim).setInCombat(12);
        PlayerDataManager.getPlayerData(attacker).setInCombat(12);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PlayerDataManager.getPlayerData(p);

        if (data.isInCombat()) {
            String cmd = e.getMessage().toLowerCase();

            // Lista de comandos bloqueados
            if (cmd.startsWith("/spawn") || cmd.startsWith("/kit") || cmd.startsWith("/arena")) {
                p.sendMessage(Component.text("Você não pode usar comandos em combate! Aguarde " + data.getRemainingCombatTime() + "s."));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PlayerDataManager.getPlayerData(p);

        if (data.isInCombat()) {
            // Punição por deslogar em combate
            p.setHealth(0);
            Bukkit.broadcast(Component.text(p.getName() + " deslogou em combate e foi punido!", NamedTextColor.GOLD));
        }
    }
}
