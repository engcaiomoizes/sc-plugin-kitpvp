package br.com.caiomoizes.scKitPvP.holograms;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import br.com.caiomoizes.scKitPvP.player.PlayerData;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RankingHologramManager {
    private final SCKitPvP plugin;
    private final String HOLO_NAME = "ranking_kills";

    public RankingHologramManager(SCKitPvP plugin) {
        this.plugin = plugin;
    }

    // Cria ou move o holograma para a posição do admin
    public void setHologramLocation(Location loc) {
        if (!Bukkit.getPluginManager().isPluginEnabled("DecentHolograms")) {
            plugin.getLogger().severe("ERRO: DecentHolograms não encontrado ou desativado!");
            return;
        }

        try {
            Hologram holo = DHAPI.getHologram(HOLO_NAME);
            if (holo == null)
                DHAPI.createHologram(HOLO_NAME, loc);
            else
                DHAPI.moveHologram(HOLO_NAME, loc);
            updateRanking(); // Atualiza os dados imediatamente
        } catch (Exception e) {
            plugin.getLogger().warning("Falha ao interagir com a API do DecentHolograms.");
        }
    }

    public void updateRanking() {
        // Buscamos os dados no SQLite de forma assíncrona
        new BukkitRunnable() {
            @Override
            public void run() {
                List<PlayerData> topPlayers = plugin.getDbManager().getTopKills(5);

                // Voltamos para a thread principal para atualizar o holograma
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        applyHologramLines(topPlayers);
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
    }

    private void applyHologramLines(List<PlayerData> top) {
        Hologram holo = DHAPI.getHologram(HOLO_NAME);
        if (holo == null) return;

        List<String> lines = new ArrayList<>();
        lines.add("&6&l\uD83C\uDFC6 TOP 5 KILLS \uD83C\uDFC6");
        lines.add("&7Servidor KitPvP");
        lines.add("");

        if (top.isEmpty()) {
            lines.add("&cRanking vazio...");
        } else {
            for (int i = 0; i < top.size(); i++) {
                PlayerData data = top.get(i);
                String prefix = getPosColor(i + 1);
                lines.add(prefix + (i + 1) + "º &f" + data.getName() + " &7- &a" + data.getKills() + " Kills");
            }
        }

        DHAPI.setHologramLines(DHAPI.getHologram(HOLO_NAME), lines);
    }

    private String getPosColor(int pos) {
        return switch (pos) {
            case 1 -> "&e&l"; // Ouro
            case 2 -> "&f&l"; // Prata
            case 3 -> "&6&l"; // Bronze
            default -> "&7";
        };
    }
}
