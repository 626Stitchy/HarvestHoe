package com.anubis.HarvestHoe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class HarvestHoeCommand implements CommandExecutor {
    private AnubisHarvestHoe plugin;

    public HarvestHoeCommand() {
        this.plugin = AnubisHarvestHoe.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] params) {
        if(params.length == 0) {
            sender.sendMessage(plugin.convertColors("&9HarvestHoe version " + plugin.getDescription().getVersion()));
            return false;
        }
        else if(params[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("harvesthoe.give")) {
                sender.sendMessage(plugin.convertColors(plugin.getConfigStringValue("noPermissions")));
            }
            if(params.length != 2) {
                sender.sendMessage(plugin.convertColors("&cInvalid Syntax. Use /harvesthoe give (playername) (amount)"));
                return false;
            }
            if (plugin.getServer().getPlayer(params[1]) == null) {
                sender.sendMessage(plugin.convertColors(plugin.getConfigStringValue("playerNotFound").replaceAll("%player%", params[1])));
                return false;
            }
            if(plugin.getServer().getPlayer(params[1]).getInventory().firstEmpty() == -1) {
                sender.sendMessage(plugin.convertColors("&cInventory of target is full!"));
            }

            ItemStack harvestHoe = new ItemStack(Material.DIAMOND_HOE, 1);
            ItemMeta meta = harvestHoe.getItemMeta();
            meta.setDisplayName(plugin.convertColors(plugin.getConfigStringValue("harvestHoeName")));
            List<String> lore = new ArrayList<>();
            lore.add(plugin.convertColors(plugin.getConfigStringValue("harvestHoeLore")));
            meta.setLore(lore);
            meta.addEnchant(Enchantment.DURABILITY, 10, true);

            NamespacedKey harvestHoeKey = new NamespacedKey(plugin, "harvestHoeKey");
            meta.getPersistentDataContainer().set(harvestHoeKey, PersistentDataType.INTEGER, 1);
            meta.setUnbreakable(true);

            harvestHoe.setItemMeta(meta);

            for(int i = 0; i < Integer.parseInt(params[2]); i++) {
                plugin.getServer().getPlayer(params[1]).getInventory().addItem(harvestHoe);
            }

            return true;

        }
        else if(params[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("harvesthoe.reload")) {
                sender.sendMessage(plugin.convertColors(plugin.getConfigStringValue("noPermissions")));
            }
            this.plugin.reloadConfig();
            this.plugin.saveConfig();
            sender.sendMessage(plugin.convertColors(plugin.getConfigStringValue("configReload")));
        }
        else {
            sender.sendMessage("&cUnrecognized command! Try /harvesthoe give or /harvesthoe reload");
        }

        return true;
    }
}
