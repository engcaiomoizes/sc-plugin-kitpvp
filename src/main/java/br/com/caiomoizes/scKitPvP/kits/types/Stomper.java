package br.com.caiomoizes.scKitPvP.kits.types;

import br.com.caiomoizes.scKitPvP.kits.ConfigKit;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class Stomper extends ConfigKit {
    public Stomper() {
        super("Stomper");
    }

    @Override
    public void apply(Player p) {
        super.apply(p);

        p.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(
                "&a&lDICA: &fPule em cima dos inimigos para causar dano!"
        ));
    }
}
