package br.com.caiomoizes.scKitPvP.commands;

import br.com.caiomoizes.scKitPvP.kits.Kit;
import br.com.caiomoizes.scKitPvP.kits.KitManager;
import br.com.caiomoizes.scKitPvP.menus.KitSelector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player p)) return true;

        if (args.length == 0) {
            new KitSelector(p, 0);
            return true;
        }

        String kitName = args[0];
        Kit kit = KitManager.getKit(kitName);

        if (kit == null) {
            p.sendMessage(Component.text("O kit '" + kitName + "' não existe!", NamedTextColor.RED));
            return true;
        }

        kit.apply(p);

        return true;
    }
}
