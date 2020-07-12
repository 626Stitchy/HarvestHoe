package com.anubis.HarvestHoe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakEvents implements Listener {
    private AnubisHarvestHoe plugin;

    public List<Material> farmables = new ArrayList<>();

    public BlockBreakEvents() {
        this.plugin = AnubisHarvestHoe.getInstance();
        farmables.add(Material.WHEAT);
        farmables.add(Material.POTATOES);
        farmables.add(Material.CARROTS);
        farmables.add(Material.NETHER_WART);
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        if(!farmables.contains(e.getBlock().getType()) ||
                !plugin.getConfigStringList("allowed-worlds").contains(e.getBlock().getWorld().getName()) ||
                !isHarvestHoe(e.getPlayer().getInventory().getItem(e.getPlayer().getInventory().getHeldItemSlot()))) {
            e.setCancelled(true);
            return;
        }

        Ageable crop = (Ageable) e.getBlock().getBlockData();

        if(crop.getAge() != crop.getMaximumAge()) {
            return;
        }

        if(e.getPlayer().getInventory().firstEmpty() == -1) {
            e.getPlayer().sendTitle(plugin.convertColors(plugin.getConfigStringValue("inventoryFullTitle")), plugin.convertColors(plugin.getConfigStringValue("inventoryFullSubTitle")), 10, 100, 10);
            e.setCancelled(true);
            return;
        }

        for (ItemStack drop : e.getBlock().getDrops()) {
            e.getPlayer().getInventory().addItem(drop);
        }

        e.setDropItems(false);
        e.setCancelled(true);
        e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(e.getBlock().getType());
        }

    public boolean isHarvestHoe(ItemStack heldItem) {
        if(heldItem == null) {
            return false;
        }
        NamespacedKey harvestHoeKey = new NamespacedKey(plugin, "harvestHoeKey");
        ItemMeta meta = heldItem.getItemMeta();
        return meta.getPersistentDataContainer().has(harvestHoeKey, PersistentDataType.INTEGER);
    }
}
