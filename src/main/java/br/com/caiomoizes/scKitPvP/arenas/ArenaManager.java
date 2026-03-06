package br.com.caiomoizes.scKitPvP.arenas;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaManager {
    private static final Map<String, Arena> arenas = new HashMap<>();

    public static void loadArenas() {
        arenas.clear();

        ConfigurationSection section = SCKitPvP.getInstance().getConfig().getConfigurationSection("kitpvp.arenas");

        if (section == null) return;

        Bukkit.getLogger().info("Entrou aqui...");

        for (String key : section.getKeys(false)) {
            String path = key + ".";

            World world = Bukkit.getWorld(section.getString(path + "spawn.world", "world"));
            double x = section.getDouble(path + "spawn.x");
            double y = section.getDouble(path + "spawn.y");
            double z = section.getDouble(path + "spawn.z");
            float yaw = (float) section.getDouble(path + "spawn.yaw");
            float pitch = (float) section.getDouble(path + "spawn.pitch");

            Location loc = new Location(world, x, y, z, yaw, pitch);

            String displayName = section.getString(path + "display-name", key);
            Material icon = Material.matchMaterial(section.getString(path + "icon", "PAPER"));
            List<String> lore = section.getStringList(path + "lore");
            String permission = section.getString(key + "permission", "");

            arenas.put(key.toLowerCase(), new Arena(key, displayName, loc, icon != null ? icon : Material.PAPER, lore, permission));
        }
        Bukkit.getLogger().info("[ArenaManager] " + arenas.size() + " arenas carregadas!");
    }

    public static Arena getArena(String name) {
        return arenas.get(name.toLowerCase());
    }

    public static void createArena(String name, Location spawn, String displayName) {
        // 1. Cria o objeto na memória
        Arena newArena = new Arena(name, displayName, spawn, null, null, "");
        arenas.put(name.toLowerCase(), newArena);

        // 2. Salva no config.yml do plugin para persistência
        saveArenaToConfig(newArena);

        SCKitPvP.getInstance().getLogger().info("Arena '" + name + "' criada com sucesso!");
    }

    public static void createArena(String name, Location spawn) {
        createArena(name, spawn, name);
    }

    private static void saveArenaToConfig(Arena arena) {
        FileConfiguration config = SCKitPvP.getInstance().getConfig();
        String path = "kitpvp.arenas." + arena.getName().toLowerCase();

        config.set(path + ".display-name", arena.getDisplayName());
        config.set(path + ".spawn.world", arena.getSpawn().getWorld().getName());
        config.set(path + ".spawn.x", arena.getSpawn().getX());
        config.set(path + ".spawn.y", arena.getSpawn().getY());
        config.set(path + ".spawn.z", arena.getSpawn().getZ());
        config.set(path + ".spawn.yaw", arena.getSpawn().getYaw());
        config.set(path + ".spawn.pitch", arena.getSpawn().getPitch());

        SCKitPvP.getInstance().saveConfig();
    }

    public static Collection<Arena> getArenas() {
        return arenas.values();
    }
}
