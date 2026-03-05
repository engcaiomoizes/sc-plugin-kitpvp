package br.com.caiomoizes.scKitPvP.kits.types;

import br.com.caiomoizes.scKitPvP.kits.ConfigKit;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class Archer extends ConfigKit {
    public Archer() {
        super("Archer");
    }

    @Override
    public void apply(Player p) {
        super.apply(p);

        p.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(
                "&a&lDICA: &fUse a pena para ganhar velocidade e resistência!"
        ));
    }
}
