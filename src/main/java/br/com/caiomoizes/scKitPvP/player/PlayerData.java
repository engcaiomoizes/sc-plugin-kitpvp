package br.com.caiomoizes.scKitPvP.player;

import br.com.caiomoizes.scKitPvP.kits.Kit;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private Kit activeKit;

    // Cooldowns
    private final Map<Material, Long> cooldowns = new HashMap<>();

    // Estatísticas da sessão (opcional)
    private int kills = 0;
    private int deaths = 0;

    private int changeCount = 0;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public Kit getActiveKit() {
        return this.activeKit;
    }

    public void incrementChangeCount() {
        this.changeCount++;
    }

    public int getChangeCount() {
        return changeCount;
    }

    // ------ Lógica de Cooldown ------

    public void setCooldown(Material material, int seconds) {
        cooldowns.put(material, System.currentTimeMillis() + (seconds * 1000L));
    }

    public boolean hasCooldown(Material material) {
        if (!cooldowns.containsKey(material)) return false;

        if (System.currentTimeMillis() >= cooldowns.get(material)) {
            cooldowns.remove(material);
            return false;
        }

        return true;
    }

    public long getRemainingCooldown(Material material) {
        if (!hasCooldown(material)) return 0;
        return (cooldowns.get(material) - System.currentTimeMillis()) / 1000;
    }

    public void removeKit() {
        this.activeKit = null;
        this.cooldowns.clear();
    }
}
