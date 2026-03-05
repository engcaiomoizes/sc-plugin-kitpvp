package br.com.caiomoizes.scKitPvP.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static final Map<UUID, PlayerData> dataMap = new HashMap<>();

    public static PlayerData getPlayerData(Player p) {
        return dataMap.computeIfAbsent(p.getUniqueId(), PlayerData::new);
    }

    public static void removeData(Player p) {
        dataMap.remove(p.getUniqueId());
    }
}
