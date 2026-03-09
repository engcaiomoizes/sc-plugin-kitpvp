package br.com.caiomoizes.scKitPvP.player;

import br.com.caiomoizes.scKitPvP.kits.Kit;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private String name;

    // ------ Estatísticas de Jogo ------
    private int kills;
    private int deaths;
    private int coins;
    private int killstreak;
    private int maxKillstreak;

    // ------ Estado do Jogador ------
    private Kit activeKit;
    private int changeCount;
    private long combatTimeout;

    // ------ Atributos Temporários ------
    private boolean isSpongeJumping;

    // Cooldowns
    private final Map<Material, Long> cooldowns = new HashMap<>();

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.kills = 0;
        this.deaths = 0;
        this.coins = 0;
        this.killstreak = 0;
        this.maxKillstreak = 0;
        this.changeCount = 0;
        this.combatTimeout = 0;
        this.isSpongeJumping = false;
    }

    // ------ Getters ------

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getCoins() {
        return coins;
    }

    public int getMaxKillstreak() {
        return maxKillstreak;
    }

    public boolean getIsSpongeJumping() {
        return isSpongeJumping;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setMaxKillstreak(int maxKillstreak) {
        this.maxKillstreak = maxKillstreak;
    }

    public void setIsSpongeJumping(boolean isSpongeJumping) {
        this.isSpongeJumping = isSpongeJumping;
    }

    // ------ Lógica de Combate ------

    public void setInCombat(int seconds) {
        this.combatTimeout = System.currentTimeMillis() + (seconds * 1000L);
    }

    public boolean isInCombat() {
        return combatTimeout > System.currentTimeMillis();
    }

    public long getRemainingCombatTime() {
        long remaining = combatTimeout - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }

    // ------ Lógica de Kills/Deaths ------
    public void addKill() {
        this.kills++;
        this.killstreak++;
        if (killstreak > maxKillstreak)
            maxKillstreak = killstreak;
    }

    public void addDeath() {
        this.deaths++;
        this.killstreak = 0; // Reseta o killstreak ao morrer
    }

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public boolean removeCoins(int amount) {
        if (this.coins >= amount) {
            this.coins -= amount;
            return true;
        }
        return false;
    }

    // ------ Utilidades ------

    public void incrementChangeCount() {
        this.changeCount++;
    }

    public int getChangeCount() {
        return changeCount;
    }

    public double getKDR() {
        if (deaths == 0) return kills;
        return (double) kills / deaths;
    }

    public Kit getActiveKit() {
        return this.activeKit;
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
