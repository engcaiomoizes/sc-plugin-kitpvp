package br.com.caiomoizes.scKitPvP.events;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class StomperListener implements Listener {
    @EventHandler
    public void onStomp(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p) || e.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;

        // Lógica de verificação de kit
        // CÓDIGO

        double fallDamage = e.getDamage();

        if (fallDamage > 4.0)
            e.setDamage(4.0);

        double radius = 5.0;
        List<Entity> nearby = p.getNearbyEntities(radius, radius, radius);

        for (Entity entity : nearby) {
            if (entity instanceof Player victim && !victim.equals(p)) {
                victim.damage(fallDamage, p);
                victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.0f, 1.0f);
            }
        }

        p.getWorld().spawnParticle(Particle.EXPLOSION, p.getLocation(), 5);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
    }
}
