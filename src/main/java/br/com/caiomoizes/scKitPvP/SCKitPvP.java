package br.com.caiomoizes.scKitPvP;

import br.com.caiomoizes.scKitPvP.events.SoupListener;import net.kyori.adventure.text.Component;import net.kyori.adventure.text.format.NamedTextColor;import org.bukkit.Bukkit;import org.bukkit.plugin.PluginManager;import org.bukkit.plugin.java.JavaPlugin;

public final class SCKitPvP extends JavaPlugin {

    private static SCKitPvP instance;

    private CustomConfig kits;

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

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new SoupListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(Component.text("[SC-KitPvP] Desativado!", NamedTextColor.RED));
    }
}
