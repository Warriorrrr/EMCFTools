package dev.warriorrr.emcftools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.PortalCreateEvent;

import net.md_5.bungee.api.ChatColor;

import static dev.warriorrr.emcftools.EMCFTools.prefix;

public class BlockListener implements Listener {
    /*
    * Prevent nether portals from breaking bedrock
    */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPortalCreate(PortalCreateEvent event) {
        for (BlockState blockState : event.getBlocks()) {
            if (blockState.getY() == 0)
                blockState.setBlockData(Bukkit.createBlockData(Material.BEDROCK));
        }
    }

    /*
    * Prevent blocks from being placed in places where bedrock is broken
    */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getY() == 0 && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getPlayer().sendMessage(prefix + ChatColor.RED + " You cannot place a block at this location.");
            event.getBlock().setType(Material.BEDROCK);
        }
    }

    /*
    * Also prevent blocks from being placed, instead replace it with bedrock.
    */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getY() == 0 && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getPlayer().sendMessage(prefix + ChatColor.RED + " You cannot break a block at this location.");
            event.setCancelled(true);
            event.getBlock().setType(Material.BEDROCK);
        }
    }
}
