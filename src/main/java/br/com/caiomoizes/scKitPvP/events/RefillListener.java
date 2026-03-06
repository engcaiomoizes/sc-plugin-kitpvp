package br.com.caiomoizes.scKitPvP.events;

import br.com.caiomoizes.scKitPvP.utils.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RefillListener implements Listener {
    @EventHandler
    public void onSignInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        // Será aceito tanto clique com botão esquerdo, quanto com botão direito
        if (e.getClickedBlock() == null) return;

        // Verifica todos os tipos de placas (carvalho, bétula, etc.)
        if (!(e.getClickedBlock().getState() instanceof Sign sign)) return;

        var frontSide = sign.getSide(Side.FRONT);

        // Verifica o conteúdo da placa
        String line0 = PlainTextComponentSerializer.plainText().serialize(frontSide.line(0));

        if (line0.equalsIgnoreCase("[Refill]")) {
            ItemUtil.fillInventoryWithSoup(p);
            p.sendMessage(Component.text("Inventário reabastecido!", NamedTextColor.GREEN));
        }
    }
}
