package br.com.caiomoizes.scKitPvP.kits;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import br.com.caiomoizes.scKitPvP.kits.types.Archer;
import br.com.caiomoizes.scKitPvP.kits.types.Stomper;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class KitManager {
    private static final Map<String, Kit> kits = new HashMap<>();

    // Armazena qual kit cada jogador está usando no momento
    private static final Map<UUID, Kit> activeKits = new HashMap<>();

    public static Collection<Kit> getKits() {
        return kits.values();
    }

    public static @Nullable Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }

    public static void setKit(Player p, Kit kit) {
        activeKits.put(p.getUniqueId(), kit);
    }

    public static @Nullable Kit getPlayerKit(Player p) {
        return activeKits.get(p.getUniqueId());
    }

    public static void removePlayerKit(Player p) {
        activeKits.remove(p.getUniqueId());
    }

    public static void loadKits() {
        kits.clear();
        var config = SCKitPvP.getInstance().getConfig();
        var section = config.getConfigurationSection("kitpvp.kits");

        if (section != null) {
            for (String kitName : section.getKeys(false)) {
                Kit kit;

                switch (kitName.toLowerCase()) {
                    case "archer":
                        kit = new Archer();
                        break;
                    case "stomper":
                        kit = new Stomper();
                        break;
                    default:
                        kit = new ConfigKit(kitName);
                        break;
                }

                kits.put(kitName.toLowerCase(), kit);
                SCKitPvP.getInstance().getLogger().info("Kit carregado: " + kitName.toUpperCase());
            }
        }
    }
}
