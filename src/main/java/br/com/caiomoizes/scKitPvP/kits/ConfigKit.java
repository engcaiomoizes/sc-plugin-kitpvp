package br.com.caiomoizes.scKitPvP.kits;

import br.com.caiomoizes.scKitPvP.SCKitPvP;
import br.com.caiomoizes.scKitPvP.player.PlayerData;
import br.com.caiomoizes.scKitPvP.player.PlayerDataManager;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConfigKit implements Kit {
    protected final String name;
    protected int abilityCooldown;
    protected int abilityDuration;
    protected final List<PotionEffect> potionEffects = new ArrayList<>();

    public ConfigKit(String name) {
        this.name = name;
        loadConfigValues();
    }

    private void loadConfigValues() {
        var config = SCKitPvP.getInstance().getConfig();
        String path = "kitpvp.kits." + name.toLowerCase();

        this.abilityCooldown = config.getInt(path + ".ability.cooldown", 0);
        this.abilityDuration = config.getInt(path + ".ability.duration", 0);

        // Carregar efeitos de Poção
        List<Map<?, ?>> effectsList = config.getMapList(path + ".effects");
        for (Map<?, ?> map : effectsList) {
            String typeName = (String) map.get("type");
            if (typeName == null) continue;

            PotionEffectType type = PotionEffectType.getByName(typeName.toUpperCase());
            if (type == null) continue;

            int duration = map.containsKey("duration") ?  ((Number) map.get("duration")).intValue() : Integer.MAX_VALUE;
            int amplifier = map.containsKey("amplifier") ? ((Number) map.get("amplifier")).intValue() : 0;

            potionEffects.add(new PotionEffect(type, duration, amplifier, false, false));
        }
    }

    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    public int getAbilityDuration() {
        return abilityDuration;
    }

    public List<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getIcon() {
        String path = getConfigPath();
        String materialName = getPlugin().getConfig().getString(path + ".display-item", "BARRIER");
        Material material = Material.matchMaterial(materialName);
        if (material == null) material = Material.PAPER;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize("&aKit: " + name));

            List<String> loreLines = getPlugin().getConfig().getStringList(path + ".lore");
            List<Component> componentLore = loreLines.stream()
                    .map(line -> LegacyComponentSerializer.legacyAmpersand().deserialize(line))
                    .collect(Collectors.toList());

            meta.lore(componentLore);
            item.setItemMeta(meta);
        }

        return item;
    }

    @Override
    public void apply(Player p) {
        // 1. Checagem de permissão
        if (!p.hasPermission("kitpvp.kit." + name.toLowerCase())) {
            p.sendMessage(Component.text("Você não tem permissão para usar este kit!", NamedTextColor.RED));
            return;
        }

        PlayerData data = PlayerDataManager.getPlayerData(p);
        data.incrementChangeCount();

        // 2. Limpeza total
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getActivePotionEffects().forEach(e -> p.removePotionEffect(e.getType()));

        // Aplicar efeitos de poção do config
        for (PotionEffect effect : getPotionEffects())
            p.addPotionEffect(effect);

        // 3. Definir o Kit no Manager
        KitManager.setKit(p, this);

        // 4. Aplicar itens e armadura
        String itemsPath = getConfigPath() + ".items";
        List<?> configItems = getPlugin().getConfig().getList(itemsPath);

        if (configItems != null) {
            for (Object obj : configItems) {
                if (!(obj instanceof Map<?, ?> map)) continue;

                ItemStack item = buildItemFromMap(map);
                if (item == null) continue;

                if (map.containsKey("slot")) {
                    int slot = ((Number) map.get("slot")).intValue();
                    p.getInventory().setItem(slot, item);
                } else {
                    p.getInventory().addItem(item);
                }
            }
        }

        applyArmor(p);

        // 5. Aplicar poções
//        applyPotionEffects(p);

        // 6. Preencher espaços vazios com sopas
        ItemStack soup = new ItemStack(Material.MUSHROOM_STEW);

        for (int i = 0; i < 36; i++) {
            ItemStack currentSlot = p.getInventory().getItem(i);

            if (currentSlot == null || currentSlot.getType() == Material.AIR)
                p.getInventory().setItem(i, soup);
        }

        p.sendMessage(LegacyComponentSerializer.legacyAmpersand()
                .deserialize("&aVocê selecionou o kit &l" + name.toUpperCase()));
    }

    private void applyArmor(Player player) {
        String armorPath = getConfigPath() + ".armor";
        var section = getPlugin().getConfig().getConfigurationSection(armorPath);
        if (section == null) return;

        // Mapeamento de chaves do YML para slots de armadura do Bukkit
        if (section.contains("helmet")) player.getInventory().setHelmet(buildItemFromMap(section.getConfigurationSection("helmet").getValues(false)));
        if (section.contains("chestplate")) player.getInventory().setChestplate(buildItemFromMap(section.getConfigurationSection("chestplate").getValues(false)));
        if (section.contains("leggings")) player.getInventory().setLeggings(buildItemFromMap(section.getConfigurationSection("leggings").getValues(false)));
        if (section.contains("boots")) player.getInventory().setBoots(buildItemFromMap(section.getConfigurationSection("boots").getValues(false)));
    }

    private ItemStack buildItemFromMap(Map<?, ?> map) {
        String matName = (String) map.get("material");

        if (matName != null && matName.equals("SKULL_ITEM"))
            matName = "PLAYER_HEAD";

        Material material = Material.matchMaterial(matName != null ? matName : "");
        if (material == null) return null;

        int amount = 1;
        if (map.get("amount") instanceof Number num) {
            amount = num.intValue();
        }

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Nome
            if (map.get("name") instanceof String name) {
                meta.displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(name));
            }

            // Lore
            if (map.get("lore") instanceof List<?> loreLines) {
                meta.lore(loreLines.stream()
                        .filter(String.class::isInstance)
                        .map(line -> LegacyComponentSerializer.legacyAmpersand().deserialize((String) line))
                        .toList());
            }

            // Unbreakable (A correção do seu erro aqui)
            if (map.get("unbreakable") instanceof Boolean unb) {
                meta.setUnbreakable(unb);
            }

            // Lógica da cabeça personalizada
            if (meta instanceof SkullMeta skullMeta && map.containsKey("base64")) {
                String base64 = (String) map.get("base64");
                applyBase64(skullMeta, base64);
            }

            item.setItemMeta(meta);
        }
        return item;
    }

    // Mantendo getItems caso precise para outra finalidade
    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        List<?> configItems = getPlugin().getConfig().getList(getConfigPath() + ".items");
        if (configItems == null) return items;

        for (Object obj : configItems) {
            if (obj instanceof Map<?, ?> map) {
                ItemStack item = buildItemFromMap(map);
                if (item != null) items.add(item);
            }
        }
        return items;
    }

    private void applyBase64(SkullMeta meta, String base64) {
        if (base64 == null || base64.isEmpty()) return;

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());

        profile.setProperty(new ProfileProperty("textures", base64));

        meta.setPlayerProfile(profile);
    }
}
