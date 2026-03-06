package br.com.caiomoizes.scKitPvP.commands;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class KitPvpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player p) || (args.length == 0)) return true;

        if (!p.hasPermission("kitpvp.use")) {
            p.sendMessage(Component.text("Você não tem permissão para usar este comando!", NamedTextColor.RED));
            return true;
        }

        String subCmd = args[0];

        switch (subCmd) {
            case "hologram":
                HologramCommand(
                        p,
                        Arrays.copyOfRange(args, 1, args.length)
                );
                break;
            default:
                break;
        }

        return true;
    }

    private void HologramCommand(Player p, String [] args) {
        if (args.length == 0) {
            p.sendMessage(Component.text("Use: /kitpvp hologram <setranking>", NamedTextColor.RED));
            return;
        }

        String action = args[0];

        switch (action) {
            case "setranking":
                if (!p.hasPermission("kitpvp.hologram.use")) {
                    p.sendMessage(Component.text("Você não tem permissão para usar este comando!", NamedTextColor.RED));
                    return;
                }
                SCKitPvP.getInstance().getRankingHologramManager().setHologramLocation(p.getLocation());
                p.sendMessage(Component.text("Posição do Ranking definida!", NamedTextColor.GREEN));
                break;
            default:
                break;
        }
    }
}
