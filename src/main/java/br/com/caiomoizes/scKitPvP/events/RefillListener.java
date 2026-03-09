package br.com.caiomoizes.scKitPvP.events;

import br.com.caiomoizes.scKitPvP.utils.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RefillListener implements Listener {
    @EventHandler
    public void onSignInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        // Não será aceito clique com botão esquerdo, apenas com botão direito
        if (e.getClickedBlock() == null || e.getAction().isLeftClick()) return;

        // Verifica todos os tipos de placas (carvalho, bétula, etc.)
        if (!(e.getClickedBlock().getState() instanceof Sign sign)) return;

        var frontSide = sign.getSide(Side.FRONT);

        // Verifica o conteúdo da placa
        String line0 = PlainTextComponentSerializer.plainText().serialize(frontSide.line(0));

        if (line0.equalsIgnoreCase("[Refill]")) {
            e.setCancelled(true); // Para evitar que abra a edição da placa
            ItemUtil.fillInventoryWithSoup(p);
            p.sendMessage(Component.text("Inventário reabastecido!", NamedTextColor.GREEN));
        }
    }

    @EventHandler
    public void onPlaceSign(SignChangeEvent e) {
        if (e.line(0) == null) return;

        String line0 = PlainTextComponentSerializer.plainText().serialize(e.line(0));

        if (line0.equalsIgnoreCase("[Refill]")) {
            if (!e.getPlayer().hasPermission("kitpvp.refill.create")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Component.text(
                        "Você não tem permissão para criar placas de refill!",
                        NamedTextColor.RED
                ));
                return;
            }

            e.line(0, Component.text("[Refill]", NamedTextColor.AQUA, TextDecoration.BOLD));
            e.line(1, Component.text("Clique para", NamedTextColor.GRAY));
            e.line(2, Component.text("reabastecer!", NamedTextColor.GRAY));

            e.getPlayer().sendMessage(Component.text("Placa de Refill criada com sucesso!", NamedTextColor.GREEN));
        }
    }
}
