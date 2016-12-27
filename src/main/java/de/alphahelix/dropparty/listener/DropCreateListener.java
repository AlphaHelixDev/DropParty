package de.alphahelix.dropparty.listener;

import de.alphahelix.alphalibary.listener.SimpleListener;
import de.alphahelix.dropparty.DropParty;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DropCreateListener extends SimpleListener<DropParty> {

    private static HashMap<String, ArmorStand> stands = new HashMap<>();

    public DropCreateListener(DropParty plugin) {
        super(plugin);
    }

    public static void removeStand(Player host) {
        if (stands.containsKey(host.getName())) {
            stands.get(host.getName()).remove();
            stands.remove(host.getName());
        }
    }

    public static int getItems(Inventory inventory) {
        int items = 0;
        for (ItemStack is : inventory.getContents()) {
            if (is != null) items++;
        }
        return items;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory() == null) return;
        if (!e.getInventory().getTitle().equalsIgnoreCase(DropParty.getMessageFile().getPlaceholderString("GUI name", "player", e.getPlayer().getName())))
            return;
        if (getItems(e.getInventory()) == 0) return;

        if (stands.containsKey(e.getPlayer().getName())) {
            DropParty.createDropParty((Player) e.getPlayer(), e.getPlayer().getLocation(), e.getInventory().getContents());
            e.getPlayer().sendMessage(getPluginInstance().getPrefix() + DropParty.getMessageFile().getColorString("DropParty active"));
            return;
        }

        ArmorStand holo = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), ArmorStand.class);

        holo.setGravity(true);
        holo.setVisible(false);
        holo.setCustomName(DropParty.getMessageFile().getPlaceholderString("Hologram display", "player", ((Player) e.getPlayer()).getDisplayName()));
        holo.setCustomNameVisible(true);

        stands.put(e.getPlayer().getName(), holo);
        DropParty.createDropParty((Player) e.getPlayer(), e.getPlayer().getLocation(), e.getInventory().getContents());
    }
}
