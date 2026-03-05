package br.com.caiomoizes.scKitPvP.arenas;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class Arena {
    private final String name;
    private final String displayName;
    private final Location spawn;
    private final Material icon;
    private final List<String> lore;
    private final String permission;

    public Arena(String name, String displayName, Location spawn, Material icon, List<String> lore, String permission) {
        this.name = name;
        this.displayName = displayName;
        this.spawn = spawn;
        this.icon = icon;
        this.lore = lore;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Material getIcon() {
        return icon;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getPermission() {
        return permission;
    }
}
