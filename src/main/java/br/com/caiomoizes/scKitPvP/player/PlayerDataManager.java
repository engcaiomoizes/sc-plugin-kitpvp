package br.com.caiomoizes.scKitPvP.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static final Map<UUID, PlayerData> dataMap = new HashMap<>();

    public static PlayerData getPlayerData(Player p) {
        PlayerData data = dataMap.computeIfAbsent(p.getUniqueId(), uuid -> new PlayerData(uuid, p.getName()));

        if (!data.getName().equals(p.getName()))
            data.setName(p.getName());

        return data;
    }

    public static void removeData(Player p) {
        dataMap.remove(p.getUniqueId());
    }
}
