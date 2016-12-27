package de.alphahelix.dropparty.commands;

import de.alphahelix.alphalibary.command.SimpleCommand;
import de.alphahelix.dropparty.DropParty;
import de.alphahelix.dropparty.inventories.DropInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class DropCommand extends SimpleCommand<DropParty> {

    private static final HashMap<String, Long> TIME = new HashMap<>();

    public DropCommand(DropParty plugin) {
        super(plugin, "dropParty", "Create a new DropParty", "dP");
    }

    public static long getDelay(Player host) {
        if (TIME.containsKey(host.getName())) return TIME.get(host.getName());
        return 20 * 30;
    }

    @Override
    public boolean execute(CommandSender cs, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(getPlugin().getPrefix() + "§7You have to be a §cplayer §7to perform §cthis command§7!");
            return true;
        }
        Player p = (Player) cs;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (p.hasPermission(DropParty.getDropsFile().getString("permission to drop"))) {
                    new DropInventory(p).openInventory();
                    TIME.put(p.getName(), (long) (20 * 30));
                } else
                    p.sendMessage(getPlugin().getPrefix() + DropParty.getMessageFile().getColorString("Not enough permission"));
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                if (p.hasPermission(DropParty.getDropsFile().getString("permission to drop"))) {
                    new DropInventory(p).openInventory();
                    TIME.put(p.getName(), Long.parseLong(args[1]) * 20);
                } else
                    p.sendMessage(getPlugin().getPrefix() + DropParty.getMessageFile().getColorString("Not enough permission"));
            }
        } else {
            p.sendMessage("§8--== §eDropParty §8==-- \n" +
                    " §7/dropParty create [time until next drop in seconds] §8~ §7Creates a new DropParty at your current location!\n" +
                    "§8--==--==--==--==-- ");
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender cs, String label, String[] args) {
        return null;
    }
}
