package br.com.caiomoizes.scKitPvP;

import br.com.caiomoizes.scKitPvP.arenas.ArenaManager;
import br.com.caiomoizes.scKitPvP.commands.ArenaCommand;
import br.com.caiomoizes.scKitPvP.commands.KitCommand;
import br.com.caiomoizes.scKitPvP.commands.KitPvpCommand;
import br.com.caiomoizes.scKitPvP.database.DatabaseManager;
import br.com.caiomoizes.scKitPvP.events.*;
import br.com.caiomoizes.scKitPvP.events.kits.ArcherListener;
import br.com.caiomoizes.scKitPvP.events.kits.StomperListener;
import br.com.caiomoizes.scKitPvP.holograms.RankingHologramManager;
import br.com.caiomoizes.scKitPvP.kits.KitManager;
import br.com.caiomoizes.scKitPvP.player.PlayerData;
import br.com.caiomoizes.scKitPvP.player.PlayerDataManager;
import net.kyori.adventure.text.Component;import net.kyori.adventure.text.format.NamedTextColor;import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

public final class SCKitPvP extends JavaPlugin {

    private static SCKitPvP instance;

    private CustomConfig kits;

    private DatabaseManager dbManager;
    private RankingHologramManager rankingHologramManager;

    public DatabaseManager getDbManager() {
        return this.dbManager;
    }

    public RankingHologramManager getRankingHologramManager() {
        return this.rankingHologramManager;
    }

    public static SCKitPvP getInstance() {
        return instance;
    }

    public CustomConfig getKits() {
        return this.kits;
    }

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getServer().getConsoleSender().sendMessage(Component.text("[SC-KitPvP] Ativado!", NamedTextColor.GREEN));

        saveDefaultConfig();

        KitManager.loadKits();
        ArenaManager.loadArenas();

        registerCommand(
                "kit",
                "Abrir menu/Selecionar kit",
                List.of("kit"),
                "kitpvp.kit.use",
                "/kit <kit>",
                new KitCommand()
        );
        registerCommand(
                "arena",
                "Abrir menu/Selecionar arena",
                List.of("kit"),
                "kitpvp.arena.use",
                "/arena <arena>",
                new ArenaCommand()
        );
        registerCommand(
                "kitpvp",
                "Comandos admin do KitPvP",
                List.of("kitpvp"),
                "kitpvp.use",
                "/kitpvp <subCommand>",
                new KitPvpCommand()
        );

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new SoupListener(), this);
        pm.registerEvents(new MenuListener(), this);
        pm.registerEvents(new ArcherListener(), this);
        pm.registerEvents(new StomperListener(), this);
        pm.registerEvents(new RefillListener(), this);
        pm.registerEvents(new CombatLogListener(), this);
        pm.registerEvents(new PlayerListener(this), this);

        this.dbManager = new DatabaseManager();
        this.dbManager.init();

        this.rankingHologramManager = new RankingHologramManager(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(Component.text("[SC-KitPvP] Desativado!", NamedTextColor.RED));

        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData data = PlayerDataManager.getPlayerData(p);
            dbManager.savePlayerData(data);
        }
        dbManager.close();
    }

    public void registerCommand(String name, String description, List<String> aliases, String permission, String usage, CommandExecutor executor) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            BukkitCommand command = new BukkitCommand(name) {
                @Override
                public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String @NotNull [] args) {
                    return executor.onCommand(sender, this, label, args);
                }
            };

            command.setDescription(description);
            command.setAliases(aliases);
            command.setPermission(permission);
            command.setUsage(usage);

            commandMap.register("snowcraft", command);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
