package de.alphahelix.dropparty.inventories;

import de.alphahelix.dropparty.DropParty;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DropInventory {

    private Inventory dropInventory;
    private Player host;

    public DropInventory(Player p) {
        dropInventory = Bukkit.createInventory(p, 9*5, DropParty.getMessageFile().getPlaceholderString("GUI name", "player", p.getName()));
        this.host = p;
    }

    public void openInventory() {
        host.openInventory(dropInventory);
    }
}
