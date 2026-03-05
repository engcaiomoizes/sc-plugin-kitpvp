package br.com.caiomoizes.scKitPvP.events.kits;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import br.com.caiomoizes.scKitPvP.kits.Kit;
import br.com.caiomoizes.scKitPvP.kits.KitManager;
import br.com.caiomoizes.scKitPvP.kits.types.Archer;
import br.com.caiomoizes.scKitPvP.player.PlayerData;
import br.com.caiomoizes.scKitPvP.player.PlayerDataManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ArcherListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (!(KitManager.getPlayerKit(p) instanceof Archer archerKit))
            return;

        // Lógica da habilidade aqui
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        PlayerData playerData = PlayerDataManager.getPlayerData(p);
        Location playerLoc = p.getLocation();
        Kit playerKit = playerData.getActiveKit();
        ItemStack itemStack = e.getItem();
        Material abilityItem = Material.FEATHER;

        // Ignora o evento se o item fornecido não corresponder ao item desejado.
        if (itemStack == null
                || !itemStack.hasItemMeta()
                || !itemStack.getItemMeta().hasDisplayName()
                || itemStack.getType() != abilityItem) {
            return;
        }

        // Lógica de SAFE ZONE
        //

        if (playerData.hasCooldown(abilityItem))
            return;

        p.getWorld().playSound(playerLoc, Sound.ENTITY_BAT_TAKEOFF, 1, 1);

        p.removePotionEffect(PotionEffectType.SPEED);
        p.removePotionEffect(PotionEffectType.RESISTANCE);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, archerKit.getAbilityDuration() * 20, 2, true, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, archerKit.getAbilityDuration() * 20, 0, true, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, archerKit.getAbilityDuration() * 20, 1, true, true));

        // Restaura os efeitos padrão da poção do kit após o término da duração da habilidade.
        // Captura a versão atual antes de agendar a Task
        final int currentVersion = playerData.getChangeCount();

        // Task agendada para restaurar os efeitos
        new BukkitRunnable() {
            @Override
            public void run() {
                if (playerData.getChangeCount() != currentVersion)
                    return;

                if (p.isOnline()) {
                    for (PotionEffect effect : archerKit.getPotionEffects())
                        p.addPotionEffect(effect);
                    p.sendMessage(Component.text("Efeitos passivos restaurados.", NamedTextColor.YELLOW));
                }
            }
        }.runTaskLater(SCKitPvP.getInstance(), archerKit.getAbilityDuration() * 20L + 2L);
    }
}
