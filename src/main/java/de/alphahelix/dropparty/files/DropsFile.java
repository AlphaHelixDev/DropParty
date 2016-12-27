package de.alphahelix.dropparty.files;

import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.dropparty.DropParty;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DropsFile extends SimpleFile<DropParty> {

    public DropsFile(DropParty plugin) {
        super("plugins/DropParty", "drops.dp", plugin);
    }

    @Override
    public void addValues() {
        setDefault("permission to drop", "drop");
        setMaterialStringList("Banned items.users",
                "diamond",
                "diamond_sword",
                "diamond_chestplate");
        setMaterialStringList("Banned items.admins",
                "stone");
    }

    public boolean canDrop(Player p, Material m) {
        for (String permission : getConfigurationSection("Banned items").getKeys(false)) {
            if (!p.hasPermission("dropparty." + permission)) continue;

            return !getMaterialStringList("Banned items." + permission).contains(m.name().toLowerCase().replace("_", " "));
        }
        return true;
    }
}
