package de.alphahelix.dropparty.files;

import de.alphahelix.alphalibary.file.SimpleFile;
import de.alphahelix.dropparty.DropParty;

public class MessageFile extends SimpleFile<DropParty> {
    public MessageFile(DropParty plugin) {
        super("plugins/DropParty", "messages.dp", plugin);
    }

    @Override
    public void addValues() {
        setDefault("DropParty created", "&7The player &a[player] &7just created a &anew dropparty&7!");
        setDefault("DropParty active", "&7Sorry but please wait until your current &edropparty &7ended.");
        setDefault("Hologram display", "&7Dropparty of &a[player]");
        setDefault("GUI name", "&7Dropparty by &a[player]");
        setDefault("Not enough permission", "&7Sorry but you don't have those permissions.");
    }
}
