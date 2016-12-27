package de.alphahelix.dropparty;

import de.alphahelix.alphalibary.AlphaPlugin;
import de.alphahelix.dropparty.commands.DropCommand;
import de.alphahelix.dropparty.files.DropsFile;
import de.alphahelix.dropparty.files.MessageFile;
import de.alphahelix.dropparty.listener.DropCreateListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public final class DropParty extends AlphaPlugin {

    private static final ArrayList<String> actives = new ArrayList<>();
    private static final Random RANDOM = new Random();
    private static DropParty instance;

    private static MessageFile messageFile;
    private static DropsFile dropsFile;

    public static void createDropParty(Player host, Location where, ItemStack... items) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        if (actives.contains(host.getName())) {
            for (ItemStack is : items) {
                if (is != null) host.getInventory().addItem(is);
            }
            return;
        }

        for (ItemStack is : items) {
            if (is != null) {
                if (getDropsFile().canDrop(host, is.getType())) {
                    itemStacks.add(is);
                } else {
                    host.getInventory().addItem(is);
                }
            }
        }

        Bukkit.broadcastMessage(instance.getPrefix() + DropParty.getMessageFile().getPlaceholderString("DropParty created", "player", host.getDisplayName()));
        actives.add(host.getName());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (itemStacks.size() == 0) {
                    actives.remove(host.getName());
                    DropCreateListener.removeStand(host);
                    this.cancel();
                } else {
                    where.getWorld().dropItemNaturally(where.clone().add(RANDOM.nextInt(2), 0, RANDOM.nextInt(2)), itemStacks.get(0));

                    itemStacks.remove(itemStacks.get(0));
                }
            }
        }.runTaskTimer(instance, 0, DropCommand.getDelay(host));
    }

    public static MessageFile getMessageFile() {
        return messageFile;
    }

    public static DropsFile getDropsFile() {
        return dropsFile;
    }

    @Override
    public void onEnable() {
        this.setPrefix("§7[§eDrop§7-§eParty§7] ");
        instance = this;
        messageFile = new MessageFile(this);
        dropsFile = new DropsFile(this);
        getMessageFile().addValues();
        getDropsFile().addValues();
        new DropCommand(this);
        new DropCreateListener(this);
    }
}
