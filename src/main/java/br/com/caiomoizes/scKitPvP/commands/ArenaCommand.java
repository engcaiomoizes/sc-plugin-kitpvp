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

import java.util.Arrays;

public class ArenaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player p)) return true;

        if (args.length == 0) {
            new ArenaSelector(p);
            return true;
        }

        String subCmd = args[0];

        if (subCmd.equalsIgnoreCase("create")) {
            if (!p.hasPermission("kitpvp.arena.create")) {
                p.sendMessage(Component.text("Você não tem permissão para criar Arenas!", NamedTextColor.RED));
                return true;
            }
            CreateCommand(
                    p,
                    Arrays.copyOfRange(args, 1, args.length)
            );
            return true;
        }

        Arena arena = ArenaManager.getArena(subCmd);

        if (arena == null) {
            p.sendMessage(Component.text("A arena '" + subCmd + "' não existe!", NamedTextColor.RED));
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

    private void CreateCommand(Player p, String [] args) {
        if (args.length == 0) {
            p.sendMessage(Component.text("Informe o nome da Arena!", NamedTextColor.GOLD));
            return;
        }

        if (args.length > 1)
            ArenaManager.createArena(args[0], p.getLocation(), args[1]);
        else
            ArenaManager.createArena(args[0], p.getLocation());

        p.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(
                "&f&lArena &5&l" + args[0] + " &f&lcriada com sucesso!"
        ));
    }
}
