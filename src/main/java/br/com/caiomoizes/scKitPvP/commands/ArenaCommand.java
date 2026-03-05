package br.com.caiomoizes.scKitPvP.commands;

import br.com.caiomoizes.scKitPvP.arenas.Arena;
import br.com.caiomoizes.scKitPvP.arenas.ArenaManager;
import br.com.caiomoizes.scKitPvP.menus.ArenaSelector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArenaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player p)) return true;

        if (args.length == 0) {
            new ArenaSelector(p);
            return true;
        }

        String arenaName = args[0];
        Arena arena = ArenaManager.getArena(arenaName);

        if (arena == null) {
            p.sendMessage(Component.text("A arena '" + arenaName + "' não existe!", NamedTextColor.RED));
            return true;
        }

        if (!arena.getPermission().isEmpty() && !p.hasPermission(arena.getPermission())) {
            p.sendMessage(Component.text("Você precisa de VIP para entrar nesta arena!", NamedTextColor.RED));
            return true;
        }

        p.teleport(arena.getSpawn());
        p.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aTeleportado para " + arena.getDisplayName()));

        return true;
    }
}
